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
import com.example.cyclingmobileapp.lib.user.ClubAccount;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventsFragment extends Fragment {

    private List<Event> events;
    private Map<String, Object> clubAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        events = new ArrayList<Event>();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        ListView listViewEventTypes = (ListView) getView().findViewById(R.id.eventsListView);
        Button addEventButton = (Button) getView().findViewById(R.id.addEventButton);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add Add Event Activity
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        if (clubAccount != null) {
//
//        }
        events.clear();

        EventList eventList = new EventList(activity, events);
        listViewEventTypes.setAdapter(eventList);
    }

    public void setClubAccount(Map<String, Object> clubAccount) {
        this.clubAccount = clubAccount;
    }
}
