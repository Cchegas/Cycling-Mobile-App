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

public class EventSignupActivity extends AppCompatActivity {

    private String username;
    private String eventDocumentId;
    private String startDate;

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

        Button signupButton = findViewById(R.id.eventSignupButton);
        signupButton.setOnClickListener(view -> {
            if (canRegisterForEvent(startDate)){
                makeToast("Registered!");
                onSupportNavigateUp();
            } else {
                makeToast("This event is closed for signups!");
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