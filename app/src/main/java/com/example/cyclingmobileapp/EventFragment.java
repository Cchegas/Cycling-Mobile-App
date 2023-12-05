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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventFragment extends Fragment {

    private List<Event> events;
    private List<String> eventDocumentIds;
    private String clubAccountUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        events = new ArrayList<Event>();
        eventDocumentIds = new ArrayList<String>();
        // Inflate the layout for this fragment
        clubAccountUsername = getArguments().getString("username");
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        ListView listViewEvents = getView().findViewById(R.id.eventsListView);
        Button addEventButton = getView().findViewById(R.id.addEventButton);

        addEventButton.setOnClickListener(view12 -> {
            // ****************** Add Event Button functionality goes here *******************
            Intent eventActivityIntent = new Intent(activity, EventActivity.class);
            eventActivityIntent.putExtra("username", clubAccountUsername);
            startActivity(eventActivityIntent);
        });

        // Listen for clicks on an existing event
        listViewEvents.setOnItemClickListener((adapterView, view1, i, l) -> {
            // Navigate to the EventActivity
            String eventDocumentId = eventDocumentIds.get(i);
            Intent eventActivityIntent = new Intent(activity, EventActivity.class);
            eventActivityIntent.putExtra("username", clubAccountUsername);
            eventActivityIntent.putExtra("eventDocumentId", eventDocumentId);
            startActivity(eventActivityIntent);
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
                                eventDocumentIds.add(doc.getId());
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
