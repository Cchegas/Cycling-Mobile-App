package com.example.cyclingmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cyclingmobileapp.lib.event.Event;
import com.example.cyclingmobileapp.lib.user.Account;
import com.example.cyclingmobileapp.lib.user.Review;
import com.example.cyclingmobileapp.lib.utils.ValidationUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private String username;
    private String clubUsername;
    private ListView requiredFieldListView;
    private List<Event> events;
    private List<String> eventDocumentIds;
    EditText profileReviewRatingInput, profileReviewCommentInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.profileActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null) {
            username = getIntent().getExtras().getString("username");
            clubUsername = getIntent().getExtras().getString("clubUsername").toLowerCase();
        }

        profileReviewRatingInput = findViewById(R.id.profileReviewRatingInput);
        profileReviewCommentInput = findViewById(R.id.profileReviewCommentInput);
        Button profileReviewUpdateButton = findViewById(R.id.profileReviewUpdateButton);
        profileReviewUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateReview();
            }
        });

        events = new ArrayList<>();
        eventDocumentIds = new ArrayList<>();

        requiredFieldListView = findViewById(R.id.profileEventsListView);
        requiredFieldListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent eventSignupActivityIntent = new Intent(this, EventSignupActivity.class);
            eventSignupActivityIntent.putExtra("username", username);
            eventSignupActivityIntent.putExtra("eventDocumentId", eventDocumentIds.get(i));
            startActivity(eventSignupActivityIntent);
        });
        displayReviewInfo();
        displayAccountInfo();
        getEvents();
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

    private boolean validateReviewInfo(){
        // Only validate the rating. We allow comments to possibly be empty (similar to how Google
        // reviews may do things to my understanding).
        return ValidationUtil.validateRegex(this, profileReviewRatingInput.getText().toString().trim(), "review rating", "^[1-5]$", "a number from 1-5 (inclusive)");
    }

    private void updateReview(){
        if (!validateReviewInfo()){
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Review review = new Review(username, clubUsername, Integer.parseInt(profileReviewRatingInput.getText().toString().trim()), profileReviewCommentInput.getText().toString().trim());
        getReviewDocumentsQuery().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (task.getResult().getDocuments().size() > 0){
                    // Update the existing review
                    DocumentSnapshot reviewDoc =  task.getResult().getDocuments().get(0);
                    db.collection(Review.COLLECTION_NAME).document(reviewDoc.getId()).set(review);
                } else {
                    // Create a new review
                    db.collection(Review.COLLECTION_NAME).document().set(review);
                }
                makeToast("Updated review!");
            } else {
                makeToast("Failed to update review!");
            }
        });
    }

    private void displayReviewInfo(){
        getReviewDocumentsQuery().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                if (documents.size() > 0){
                    DocumentSnapshot reviewDoc = documents.get(0);
                    Review review = reviewDoc.toObject(Review.class);
                    profileReviewRatingInput.setText(String.valueOf(review.getRating()));
                    profileReviewCommentInput.setText(review.getComment());
                }
            } else {
                makeToast("Failed to fetch review data!");
            }
        });
    }

    private Task<QuerySnapshot> getReviewDocumentsQuery(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(Review.COLLECTION_NAME).whereEqualTo("reviewer", username).whereEqualTo("reviewee", clubUsername).get();
    }

    private void getEvents(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Event.COLLECTION_NAME).whereEqualTo("organizer", clubUsername).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    events.clear();
                    eventDocumentIds.clear();
                    for (DocumentSnapshot document : documents) {
                        String title = document.getString("title");

                        Event event = new Event(title);
                        events.add(event);
                        eventDocumentIds.add(document.getId());
                    }
                    updateEventListView();
                } else {
                    makeToast("Something went wrong when fetching events!");
                    onSupportNavigateUp();
                }
            }
        });
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