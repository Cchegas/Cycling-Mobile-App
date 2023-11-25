package com.example.cyclingmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.cyclingmobileapp.lib.event.Event;
import com.example.cyclingmobileapp.lib.user.Account;
import com.example.cyclingmobileapp.lib.user.ClubAccount;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventsFragment extends Fragment {

    private List<Event> events;
    private String clubAccountUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        events = new ArrayList<Event>();
        // Inflate the layout for this fragment
        clubAccountUsername = getArguments().getString("username");
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        ListView listViewEvents = (ListView) getView().findViewById(R.id.eventsListView);
        Button addEventButton = (Button) getView().findViewById(R.id.addEventButton);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add Add Event Activity
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Display the events in a ListView
        db.collection(Account.COLLECTION_NAME).whereEqualTo("username", clubAccountUsername).addSnapshotListener((value, error) -> {
            events.clear();

            events = (ArrayList<Event>) value.getDocuments().get(0).get("events");

            EventList eventList = new EventList(activity, events);
            listViewEvents.setAdapter(eventList);
        });
    }
}
