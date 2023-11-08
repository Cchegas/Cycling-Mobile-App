package com.example.cyclingmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class EventTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_type);

        String eventTypeLabel = getIntent().getExtras() != null ? getIntent().getExtras().getString("label") : null;
        TextView eventTypeHeader = (TextView) findViewById(R.id.eventTypeHeader);
        if (eventTypeLabel != null){
            eventTypeHeader.setText("Modify event type");
        } else {
            eventTypeHeader.setText("Add an event type");
        }
    }
}