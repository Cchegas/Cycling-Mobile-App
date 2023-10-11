package com.example.cyclingmobileapp;

import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent ;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String username = getIntent().getExtras().getString("username");
        String name = getIntent().getExtras().getString("name");
        String role = getIntent().getExtras().getString("role");

        TextView greetingText = (TextView) findViewById(R.id.greetingText);
        String greeting = "Hello " + name + "! You are logged in as: " + role + ".";
        greetingText.setText(greeting);

        TextView usernameText = (TextView) findViewById(R.id.usernameText);
        usernameText.setText("Username: " + username);
    }
    public void SignOut(View view) {
           startActivity(new Intent(MainActivity.this, LoginActivity.class)) ;
    }
}
