package com.example.cyclingmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.cyclingmobileapp.lib.event.Event;
import com.example.cyclingmobileapp.lib.event.EventType;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

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
        ListView listViewEvents = getView().findViewById(R.id.eventsListView);
        Button addEventButton = getView().findViewById(R.id.addEventButton);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ****************** Add Event Button functionality goes here *******************
                Intent eventCreationActivityIntent = new Intent(activity, EventCreationActivity.class);
                eventCreationActivityIntent.putExtra("username", clubAccountUsername);
                startActivity(eventCreationActivityIntent);
            }
        });

        listViewEvents.setOnItemClickListener((adapterView, view1, i, l) -> {
            Event event = events.get(i);
            Intent eventCreationActivityIntent = new Intent(activity, EventCreationActivity.class);
            eventCreationActivityIntent.putExtra("username", clubAccountUsername);
            eventCreationActivityIntent.putExtra("title", event.getTitle());
            startActivity(eventCreationActivityIntent);
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Display the events in a ListView
        db.collection(Event.COLLECTION_NAME).addSnapshotListener((value, error) -> {
            events.clear();
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        if (doc.get("organizer") != null && doc.get("title") != null) {
                            if (doc.get("organizer").toString().equals(clubAccountUsername)) {
                                Event event = new Event(doc.get("title").toString());
                                events.add(event);
                            }
                        }
                    }
                }
            }
            EventList eventList = new EventList(activity, events);
            listViewEvents.setAdapter(eventList);
        });
    }
}
