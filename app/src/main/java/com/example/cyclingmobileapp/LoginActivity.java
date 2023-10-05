package com.example.cyclingmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginButtonClick(View view) {
        String username = ((EditText) findViewById(R.id.loginUsernameInput)).getText().toString();
        String password = ((EditText) findViewById(R.id.loginPasswordInput)).getText().toString();

        if (!validateLogin(username, password)) {
            Toast.makeText(this, "Invalid username or password! Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainActivityIntent.putExtra("username", username);
        mainActivityIntent.putExtra("role", "admin");
        startActivity(mainActivityIntent);
        finish();
    }

    private boolean validateLogin(String username, String password) {
        return username.equals("admin");
    }
}