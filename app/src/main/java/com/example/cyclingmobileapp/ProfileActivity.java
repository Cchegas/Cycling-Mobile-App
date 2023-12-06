package com.example.cyclingmobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyclingmobileapp.lib.user.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private String username;
    private String clubUsername;
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
        displayAccountInfo();
    }

    private void displayAccountInfo(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TextView profileHeader = findViewById(R.id.profileHeader);
        TextView profileSocialMedia = findViewById(R.id.profileSocialMedia);
        TextView  profileMainContact = findViewById(R.id.profileMainContact);
        TextView profilePhoneNumber = findViewById(R.id.profilePhoneNumber);
        String defaultValue = "None";
        db.collection(Account.COLLECTION_NAME).whereEqualTo("username", clubUsername).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getDocuments().size() > 0){
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

                    profileSocialMedia.setText(mainContact);
                    profileMainContact.setText(phoneNumber);
                    profilePhoneNumber.setText(socialMediaLink);
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}