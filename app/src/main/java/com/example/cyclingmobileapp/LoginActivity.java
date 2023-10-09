package com.example.cyclingmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cyclingmobileapp.lib.user.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.db = FirebaseFirestore.getInstance();
    }

    public void onLoginButtonClick(View view) {
        String username = ((EditText) findViewById(R.id.loginUsernameInput)).getText().toString();
        String password = ((EditText) findViewById(R.id.loginPasswordInput)).getText().toString();

        if (validateLogin(username, password)) {
            db.collection("users").whereEqualTo("username", username).whereEqualTo("password", password).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                        if (documentSnapshots.size() == 1) {
                            Account account = documentSnapshots.get(0).toObject(Account.class);
                            Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                            mainActivityIntent.putExtra("username", account.getUsername());
                            mainActivityIntent.putExtra("role", account.getRole());
                            startActivity(mainActivityIntent);
                            finish();
                            return;
                        }
                    }
                    makeToast("Invalid username or password! Try again.");
                }
            });
        } else {
            makeToast("The username or password is invalid!");
        }
    }

    public void onSignupButtonClick(View view){
        Intent signupActivityIntent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(signupActivityIntent);
        finish();
    }

    private boolean validateLogin(String username, String password) {
        // Possibly use regex to validate the fields for login
        // For now, just ensure that the username field doesn't have a space
        return username.indexOf(" ") == -1;
    }

    private void makeToast(String toastText) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }
}