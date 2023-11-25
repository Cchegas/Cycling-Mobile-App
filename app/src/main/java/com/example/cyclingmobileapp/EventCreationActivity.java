package com.example.cyclingmobileapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cyclingmobileapp.lib.event.Event;
import com.example.cyclingmobileapp.lib.event.EventType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventCreationActivity extends AppCompatActivity {
    private EditText eventNameEditText;
    private Spinner eventTypeSpinner;
    private RadioGroup difficultyLevelRadioGroup;
    private EditText registrationFeesEditText;
    private EditText participantLimitEditText;
    private EditText zipCodeEditText;
    private EditText dateEditText;
    private EditText startTimeEditText;
    private EditText endTimeEditText;

    private Button createEventButton,updateEventButton,deleteEventButton,getInfoButton;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);
        //
        //
        eventNameEditText = findViewById(R.id.editTextEventName);
        eventTypeSpinner = findViewById(R.id.eventTypeSpinner);
        difficultyLevelRadioGroup = findViewById(R.id.difficultyLevelRadioGroup);
        registrationFeesEditText = findViewById(R.id.editTextRegistrationFees);
        participantLimitEditText = findViewById(R.id.editTextParticipantLimit);
        zipCodeEditText = findViewById(R.id.editTextZipCode);
        dateEditText = findViewById(R.id.editTextDate);
        startTimeEditText = findViewById(R.id.editStartTime);
        endTimeEditText = findViewById(R.id.editEndTime);
        createEventButton = findViewById(R.id.createEventButton);
        updateEventButton = findViewById(R.id.updateEventButton);
        deleteEventButton = findViewById(R.id.deleteEvent);
        getInfoButton = findViewById(R.id.getInfoButton);
        //
        fetchEventTypes();
        ///

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateAButtonClick(view);

            }
        });

        updateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpdateButtonClick(view);
            }
        });
        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteButton(view);
            }
        });
        getInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGetInfoButtonClick(view);
            }
        });




    }
    private String getSelectedDifficulty(int selectedId) {
        if (selectedId == R.id.easyRadioButton) {
            return "Easy";
        } else if (selectedId == R.id.mediumRadioButton) {
            return "Medium";
        } else if (selectedId == R.id.hardRadioButton) {
            return "Hard";
        } else if (selectedId == R.id.extremeRadioButton) {
            return "Extreme";
        } else {
            return "";
        }
    }

    private void addEvent(Event event, Button activateButton) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        activateButton.setEnabled(false);

        // Convert the Event object to a Map using toString()
        String eventString = event.toString();
        String[] keyValuePairs = eventString.split(System.getProperty("line.separator"));

        Map<String, Object> eventMap = new HashMap<>();

        eventMap.put("title", event.getTitle());
        eventMap.put("location", event.getLocation());
        eventMap.put("description", event.getDescription());
        eventMap.put("difficulty", event.getDifficulty());
        eventMap.put("participantLimit", event.getParticipantLimit());
        eventMap.put("fee", event.getFee());
        eventMap.put("startDate", event.getStartDate());
        eventMap.put("endDate", event.getEndDate());
        eventMap.put("organizer", event.getOrganizer());
        eventMap.put("eventType", event.getEventType());


        // Set the eventMap directly to Firestore
        db.collection("Events").document(event.getTitle()).set(eventMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    makeToast("Event created successfully!");
                    finish();
                } else {
                    makeToast("Something unexpected occurred. Try again.");
                    activateButton.setEnabled(true);
                }
            }
        });
    }


    public void onCreateAButtonClick(View view) {
        String eventName = eventNameEditText.getText().toString().trim();
        String eventType = eventTypeSpinner.getSelectedItem().toString();
        String difficultyLevel = getSelectedDifficulty(difficultyLevelRadioGroup.getCheckedRadioButtonId());
        String registrationFeesString = registrationFeesEditText.getText().toString().trim();
        double registrationFees = Double.parseDouble(registrationFeesString);
        int participantLimit = Integer.parseInt(participantLimitEditText.getText().toString().trim());
        String zipCode = zipCodeEditText.getText().toString().trim();
     String date = dateEditText.getText().toString().trim();
     String startTime = startTimeEditText.getText().toString().trim();
     String endTime = endTimeEditText.getText().toString().trim();
        System.out.println("test 1");
       // if(isValidDate(date) && isValidTime(startTime) && isValidTime(endTime)&& regFeeValid(registrationFees)){
        Event event = new Event(eventName, DateTimeConversion(startTime,date), DateTimeConversion(endTime,date), zipCode, null, difficultyLevel, participantLimit, (int) registrationFees, null, null);
        System.out.println("test 2");
        addEvent(event, createEventButton);}//else{
           // makeToast("Enter valid fields!!");

      //  }

    private void updateEvent(Event event, Button updateButton) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        updateButton.setEnabled(false);
        Event backup = event;

        deleteEventU(event.getTitle(), updateButton, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Delete successful, now add the updated event
                    addEvent(backup, updateButton);
                    makeToast("Updated Event");
                } else {
                    // Delete failed, show a message and enable the button
                    makeToast("Failed to update event. Try again.");
                    updateButton.setEnabled(true);
                }
            }
        });
    }

    private void deleteEventU(String eventName, Button deleteButton, OnCompleteListener<Void> onCompleteListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        deleteButton.setEnabled(false);

        db.collection("Events")
                .document(eventName)  // Use the event name as the document ID
                .delete()
                .addOnCompleteListener(onCompleteListener);
    }

    public void onUpdateButtonClick(View view){
        String eventName = eventNameEditText.getText().toString().trim();
        String eventType = eventTypeSpinner.getSelectedItem().toString();
        String difficultyLevel = getSelectedDifficulty(difficultyLevelRadioGroup.getCheckedRadioButtonId());
        String registrationFeesString = registrationFeesEditText.getText().toString().trim();
        double registrationFees = Double.parseDouble(registrationFeesString);
        int participantLimit = Integer.parseInt(participantLimitEditText.getText().toString().trim());
        String zipCode = zipCodeEditText.getText().toString().trim();
    String date = dateEditText.getText().toString().trim();
     String startTime = startTimeEditText.getText().toString().trim();
     String endTime = endTimeEditText.getText().toString().trim();
//if(isValidDate(date) && isValidTime(startTime) && isValidTime(endTime)&& regFeeValid(registrationFees)){
        ZonedDateTime startDateTime = DateTimeConversion(startTime, date);
        ZonedDateTime endDateTime = DateTimeConversion(endTime, date);
        Event event = new Event(eventName, startDateTime, endDateTime, zipCode, null, difficultyLevel, participantLimit, (int) registrationFees, null, null);

        updateEvent(event, updateEventButton);}//else{
   // makeToast("enter valid fields");


   // }
    public void onDeleteButton(View view){
        String eventName = eventNameEditText.getText().toString().trim();

deleteEvent(eventName,deleteEventButton);

    }

    private void deleteEvent(String eventName, Button deleteButton) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        deleteButton.setEnabled(false);

        db.collection("Events")
                .document(eventName)  // Use the event name as the document ID
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            if (documentSnapshot.exists()) {
                                // The document exists, proceed with deletion
                                db.collection("Events")
                                        .document(eventName)
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    makeToast("Event deleted successfully!");
                                                    finish();
                                                } else {
                                                    makeToast("Something unexpected occurred. Try again.");
                                                    deleteButton.setEnabled(true);
                                                }
                                            }
                                        });
                            } else {
                                // The document does not exist
                                makeToast("Event with name " + eventName + " does not exist!");
                                deleteButton.setEnabled(true);
                            }
                        } else {
                            // Error getting document
                            makeToast("Error checking document existence. Try again.");
                            deleteButton.setEnabled(true);
                        }
                    }
                });
    }
    private void onGetInfoButtonClick(View view) {
        String eventName = eventNameEditText.getText().toString().trim();

        // Check if the event name is not empty
        if (!eventName.isEmpty()) {
            // Call a method to fetch event information by name
            fetchEventInfo(eventName);
        } else {
            makeToast("Enter event name to fetch information");
        }
    }

    private void fetchEventInfo(String eventName) {
        // Use FirebaseFirestore to fetch event information from the database
        db.collection("Events")
                .document(eventName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Event information found, fill in the fields
                                fillFieldsFromDocument(document);
                            } else {
                                makeToast("Event not found");
                            }
                        } else {
                            makeToast("Error fetching event information");
                        }
                    }
                });
    }

    private void fillFieldsFromDocument(DocumentSnapshot document) {
        // Extract data from the document and fill in the corresponding fields
        eventNameEditText.setText(document.getString("title"));

        // Set the selected item of the eventTypeSpinner based on the data
        String eventType = document.getString("eventType");
        if (eventType != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) eventTypeSpinner.getAdapter();
            int position = adapter.getPosition(eventType);
            if (position != -1) {
                eventTypeSpinner.setSelection(position);
            }
        }

        difficultyLevelRadioGroup.check(getRadioButtonIdFromDifficulty(document.getString("difficulty")));

        // Check if 'fee' exists and is a valid double
        if (document.contains("fee") && document.getDouble("fee") != null) {
            registrationFeesEditText.setText(String.valueOf(document.getDouble("fee")));
        }

        // Check if 'participantLimit' exists and is a valid long
        if (document.contains("participantLimit") && document.getLong("participantLimit") != null) {
            participantLimitEditText.setText(String.valueOf(document.getLong("participantLimit")));
        }

        // Check if 'zipCode' exists
        if (document.contains("location")) {
            zipCodeEditText.setText(document.getString("location"));
        }

        // Check if 'startDate' exists
        if (document.contains("startDate")) {
            Object startDateObject = document.get("startDate");

            if (startDateObject instanceof Timestamp) {
                // If 'startDate' is a Timestamp, convert it to Date and display
                Timestamp startDateTimestamp = (Timestamp) startDateObject;
                dateEditText.setText(formatDateFromDocument(startDateTimestamp.toDate()));
                startTimeEditText.setText(formatTimeFromDocument(startDateTimestamp.toDate()));
            }
        }

        // Check if 'endDate' exists
        if (document.contains("endDate")) {
            Object endDateObject = document.get("endDate");

            if (endDateObject instanceof Timestamp) {
                // If 'endDate' is a Timestamp, convert it to Date and display
                Timestamp endDateTimestamp = (Timestamp) endDateObject;
                endTimeEditText.setText(formatTimeFromDocument(endDateTimestamp.toDate()));
            }
        }

        makeToast("Event information retrieved");
    }


    private int getRadioButtonIdFromDifficulty(String difficulty) {
        if ("Easy".equals(difficulty)) {
            return R.id.easyRadioButton;
        } else if ("Medium".equals(difficulty)) {
            return R.id.mediumRadioButton;
        } else if ("Hard".equals(difficulty)) {
            return R.id.hardRadioButton;
        } else if ("Extreme".equals(difficulty)) {
            return R.id.extremeRadioButton;
        } else {

            return 0;
        }
    }
    private String formatDateFromDocument(Date date) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            return dateFormat.format(date);

    }

    private String formatTimeFromDocument(Date date) {

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            return timeFormat.format(date);

    }







    private ZonedDateTime DateTimeConversion(String timeString, String dateString) {
        // Define the date and time formatters
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Parse the strings to LocalDate and LocalTime
        LocalDate localDate = LocalDate.parse(dateString, dateFormatter);
        LocalTime localTime = LocalTime.parse(timeString, timeFormatter);

        // Combine LocalDate and LocalTime to create ZonedDateTime
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.systemDefault());

        return zonedDateTime;
    }

    private void makeToast(String toastText) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }
    private void fetchEventTypes() {
        db = FirebaseFirestore.getInstance();

        db.collection("EventTypes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<EventType> eventTypes = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String label = document.getString("label");
                                boolean enabled = document.getBoolean("enabled");

                                // Create an EventType object
                                EventType eventType = new EventType(label, enabled);
                                eventTypes.add(eventType);
                            }

                            // Now eventTypes list contains EventType objects


                            // For example, to populate a Spinner
                            List<String> eventTypeLabels = new ArrayList<>();
                            for (EventType eventType : eventTypes) {
                                eventTypeLabels.add(eventType.getLabel());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    EventCreationActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    eventTypeLabels
                            );
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            eventTypeSpinner.setAdapter(adapter);

                        } else {
                            Toast.makeText(EventCreationActivity.this, "Error fetching event types", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }}

//    public CompletableFuture<ClubAccount> getOrganizer() {
//        CompletableFuture<ClubAccount> future = new CompletableFuture<>();
//
//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference userRef = db.collection("users").document(uid);
//
//        userRef.get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        // User data exists
//                        ClubAccount organizer = new ClubAccount(null,null,null,null);
//                        organizer.setUsername(documentSnapshot.getString("username"));
//                        // Set other organizer details
//
//                        System.out.println("Organizer: " + organizer.getUsername());
//                        future.complete(organizer);
//                    } else {
//                        // User data doesn't exist
//                        System.out.println("Organizer data not found");
//                        future.completeExceptionally(new RuntimeException("Organizer data not found"));
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    // Handle errors
//                    System.err.println("Error getting organizer data: " + e.getMessage());
//                    future.completeExceptionally(e);
//                });
//
//        return future;
//    }
//
//    public void assignOrganizerToEvent(Event event) {
//        getOrganizer().thenAccept(organizer -> {
//
//            event.setOrganizer(organizer);
//        }).exceptionally(ex -> {
//            // Handle exceptions here
//            System.err.println("Exception: " + ex.getMessage());
//            return null;
//        });
//    }



//        private boolean isValid(String){
//        return false;
//        }
/////////////////////////////////////////////////////////////////////////////////////////////////////
    //Validation fields
//
//    private  boolean isValidCoordinate(double latitude, double longitude) {
//        // Assuming typical range for latitude is -90 to 90, and for longitude is -180 to 180
//        return (latitude >= -90 && latitude <= 90) && (longitude >= -180 && longitude <= 180);
//    }
//    private boolean regFeeValid(double regFee){
//        return regFee >= 0;
//    }
//}
