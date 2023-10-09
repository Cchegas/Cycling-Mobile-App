package com.example.cyclingmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cyclingmobileapp.lib.user.Account;
import com.example.cyclingmobileapp.lib.user.ClubAccount;
import com.example.cyclingmobileapp.lib.user.ParticipantAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        inflateParticipantAccountInputViews(null);
    }

    public void onSignupButtonClick(View view) {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.signupAccountTypeRadioGroup);
        int checkedButtonId = radioGroup.getCheckedRadioButtonId();
        String email = ((EditText) findViewById(R.id.signupEmailInput)).getText().toString();
        String username = ((EditText) findViewById(R.id.signupUsernameInput)).getText().toString();
        String password = ((EditText) findViewById(R.id.signupPasswordInput)).getText().toString();

        if (checkedButtonId == R.id.signupAccountTypeParticipantRadio){
            String fName = ((EditText) findViewById(R.id.signup_first_name_input)).getText().toString();
            String lName = ((EditText) findViewById(R.id.signup_last_name_input)).getText().toString();

            if (!validateSignupInfo(fName, lName, username, email, password)){
                makeToast("Invalid info! Try again.");
                return;
            }

            ParticipantAccount participantAccount = new ParticipantAccount(username, email, password, fName, lName);
            addAccount(participantAccount);
        } else if (checkedButtonId == R.id.signupAccountTypeClubRadio){
            String clubName = ((EditText) findViewById(R.id.signup_club_name_input)).getText().toString();

            if (!validateSignupInfo(clubName, username, email, password)){
                makeToast("Invalid info! Try again.");
                return;
            }
            ClubAccount clubAccount = new ClubAccount(username, email, password, clubName);
            addAccount(clubAccount);
        }
    }

    private void addAccount(Account account){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("username", account.getUsername()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                    if (documentSnapshots.size() != 0){
                        makeToast("This username is already taken!");
                        return;
                    }
                    db.collection("users").add(account).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                makeToast("Success!");
                                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                                mainActivityIntent.putExtra("username", account.getUsername());
                                mainActivityIntent.putExtra("role", account.getRole());
                                String name;
                                if (account.getRole().equals("participant")){
                                    ParticipantAccount participantAccount = (ParticipantAccount) account;
                                    name = participantAccount.getFirstName();
                                } else if (account.getRole().equals("club")) {
                                    ClubAccount clubAccount = (ClubAccount) account;
                                    name = clubAccount.getName();
                                }else {
                                    makeToast("Something unexpected occurred. Try again.");
                                    return;
                                }
                                mainActivityIntent.putExtra("name", name);
                                startActivity(mainActivityIntent);
                                finish();
                                return;
                            }
                            makeToast("Something went wrong adding your account. Try again.");
                        }
                    });
                    return;
                }
                makeToast("Something went wrong. Try again.");
            }
        });
    }

    private void makeToast(String toastText) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }

    private boolean validateSignupInfo(String fName, String lName, String username, String email, String password){
        return username.indexOf(" ") == -1;
    }

    private boolean validateSignupInfo(String clubName, String username, String email, String password){
        return username.indexOf(" ") == -1;
    }

    public void onLoginButtonClick(View view) {
        Intent loginActivityIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivityIntent);
        finish();
    }

    public void inflateParticipantAccountInputViews(View view) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.signupVariableInputLayout);
        layout.removeAllViews();

        EditText fNameInput = createEditTextInput("First name", R.id.signup_first_name_input);
        EditText lNameInput = createEditTextInput("Last name", R.id.signup_last_name_input);

        layout.addView(fNameInput);
        layout.addView(lNameInput);
    }

    public void inflateClubAccountInputViews(View view) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.signupVariableInputLayout);
        layout.removeAllViews();

        EditText clubNameInput = createEditTextInput("Club name", R.id.signup_club_name_input);

        layout.addView(clubNameInput);
    }

    private EditText createEditTextInput(String editTextInputHint, int id) {
        EditText editText = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, dpToPx(16));
        editText.setLayoutParams(layoutParams);
        editText.setMinHeight(dpToPx(48));
        editText.setHint(editTextInputHint);
        editText.setId(id);

        return editText;
    }

    private int dpToPx(int dp) {
        return (int) (dp * this.getResources().getDisplayMetrics().density);
    }
}