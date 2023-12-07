package com.example.cyclingmobileapp;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cyclingmobileapp.lib.event.Event;
import com.example.cyclingmobileapp.lib.event.EventType;
import com.example.cyclingmobileapp.lib.event.RequiredField;
import com.example.cyclingmobileapp.lib.utils.ValidationUtil;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
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

public class EventActivity extends AppCompatActivity {
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final String TIME_FORMAT = "HH:mm";
    String clubUsername, eventDocumentId;
    List<EventType> eventTypes;
    List<String> eventTypeIds;
    Map<EditText, String> requiredFieldInputMap;
    private EditText eventTitleEditText;
    private Spinner eventTypeSpinner;
    private RadioGroup difficultyLevelRadioGroup;
    private EditText registrationFeesEditText, participantLimitEditText, postalCodeEditText,
            dateEditText, startTimeEditText, endTimeEditText, descriptionEditText;
    private Button eventDoneButton, eventDeleteButton;
    private LinearLayout eventTypeDetailsLinearLayout;
    private TextView eventHeader;

    private List<String> eventParticipants;

    public EventActivity(){

    }

    //unit test case constructor
    public EventActivity(Context context){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.eventToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Check for the club's username and for an event title (optional, if it exists the event
        // is being modified)
        eventDocumentId = null;
        if (getIntent().getExtras() != null) {
            clubUsername = getIntent().getExtras().getString("username");
            if (getIntent().getExtras().getString("eventDocumentId") != null) {
                eventDocumentId = getIntent().getExtras().getString("eventDocumentId");
            }
        }

        eventTitleEditText = findViewById(R.id.editTextEventTitle);
        eventTypeSpinner = findViewById(R.id.eventTypeSpinner);
        difficultyLevelRadioGroup = findViewById(R.id.difficultyLevelRadioGroup);
        registrationFeesEditText = findViewById(R.id.editTextRegistrationFees);
        participantLimitEditText = findViewById(R.id.editTextParticipantLimit);
        postalCodeEditText = findViewById(R.id.editTextPostalCode);
        dateEditText = findViewById(R.id.editTextDate);
        startTimeEditText = findViewById(R.id.editStartTime);
        endTimeEditText = findViewById(R.id.editEndTime);
        eventDoneButton = findViewById(R.id.eventDoneButton);
        eventDeleteButton = findViewById(R.id.eventDeleteButton);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        eventTypeDetailsLinearLayout = findViewById(R.id.eventTypeDetailsLinearLayout);
        eventHeader = findViewById(R.id.eventHeader);
        eventParticipants = new ArrayList<>();

        // Check to see if the event is being modified or being created from scratch
        if (eventDocumentId == null) {
            // The event is being created from scratch

            eventHeader.setText("Add an event");
            populateEventData(eventDocumentId);
            // The delete button should just exit the activity
            eventDeleteButton.setText("Cancel");
            eventDeleteButton.setOnClickListener(view -> onSupportNavigateUp());
            // The done button should add the event to the DB
            eventDoneButton.setOnClickListener(view -> pushEventData());

        } else {
            // The event is being modified

            eventHeader.setText("Modify event");
            populateEventData(eventDocumentId);
            // The delete button should delete the event
            eventDeleteButton.setOnClickListener(view -> {
                deleteEvent(eventDocumentId);
                onSupportNavigateUp();
            });
            // The done button should update the event's information
            eventDoneButton.setOnClickListener(view -> pushEventData());
        }
    }

    private boolean validateEventData() {
        // The dates should be in the future if event is being created from scratch
        boolean requireToBeInFuture = eventDocumentId == null;
        boolean status = ValidationUtil.validateRegex(this, eventTitleEditText.getText().toString().trim(), "event name", "^.*[a-zA-Z]+.*$", "at least one letter")
                && ValidationUtil.validateRegex(this, postalCodeEditText.getText().toString().trim().toUpperCase(), "zip code", "[ABCEGHJKLMNPRSTVXY][0-9][ABCEGHJKLMNPRSTVWXYZ] ?[0-9][ABCEGHJKLMNPRSTVWXYZ][0-9]", "a valid Canadian postal code")
                && ValidationUtil.validateFloat(this, registrationFeesEditText.getText().toString().trim(), "registration fees")
                && Float.parseFloat(registrationFeesEditText.getText().toString().trim()) >= 0
                && ValidationUtil.validateInt(this, participantLimitEditText.getText().toString().trim(), "participant limit")
                && Integer.parseInt(participantLimitEditText.getText().toString().trim()) >= 1
                && ValidationUtil.validateEmpty(this, descriptionEditText.getText().toString().trim(), "event description")
                && isValidDate(dateEditText.getText().toString().trim(), "event date", requireToBeInFuture)
                && isValidTime(startTimeEditText.getText().toString().trim(), "start time")
                && isValidTime(endTimeEditText.getText().toString().trim(), "end time");
        if (!status) {
            if (ValidationUtil.validateFloat(this, registrationFeesEditText.getText().toString().trim(), "registration fees")
                    && Float.parseFloat(registrationFeesEditText.getText().toString().trim()) < 0) {
                makeToast("The registration fees must be positive or 0!");
                return false;
            }
            if (ValidationUtil.validateInt(this, participantLimitEditText.getText().toString().trim(), "participant limit")
                    && Integer.parseInt(participantLimitEditText.getText().toString().trim()) < 1) {
                makeToast("The participant limit must be at least 1!");
                return false;
            }
        }
        return status;
    }

    private boolean validateRequiredFields() {
        for (EditText requiredFieldInput : requiredFieldInputMap.keySet()) {
            boolean inputStatus = true;
            String fieldName = requiredFieldInput.getHint().toString();
            String completeFieldName = "event type details " + fieldName + " field";

            if (requiredFieldInputMap.get(requiredFieldInput).equals(RequiredField.STRING_TYPE)) {
                inputStatus = ValidationUtil.validateEmpty(this, requiredFieldInput.getText().toString().trim(), completeFieldName);
            } else if (requiredFieldInputMap.get(requiredFieldInput).equals(RequiredField.INT_TYPE)) {
                inputStatus = ValidationUtil.validateInt(this, requiredFieldInput.getText().toString().trim(), completeFieldName);
            } else if (requiredFieldInputMap.get(requiredFieldInput).equals(RequiredField.FLOAT_TYPE)) {
                inputStatus = ValidationUtil.validateFloat(this, requiredFieldInput.getText().toString().trim(), completeFieldName);
            }
            if (!inputStatus) {
                return false;
            }
        }
        return true;
    }

    private void pushEventData() {
        boolean status = validateEventData() && validateRequiredFields();

        if (status) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // If the event is being added for the first time, generate a new id for it
            if (eventDocumentId == null) {
                eventDocumentId = db.collection(Event.COLLECTION_NAME).document().getId();
            }

            // Gather the datetime info
            ZonedDateTime startDateTime = dateTimeConversion(startTimeEditText.getText().toString().trim(), dateEditText.getText().toString().trim());
            ZonedDateTime endDateTime = dateTimeConversion(endTimeEditText.getText().toString().trim(), dateEditText.getText().toString().trim());
            if (!endDateTime.isAfter(startDateTime)) {
                makeToast("The end datetime must be later than the start datetime!");
                return;
            }
            // Gather the inputs from the required fields and store them in a map
            Map<String, Object> eventTypeRequiredFieldsMap = new HashMap<>();
            for (EditText requiredFieldInput : requiredFieldInputMap.keySet()) {
                String key = requiredFieldInput.getHint().toString();
                if (requiredFieldInputMap.get(requiredFieldInput).equals(RequiredField.STRING_TYPE)) {
                    eventTypeRequiredFieldsMap.put(key, requiredFieldInput.getText().toString().trim());
                } else if (requiredFieldInputMap.get(requiredFieldInput).equals(RequiredField.INT_TYPE)) {
                    eventTypeRequiredFieldsMap.put(key, Integer.valueOf(requiredFieldInput.getText().toString().trim()));
                } else if (requiredFieldInputMap.get(requiredFieldInput).equals(RequiredField.FLOAT_TYPE)) {
                    eventTypeRequiredFieldsMap.put(key, Float.valueOf(requiredFieldInput.getText().toString().trim()));
                }
            }

            // Create a map with the event's data
            Map<String, Object> eventDataMap = new HashMap<>();
            eventDataMap.put("title", eventTitleEditText.getText().toString().trim());
            eventDataMap.put("description", descriptionEditText.getText().toString().trim());
            eventDataMap.put("difficulty", getSelectedDifficulty(difficultyLevelRadioGroup.getCheckedRadioButtonId()));
            eventDataMap.put("fee", Double.parseDouble(registrationFeesEditText.getText().toString().trim()));
            eventDataMap.put("location", postalCodeEditText.getText().toString().trim().toUpperCase());
            eventDataMap.put("organizer", clubUsername);
            eventDataMap.put("participantLimit", Long.valueOf(participantLimitEditText.getText().toString().trim()));
            eventDataMap.put("startDate", startDateTime.toString());
            eventDataMap.put("endDate", endDateTime.toString());
            // Store the event type's id instead of its name
            int eventTypeIndex = eventTypeSpinner.getSelectedItemPosition();
            eventDataMap.put("eventType", eventTypeIds.get(eventTypeIndex));
            eventDataMap.put("eventTypeRequiredFields", eventTypeRequiredFieldsMap);
            eventDataMap.put("participants", eventParticipants == null ? new ArrayList<>() : eventParticipants);

            db.collection(Event.COLLECTION_NAME)
                    .document(eventDocumentId).set(eventDataMap);
            onSupportNavigateUp();
        }
    }

    private void deleteEvent(String eventDocumentId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Event.COLLECTION_NAME).document(eventDocumentId).delete();
    }

    private void populateEventData(String eventDocumentId) {
        // If the eventDocumentId is null, the event doesn't already exist.
        // Only the event types need to be fetched
        if (eventDocumentId == null) {
            fillFieldsFromDocument(null);
            return;
        }
        // Use FirebaseFirestore to fetch event information from the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Event.COLLECTION_NAME).document(eventDocumentId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Event information found, fill in the fields
                            fillFieldsFromDocument(document);
                        } else {
                            // Quit the activity if something goes wrong
                            makeToast("Event not found. Debug ID: " + eventDocumentId);
                            onSupportNavigateUp();
                        }
                    } else {
                        // Quit the activity if something goes wrong
                        makeToast("Error fetching event information");
                        onSupportNavigateUp();
                    }
                });
    }

    private void fillFieldsFromDocument(DocumentSnapshot eventDocument) {
        if (eventDocument != null) {
            // Extract data from the document and fill in the corresponding fields
            eventTitleEditText.setText(eventDocument.getString("title"));
            difficultyLevelRadioGroup.check(getRadioButtonIdFromDifficulty(eventDocument.getString("difficulty")));
            registrationFeesEditText.setText(String.valueOf(eventDocument.getDouble("fee")));
            descriptionEditText.setText(eventDocument.getString("description"));
            participantLimitEditText.setText(String.valueOf(eventDocument.getLong("participantLimit")));
            postalCodeEditText.setText(eventDocument.getString("location"));
            dateEditText.setText(extractDate(eventDocument.getString("startDate")));
            startTimeEditText.setText(extractTime(eventDocument.getString("startDate")));
            endTimeEditText.setText(extractTime(eventDocument.getString("endDate")));
            eventParticipants = (List<String>) eventDocument.get("participants");
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(EventType.COLLECTION_NAME).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                eventTypes = new ArrayList<>();
                eventTypeIds = new ArrayList<>();
                for (QueryDocumentSnapshot eventTypeDocument : task.getResult()) {
                    // Create a corresponding EventType object
                    String eventTypeLabel = eventTypeDocument.getString("label");
                    boolean eventTypeEnabled = eventTypeDocument.getBoolean("enabled");
                    EventType selectedEventType = new EventType(eventTypeLabel, eventTypeEnabled);
                    Map<String, String> requiredFields = (HashMap<String, String>) eventTypeDocument.get("requiredFields");
                    for (String requiredFieldName : requiredFields.keySet()) {
                        selectedEventType.addRequiredField(requiredFieldName, requiredFields.get(requiredFieldName));
                    }
                    if (eventDocument == null) {
                        // The event doesn't already exist, so its event type can be set to
                        // any existing event type that is ENABLED
                        if (eventTypeEnabled) {
                            eventTypes.add(selectedEventType);
                            eventTypeIds.add(eventTypeDocument.getId());
                        }
                    } else {
                        // The event already exists, so its event type is already known.
                        // We won't allow the club to change the event type of the event.
                        //
                        // The event type is allowed to be disabled.
                        if (eventTypeDocument.getId().equals(eventDocument.getString("eventType"))) {
                            eventTypes.add(selectedEventType);
                            eventTypeIds.add(eventTypeDocument.getId());
                            break;
                        }
                    }
                }
                // Populate the Spinner
                List<String> eventTypeLabelList = new ArrayList<>();
                for (EventType eventType : eventTypes) {
                    eventTypeLabelList.add(eventType.getLabel());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        EventActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        eventTypeLabelList
                );
                eventTypeSpinner.setAdapter(adapter);

                requiredFieldInputMap = new HashMap<>();
                if (eventDocument == null) {
                    // If the event is being created from scratch, then add a listener to the
                    // spinner to create inputs for each required field
                    eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            requiredFieldInputMap = new HashMap<>();
                            EventType selectedEventType = eventTypes.get(i);
                            // Clear the linear layout
                            eventTypeDetailsLinearLayout.removeAllViews();
                            for (RequiredField requiredField : selectedEventType.getRequiredFields()) {
                                EditText editText = createEditTextInput(requiredField.getName());
                                // Set the type of the editText based on how the event type defines it.
                                if (requiredField.getType().equals(RequiredField.STRING_TYPE)) {
                                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                                } else if (requiredField.getType().equals(RequiredField.INT_TYPE)) {
                                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                                } else if (requiredField.getType().equals(RequiredField.FLOAT_TYPE)) {
                                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                                }
                                // Add the editText to the linear layout ready to accept it
                                eventTypeDetailsLinearLayout.addView(editText);
                                requiredFieldInputMap.put(editText, requiredField.getType());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            makeToast("No event type was selected - something went wrong!");
                            onSupportNavigateUp();
                        }
                    });
                } else {
                    // Otherwise, just create the existing required fields and fill in their inputs
                    // based on the stored information
                    EventType selectedEventType = eventTypes.get(0);
                    // Clear the linear layout
                    eventTypeDetailsLinearLayout.removeAllViews();
                    Map<String, String> eventRequiredFields = (HashMap<String, String>) eventDocument.get("eventTypeRequiredFields");
                    for (RequiredField requiredField : selectedEventType.getRequiredFields()) {
                        EditText editText = createEditTextInput(requiredField.getName());
                        // Set the type of the editText based on how the event type defines it.
                        if (requiredField.getType().equals(RequiredField.STRING_TYPE)) {
                            editText.setInputType(InputType.TYPE_CLASS_TEXT);
                        } else if (requiredField.getType().equals(RequiredField.INT_TYPE)) {
                            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        } else if (requiredField.getType().equals(RequiredField.FLOAT_TYPE)) {
                            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        }
                        // Set the text to what was stored in the DB
                        try {
                            editText.setText(eventRequiredFields.get(requiredField.getName()));
                        } catch (Exception e) {
                            editText.setText(String.valueOf(eventRequiredFields.get(requiredField.getName())));
                        }
                        // Add the editText to the linear layout ready to accept it
                        eventTypeDetailsLinearLayout.addView(editText);
                        requiredFieldInputMap.put(editText, requiredField.getType());
                    }
                }
                eventTypeSpinner.setSelection(0);
            } else {
                // Exit the activity if something goes wrong
                makeToast("Error fetching event types");
                onSupportNavigateUp();
            }
        });
    }

    private EditText createEditTextInput(String editTextInputHint) {
        EditText editText = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, dpToPx(16));
        editText.setLayoutParams(layoutParams);
        editText.setMinHeight(dpToPx(48));
        editText.setHint(editTextInputHint);

        return editText;
    }

    private int dpToPx(int dp) {
        return (int) (dp * this.getResources().getDisplayMetrics().density);
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

    private String extractTime(String dateTimeString) {
        return dateTimeString.substring(11, 16);
    }

    private String extractDate(String dateTimeString) {
        String dateString = dateTimeString.substring(0, 10);
        dateString = replaceDashesWithSlashes(dateString);
        return dateString;
    }


    private String replaceDashesWithSlashes(String dateString) {
        return dateString.replace("-", "/");
    }

    private ZonedDateTime dateTimeConversion(String timeString, String dateString) {
        // Define the date and time formatters
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);

        // Parse the strings to LocalDate and LocalTime
        LocalDate localDate = LocalDate.parse(dateString, dateFormatter);
        LocalTime localTime = LocalTime.parse(timeString, timeFormatter);

        // Combine LocalDate and LocalTime to create ZonedDateTime
        return ZonedDateTime.of(localDate, localTime, ZoneId.systemDefault());
    }

    public boolean isValidDate(String date, String fieldName, boolean requireToBeInFuture) {
        try {
            ZonedDateTime zonedDateTime = dateTimeConversion("12:00", date);
            if (requireToBeInFuture && !zonedDateTime.isAfter(ZonedDateTime.now())) {
                makeToast("The " + fieldName + " field is invalid! Must be in future.");
                return false;
            }
            return true;
        } catch (Exception e) {
            makeToast("The " + fieldName + " field is invalid!");
            return false;
        }
    }

    public boolean isValidTime(String time, String fieldName) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
            sdf.setLenient(false);
            Date parsedTime = sdf.parse(time);
            return true;

        } catch (ParseException e) {
            makeToast("The " + fieldName + " field is invalid!");
            return false;
        }
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

    private void makeToast(String toastText) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}


