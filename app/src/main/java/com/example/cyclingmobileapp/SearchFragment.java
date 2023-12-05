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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();

        //initializing the Search- and ListView
        searchBar = activity.findViewById(R.id.search_bar);
        itemsList = activity.findViewById(R.id.items_list);

        //need to get data from Database________________________________________________________________________
        //1. get ALL ClubAccounts from database
        //2. put these ClubAccounts into the ClubAccount[] clubAccounts array.
        //3. the clubAccounts array will be placed into an ArrayAdapter which is sent to the ListView___________
        List<String> results = new ArrayList<>();
        clubAccounts = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Account.COLLECTION_NAME).addSnapshotListener((value, error) -> {
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        if (doc.get("role") != null) {
                            if (doc.get("role").toString().equals("club") && doc.get("name") != null) {
                                String accountName = doc.get("name").toString();
                                clubAccounts.add(accountName);
                                results.add(accountName);
                            }
                        }
                    }
                }
            }
        });
        events = new ArrayList<>();
        db.collection(Event.COLLECTION_NAME).addSnapshotListener((value, error) -> {
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        if (doc.get("title") != null) {
                            String eventName = doc.get("title").toString();
                            events.add(eventName);
                            results.add(eventName);
                        }
                    }
                }
            }
        });
        eventTypes = new ArrayList<>();
        db.collection(EventType.COLLECTION_NAME).addSnapshotListener((value, error) -> {
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        if (doc.get("label") != null) {
                            String eventTypeName = doc.get("label").toString();
                            eventTypes.add(eventTypeName);
                            results.add(eventTypeName);
                        }
                    }
                }
            }
        });
        ArrayAdapter<String> searchResults = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, results);
        itemsList.setAdapter(searchResults);
        setupSearchView();
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
