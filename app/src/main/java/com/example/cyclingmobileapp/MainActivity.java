package com.example.cyclingmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String username = getIntent().getExtras().getString("username");
        String role = getIntent().getExtras().getString("role");

        TextView greetingText = (TextView) findViewById(R.id.greetingText);
        String greeting = "Hello " + username + "! You are logged in as: " + role + ".";
        greetingText.setText(greeting);
    }
}