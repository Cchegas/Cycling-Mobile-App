package com.example.cyclingmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.cyclingmobileapp.lib.event.EventType;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventTypeFragment extends Fragment {


    private List<EventType> eventTypes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        eventTypes = new ArrayList<EventType>();
        // Inflate the layout for this fragment
        View eventTypeFragment = inflater.inflate(R.layout.fragment_event_type, container, false);
        return eventTypeFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        ListView listViewEventTypes = (ListView) getView().findViewById(R.id.eventTypeListView);
        Button addEventTypeButton = (Button) getView().findViewById(R.id.addEventTypeButton);

        addEventTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventTypeActivityIntent = new Intent(activity, EventTypeActivity.class);
                startActivity(eventTypeActivityIntent);
            }
        });

        listViewEventTypes.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventType eventType = eventTypes.get(i);
                Intent eventTypeActivityIntent = new Intent(activity, EventTypeActivity.class);
                eventTypeActivityIntent.putExtra("label", eventType.getLabel());
                startActivity(eventTypeActivityIntent);
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Display the event types in a ListView
        db.collection(EventType.COLLECTION_NAME).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                eventTypes.clear();

                for (QueryDocumentSnapshot doc : value) {
                    EventType eventType = new EventType(doc.get("label").toString());
                    eventTypes.add(eventType);
                }

                EventTypeList eventTypeList = new EventTypeList(activity, eventTypes);
                listViewEventTypes.setAdapter(eventTypeList);
            }
        });
    }
}
