package com.example.cyclingmobileapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cyclingmobileapp.lib.event.Event;
import com.example.cyclingmobileapp.lib.user.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private String username;
    private String clubUsername;
    private ListView requiredFieldListView;
    private List<Event> events;
    private List<String> eventsDocumentIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.profileActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        events = new ArrayList<>();
        eventsDocumentIds = new ArrayList<>();

        requiredFieldListView = findViewById(R.id.profileEventsListView);

        if (getIntent().getExtras() != null) {
            username = getIntent().getExtras().getString("username");
            clubUsername = getIntent().getExtras().getString("clubUsername").toLowerCase();
        }
        displayAccountInfo();
        updateEvents();
    }

    private void displayAccountInfo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TextView profileHeader = findViewById(R.id.profileHeader);
        TextView profileSocialMedia = findViewById(R.id.profileSocialMedia);
        TextView profileMainContact = findViewById(R.id.profileMainContact);
        TextView profilePhoneNumber = findViewById(R.id.profilePhoneNumber);
        String defaultValue = "None";
        db.collection(Account.COLLECTION_NAME).whereEqualTo("username", clubUsername).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getDocuments().size() > 0) {
                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                String clubName = documentSnapshot.getString("name");
                profileHeader.setText(clubName);
                if (documentSnapshot.get("profileInfo") != null) {
                    Map<String, String> profileInfo = (Map<String, String>) documentSnapshot.get("profileInfo");
                    String mainContact = profileInfo.get("mainContact");
                    String phoneNumber = profileInfo.get("phoneNumber");
                    String socialMediaLink = profileInfo.get("socialMediaLink");

                    mainContact = mainContact == null ? defaultValue : mainContact;
                    phoneNumber = phoneNumber == null ? defaultValue : phoneNumber;
                    socialMediaLink = socialMediaLink == null ? defaultValue : socialMediaLink;

                    profileSocialMedia.setText(socialMediaLink);
                    profileMainContact.setText(mainContact);
                    profilePhoneNumber.setText(phoneNumber);
                } else {
                    profileSocialMedia.setText(defaultValue);
                    profileMainContact.setText(defaultValue);
                    profilePhoneNumber.setText(defaultValue);
                }
            } else {
                makeToast("Something went wrong! CLUB = " + clubUsername);
            }
        });
    }

    private void updateEvents(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Event.COLLECTION_NAME).whereEqualTo("organizer", clubUsername).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    events.clear();
                    eventsDocumentIds.clear();
                    for (DocumentSnapshot document : documents) {
                        String title = document.getString("title");

                        Event event = new Event(title);
                        events.add(event);
                        eventsDocumentIds.add(document.getId());
                    }
                    updateEventListView();
                } else {
                    makeToast("Something went wrong when fetching events!");
                    onSupportNavigateUp();
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

    private boolean canRegisterForEvent(String datetime){
        ZonedDateTime dateTime = parseDatetime(datetime);
        if (dateTime == null){
            return  false;
        }
        return !ZonedDateTime.now().isAfter(dateTime);
    }

    private void updateEventListView() {
        EventList requiredFieldListAdapter = new EventList(this, events);
        requiredFieldListView.setAdapter(requiredFieldListAdapter);
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