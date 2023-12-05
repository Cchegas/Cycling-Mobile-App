package com.example.cyclingmobileapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cyclingmobileapp.lib.user.Account;
import com.example.cyclingmobileapp.lib.utils.ValidationUtil;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileUsername;
    private EditText profileSocialMediaInput, profileMainContactInput, profilePhoneNumberInput;
    private Button updateProfileButton;
    private String username;
    private boolean alreadyValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        profileUsername = findViewById(R.id.profileUsername);
        profileSocialMediaInput = findViewById(R.id.profileSocialMediaInput);
        profileMainContactInput = findViewById(R.id.profileMainContactInput);
        profilePhoneNumberInput = findViewById(R.id.profilePhoneNumberInput);
        updateProfileButton = findViewById(R.id.updateProfileButton);

        updateProfileButton.setOnClickListener(view -> {
            onUpdateProfileButtonClick();
        });

        username = "";
        if (getIntent().getExtras() != null) {
            username = getIntent().getExtras().getString("username");
        }
        profileUsername.setText(username);
        attemptToPopulateFields(username);
    }

    private void attemptToPopulateFields(String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Account.COLLECTION_NAME).document(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.get("profileInfo") != null) {
                    Map<String, String> profileInfo = (HashMap) documentSnapshot.get("profileInfo");
                    profileSocialMediaInput.setText(profileInfo.get("socialMediaLink"));
                    profileMainContactInput.setText(profileInfo.get("mainContact"));
                    profilePhoneNumberInput.setText(profileInfo.get("phoneNumber"));
                    alreadyValid = true;
                }
            } else {
                makeToast("Failed to retrieve profile information!");
                completeActivity();
            }
        });
    }

    private boolean onUpdateProfileButtonClick() {
        String profileSocialMediaInputted = profileSocialMediaInput.getText().toString().trim();
        String profileMainContactInputted = profileMainContactInput.getText().toString().trim();
        String profilePhoneNumberInputted = profilePhoneNumberInput.getText().toString().trim();

        if (
                ValidationUtil.validateRegex(this, profileSocialMediaInputted, "social media link", "instagram\\.com\\/[\\w.]{3,}$", "a valid Instagram link")
                        && ValidationUtil.validateRegex(this, profilePhoneNumberInputted, "phone number", "^\\d{10}$", "a 10 digit phone number with no spaces")
        ) {
            if (!profileMainContactInputted.equals("") && !ValidationUtil.validateRegex(this, profileMainContactInputted, "main contact name", "^[a-zA-Z '-]{2,}$", "a name containing only letters apostrophe or dashes that's at least 2 characters long")) {
                return false;
            }
            Map<String, String> profileInfo = new HashMap<>();
            profileInfo.put("socialMediaLink", profileSocialMediaInputted);
            profileInfo.put("mainContact", profileMainContactInputted);
            profileInfo.put("phoneNumber", profilePhoneNumberInputted);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(Account.COLLECTION_NAME).document(username).update("profileInfo", profileInfo);
            completeActivity();
            return true;
        } else {
            makeToast("Both the social media link and phone number must be filled out!");
            return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (alreadyValid) {
            completeActivity();
            return true;
        }
        return onUpdateProfileButtonClick();
    }

    private void completeActivity() {
        finish();
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}