package com.example.cyclingmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class EventTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_type);


        TextView eventTypeHeader = (TextView) findViewById(R.id.eventTypeHeader);
        Button cancelButton = (Button) findViewById(R.id.eventTypeCancelButton);
        Button doneButton = (Button) findViewById(R.id.eventTypeDoneButton);

        cancelButton.setOnClickListener(view -> {
            finishActivity();
        });
        doneButton.setOnClickListener(view -> {
            finishActivity();
        });

        String eventTypeLabel = getIntent().getExtras() != null ? getIntent().getExtras().getString("label") : null;
        if (eventTypeLabel != null){
            eventTypeHeader.setText("Modify event type");
        } else {
            eventTypeHeader.setText("Add an event type");
        }
    }

    private void finishActivity(){
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
}