package com.example.cyclingmobileapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.cyclingmobileapp.lib.event.Event;
import com.example.cyclingmobileapp.lib.event.EventType;
import com.example.cyclingmobileapp.lib.user.Account;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private SearchView searchBar;
    private ListView itemsList;
    private List<String> clubAccounts;
    private List<String> events;
    private List<String> eventTypes;

    // Store the display name of each result, and the type of each result
    private List<String> results;
    private List<String> resultTypes;

    private static final String EVENT_ID = "EVENT";
    private static final String EVENT_TYPE_ID = "EVENT_TYPE";
    private static final String CLUB_ACCOUNT_ID = "CLUB_ACCOUNT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();

        // Initializing the Search - and ListView
        searchBar = activity.findViewById(R.id.search_bar);
        itemsList = activity.findViewById(R.id.items_list);

        results = new ArrayList<>();
        resultTypes = new ArrayList<>();

        clubAccounts = new ArrayList<>();
        events = new ArrayList<>();
        eventTypes = new ArrayList<>();

        // Need to get data from Database________________________________________________________________________
        // 1. get ALL ClubAccounts from database
        // 2. put these ClubAccounts into the ClubAccount[] clubAccounts array.
        // 3. the clubAccounts array will be placed into an ArrayAdapter which is sent to the ListView___________
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Account.COLLECTION_NAME).addSnapshotListener((value, error) -> {
            if (value != null) {
                clubAccounts.clear();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        if (doc.get("role") != null) {
                            if (doc.getString("role").equals("club") && doc.get("name") != null) {
                                String accountName = doc.getString("name");
                                clubAccounts.add(accountName);
                            }
                        }
                    }
                }
                updateResults();
            }
        });
        db.collection(Event.COLLECTION_NAME).addSnapshotListener((value, error) -> {
            if (value != null) {
                events.clear();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        if (doc.get("title") != null) {
                            String eventName = doc.get("title").toString();
                            events.add(eventName);
                        }
                    }
                }
                updateResults();
            }
        });
        db.collection(EventType.COLLECTION_NAME).addSnapshotListener((value, error) -> {
            if (value != null) {
                eventTypes.clear();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        if (doc.get("label") != null) {
                            String eventTypeName = doc.get("label").toString();
                            eventTypes.add(eventTypeName);
                        }
                    }
                }
                updateResults();
            }
        });
        ArrayAdapter<String> searchResults = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, results);
        itemsList.setAdapter(searchResults);
        setupSearchView();
    }

    private void updateResults(){
        results.clear();
        resultTypes.clear();
        for (String clubAccount: clubAccounts){
            results.add(clubAccount);
            resultTypes.add(CLUB_ACCOUNT_ID);
        }
        for (String event: events){
            results.add(event);
            resultTypes.add(EVENT_ID);
        }
        for (String eventType: eventTypes){
            results.add(eventType);
            resultTypes.add(EVENT_TYPE_ID);
        }
    }

    private void filter(String filterName) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) itemsList.getAdapter();
        adapter.getFilter().filter(filterName);
    }

    private void setupSearchView() {
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String filterText) {
                filter(filterText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

}
