package com.example.cyclingmobileapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class EventActivity extends AppCompatActivity {
    /*Needs:
    * What event type?
    * Depending on the event type selected...
    * -mandatory editing of RequiredFields
    * -Optional fields can be left blank
    *
    * So...
    * Need a list of event types --> EventTypeList
    * Display as a list of options
    * THIS CAN BE DONE ON A FRAGMENT
    *  */


    EventTypeList eventTypes ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

}
