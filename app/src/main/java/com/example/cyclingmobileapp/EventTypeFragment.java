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

import com.example.cyclingmobileapp.lib.event.EventType;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventTypeFragment extends Fragment {


    private List<EventType> eventTypes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        eventTypes = new ArrayList<EventType>();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_type, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        ListView listViewEventTypes = getView().findViewById(R.id.eventTypeListView);
        Button addEventTypeButton = getView().findViewById(R.id.addEventTypeButton);

        addEventTypeButton.setOnClickListener(view1 -> {
            Intent eventTypeActivityIntent = new Intent(activity, EventTypeActivity.class);
            startActivity(eventTypeActivityIntent);
        });

        listViewEventTypes.setOnItemClickListener((adapterView, view12, i, l) -> {
            EventType eventType = eventTypes.get(i);
            Intent eventTypeActivityIntent = new Intent(activity, EventTypeActivity.class);
            eventTypeActivityIntent.putExtra("label", eventType.getLabel());
            startActivity(eventTypeActivityIntent);
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Display the event types in a ListView
        db.collection(EventType.COLLECTION_NAME).addSnapshotListener((value, error) -> {
            eventTypes.clear();

            for (QueryDocumentSnapshot doc : value) {
                EventType eventType = new EventType(doc.get("label").toString(), (boolean) doc.get("enabled"));
                // Don't display non-enabled events, for simplicity
                if (eventType.getEnabled()) {
                    eventTypes.add(eventType);
                }
            }

            EventTypeList eventTypeList = new EventTypeList(activity, eventTypes);
            listViewEventTypes.setAdapter(eventTypeList);
        });
    }
}
