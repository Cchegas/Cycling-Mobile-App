package com.example.cyclingmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cyclingmobileapp.lib.event.RequiredField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventTypeActivity extends AppCompatActivity {

    private List<RequiredField> requiredFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_type);

        requiredFields = new ArrayList<RequiredField>();

        TextView eventTypeHeader = (TextView) findViewById(R.id.eventTypeHeader);
        Button addFieldButton = (Button) findViewById(R.id.eventTypeAddFieldButton);
        Button cancelButton = (Button) findViewById(R.id.eventTypeCancelButton);
        Button doneButton = (Button) findViewById(R.id.eventTypeDoneButton);

        addFieldButton.setOnClickListener(view -> {

        });
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