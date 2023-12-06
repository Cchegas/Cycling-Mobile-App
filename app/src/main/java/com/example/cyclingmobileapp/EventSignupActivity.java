package com.example.cyclingmobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyclingmobileapp.lib.event.Event;
import com.example.cyclingmobileapp.lib.event.EventType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.ZonedDateTime;
import java.util.List;

public class EventSignupActivity extends AppCompatActivity {

    private String username;
    private String eventDocumentId;
    private String startDate;

    boolean alreadyRegistered;
    Button signupButton;

    private final String registerText = "Sign up";
    private final String unRegisterText = "Un-register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_signup);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.eventSignupToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null) {
            username = getIntent().getExtras().getString("username");
            eventDocumentId = getIntent().getExtras().getString("eventDocumentId");
        }

        signupButton = findViewById(R.id.eventSignupButton);
        alreadyRegistered = false;
        signupButton.setText(registerText);
        signupButton.setOnClickListener(view -> {
            if (!canRegisterForEvent(startDate)){
                makeToast("This event is closed for signups!");
            }
            else {
                if (!alreadyRegistered){
                    setRegistration(true);
                } else {
                    setRegistration(false);
                }
            }
        });

        fillInFields(eventDocumentId);
    }
    private void fillInFields(String eventDocumentId){
        TextView eventTitleField = findViewById(R.id.eventSignupEventTitleField);
        TextView eventOrganizerField = findViewById(R.id.eventSignupEventOrganizerField);
        TextView eventLocationField = findViewById(R.id.eventSignupEventLocationField);
        TextView eventStartDateField = findViewById(R.id.eventSignupStartDateField);
        TextView eventEndDateField = findViewById(R.id.eventSignupEndDateField);
        TextView eventDifficultyField = findViewById(R.id.eventSignupDifficultyField);
        TextView eventFeeField = findViewById(R.id.eventSignupFeeField);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Event.COLLECTION_NAME).document(eventDocumentId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                eventTitleField.setText(document.getString("title"));
                eventOrganizerField.setText(document.getString("organizer"));
                eventLocationField.setText(document.getString("location"));
                eventDifficultyField.setText(document.getString("difficulty"));
                eventFeeField.setText(document.get("fee").toString());

                startDate = document.getString("startDate").split("\\[")[0];
                String endDate = document.getString("endDate").split("\\[")[0];
                eventStartDateField.setText(startDate);
                eventEndDateField.setText(endDate);

                List<String> participantList = (List<String>) document.get("participants");
                alreadyRegistered = participantList.contains(username);
                if (alreadyRegistered){
                    signupButton.setText(unRegisterText);
                } else {
                    signupButton.setText(registerText);
                }

                fillInEventType(document.getString("eventType"));
            } else {
                makeToast("Something went wrong!");
                onSupportNavigateUp();
            }
        });
    }

    private void fillInEventType(String eventTypeDocumentId){
        TextView eventTypeField = findViewById(R.id.eventSignupEventTypeField);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(EventType.COLLECTION_NAME).document(eventTypeDocumentId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                eventTypeField.setText(document.getString("label"));
            } else {
                makeToast("Something went wrong!");
                onSupportNavigateUp();
            }
        });
    }

    private void setRegistration(boolean register){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Event.COLLECTION_NAME).document(eventDocumentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    List<String> particpantList = (List<String>) document.get("participants");
                    if (register){
                        if (!particpantList.contains(username)){
                            particpantList.add(username);
                        }
                    } else {
                        if (particpantList.contains(username)){
                            particpantList.remove(username);
                        }
                    }
                    db.collection(Event.COLLECTION_NAME).document(eventDocumentId).update("participants", particpantList);

                    alreadyRegistered = register;
                    if (alreadyRegistered){
                        signupButton.setText(unRegisterText);
                    } else {
                        signupButton.setText(registerText);
                    }

                    makeToast("Success!");
                    onSupportNavigateUp();
                } else {
                    makeToast("Failed to change registration for event!");
                }
            }
        });
    }

    private ZonedDateTime parseDatetime(String datetime){
        String datetimeWithoutTimezoneName = datetime.split("\\[")[0];
        try {
            return ZonedDateTime.parse(datetimeWithoutTimezoneName);
        } catch (Exception e){
            return null;
        }
    }

    private boolean canRegisterForEvent(String startDatetime){
        ZonedDateTime dateTime = parseDatetime(startDatetime);
        if (dateTime == null){
            return  false;
        }
        return !ZonedDateTime.now().isAfter(dateTime);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}