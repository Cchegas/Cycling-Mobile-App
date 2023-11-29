package com.example.cyclingmobileapp;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cyclingmobileapp.lib.event.Event;
import com.example.cyclingmobileapp.lib.event.EventType;
import com.example.cyclingmobileapp.lib.user.ClubAccount;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity{
    ClubAccount account ;
    EventTypeList offeredEventTypes ;
    Event[] scheduledEvents ;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile) ;

        db =  FirebaseFirestore.getInstance();

        List<EventType> eventTypeList = new ArrayList<>();
        offeredEventTypes = new EventTypeList(this,eventTypeList) ;


        //makes the list invisible unless the button is clicked
        Button button = findViewById(R.id.seeEventTypes);
        ListView listView = findViewById(R.id.offeredEventTypeListView);
        listView.setVisibility(View.GONE) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);
            }
        });
    }


}
