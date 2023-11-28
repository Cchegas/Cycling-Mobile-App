package com.example.cyclingmobileapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.regex.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cyclingmobileapp.lib.event.Event;

import com.example.cyclingmobileapp.lib.event.EventType;
import com.example.cyclingmobileapp.lib.user.ClubAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    private EditText descriptionEditText;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);
        //
        //
        fetchEventTypes();

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
        descriptionEditText = findViewById(R.id.descriptionEditText);
        //

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

        // Check if the event already exists
        db.collection("Events")
                .document(event.getTitle())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                makeToast("Event with the same Name already exists!");
                                activateButton.setEnabled(true);
                            } else {
                                // Event does not exist, proceed with adding
                                // Convert the Event object to a Map using toString()
                                Map<String, Object> eventMap = new HashMap<>();

                                eventMap.put("title", event.getTitle());
                                eventMap.put("location", event.getLocation());
                                eventMap.put("description", event.getDescription());
                                eventMap.put("difficulty", event.getDifficulty());
                                eventMap.put("participantLimit", event.getParticipantLimit());
                                eventMap.put("fee", event.getFee());
                                eventMap.put("startDate", event.getStartDate().toString());
                                eventMap.put("endDate", event.getEndDate().toString());
                                eventMap.put("organizer", event.getOrganizer());
                                eventMap.put("eventType", event.getEventType().getLabel());

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
                        } else {
                            // Error checking if the event exists
                            makeToast("Error checking if the event exists. Try again.");
                            activateButton.setEnabled(true);
                        }
                    }
                });
    }



    public void onCreateAButtonClick(View view) {
        if (areFieldsEmpty()) {
            makeToast("Fill in all the fields!");
        } else {
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
            String description = descriptionEditText.getText().toString().trim();

//
            if (isValidDate(date) && isValidTime(startTime) && isValidTime(endTime)) {
               // String username = getIntent().getExtras().getString("username");
                Event event = new Event(eventName, DateTimeConversion(startTime, date), DateTimeConversion(endTime, date), zipCode, description, difficultyLevel, participantLimit, (int) registrationFees, null, null);
                EventType eventType1 = new EventType(eventType, true);
               // ClubAccount clubAccount =new ClubAccount(username,null,null,null);
                //event.setOrganizer(clubAccount);
                event.setEventType(eventType1);
                addEvent(event, createEventButton);
            } else {
                makeToast("Invalid date or time format !");
            }

        }

    }

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
        if (areFieldsEmpty()) {
            makeToast("Fill in all the fields!");
        } else {
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
            ZonedDateTime startDateTime = DateTimeConversion(startTime, date);
            ZonedDateTime endDateTime = DateTimeConversion(endTime, date);
            Event event = new Event(eventName, startDateTime, endDateTime, zipCode, null, difficultyLevel, participantLimit, (int) registrationFees, null, null);
            EventType eventType1=new EventType(eventType,true);
            event.setEventType(eventType1);
            updateEvent(event, updateEventButton);
        }
        }
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

        // Check if 'description' exists
        if (document.contains("description") && document.getString("description") != null) {
            descriptionEditText.setText(document.getString("description"));
        }

        // Check if 'participantLimit' exists and is a valid long
        if (document.contains("participantLimit") && document.getLong("participantLimit") != null) {
            participantLimitEditText.setText(String.valueOf(document.getLong("participantLimit")));
        }

        // Check if 'zipCode' exists
        if (document.contains("location")) {
            zipCodeEditText.setText(document.getString("location"));
        }
        // Check if 'startDate'  exist and fill date fields
        if (document.contains("startDate") && document.getString("startDate") != null) {
            String startDateString = document.getString("startDate");

            dateEditText.setText(extractDate(startDateString));
            // Assuming you have an endDateEditText for the end date

        }
        if ((document.contains("startDate") && document.getString("startDate") != null)&&(document.contains("endDate") && document.getString("endDate") != null)) {
            String startTimeString = document.getString("startDate");
            String endTimeString = document.getString("endDate");


                startTimeEditText.setText(extractTime(startTimeString));
                //
                endTimeEditText.setText(extractTime(endTimeString));

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
    private  String extractTime(String dateTimeString) {
        String timeString = dateTimeString.substring(11,16);
        return timeString;
    }
    private  String extractDate(String dateTimeString) {
        String dateString = dateTimeString.substring(0, 10);
        dateString =replaceDashesWithSlashes( dateString);
        return dateString;
    }


    private  String replaceDashesWithSlashes(String dateString) {
        String newDateString = dateString.replace("-", "/");
        return newDateString;
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
    }
    private boolean areFieldsEmpty() {
        if (eventNameEditText.getText().toString().trim().isEmpty() ||
                eventTypeSpinner.getSelectedItem().toString().isEmpty() ||
                difficultyLevelRadioGroup.getCheckedRadioButtonId() == -1 ||
                registrationFeesEditText.getText().toString().trim().isEmpty() ||
                participantLimitEditText.getText().toString().trim().isEmpty() ||
                zipCodeEditText.getText().toString().trim().isEmpty() ||
                dateEditText.getText().toString().trim().isEmpty() ||
                startTimeEditText.getText().toString().trim().isEmpty() ||
                endTimeEditText.getText().toString().trim().isEmpty() ||
                descriptionEditText.getText().toString().trim().isEmpty()) {
            return true;
        }
        return false;
    }
    private boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            sdf.setLenient(false);
            Date parsedDate = sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidTime(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setLenient(false);
            Date parsedTime = sdf.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}


