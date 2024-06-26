package com.example.cyclingmobileapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.cyclingmobileapp.lib.event.Event;
import com.example.cyclingmobileapp.lib.event.EventType;
import com.example.cyclingmobileapp.lib.user.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {
    private static final String EVENT_ID = "Event";
    private static final String EVENT_TYPE_ID = "Event Type";
    private static final String CLUB_ACCOUNT_ID = "Club";
    private SearchView searchBar;
    private ListView itemsList;
    private List<Pair<String, String>> clubAccounts;

    private Map<String, String> clubAccountUsernameMap;
    private Map<Integer, String> clubAccountUsernames;
    private List<Pair<String, String>> events;
    private Map<Integer, String> eventOrganizers;
    private List<Pair<String, String>> eventTypes;
    private Map<Integer, String> eventTypeDocumentIds;
    // Store the display name of each result, and the type of each result
    private List<String> results;
    private List<String> resultTypes;

    private String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        username = getArguments().getString("username");
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
        eventOrganizers = new HashMap<>();
        clubAccountUsernames = new HashMap<>();
        eventTypeDocumentIds = new HashMap<>();
        clubAccountUsernameMap = new HashMap<>();

        // Need to get data from Database________________________________________________________________________
        // 1. get ALL ClubAccounts from database
        // 2. put these ClubAccounts into the ClubAccount[] clubAccounts array.
        // 3. the clubAccounts array will be placed into an ArrayAdapter which is sent to the ListView___________
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Account.COLLECTION_NAME).addSnapshotListener((value, error) -> {
            if (value != null) {
                clubAccounts.clear();
                clubAccountUsernameMap.clear();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        if (doc.get("role") != null) {
                            if (doc.getString("role").equals("club") && doc.get("name") != null) {
                                String accountName = doc.getString("name");
                                String accountUsername = doc.getString("username");
                                Pair<String, String> pair = new Pair<>(accountName, accountUsername);
                                clubAccounts.add(pair);
                                clubAccountUsernameMap.put(doc.getString("username"), doc.getString("name"));
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
                            String eventOrganizer = doc.get("organizer").toString();
                            Pair<String, String> pair = new Pair<>(eventName, eventOrganizer);
                            events.add(pair);
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
                            Pair<String, String> pair = new Pair<>(eventTypeName, doc.getId());
                            eventTypes.add(pair);
                        }
                    }
                }
                updateResults();
            }
        });
        createAndSetAdapter();
        setupSearchView();
        itemsList.setOnItemClickListener((adapterView, view1, i, l) -> {
            String clickedType = resultTypes.get(i);
            if (clickedType.equals(EVENT_ID) || clickedType.equals(CLUB_ACCOUNT_ID)){
                String clubUsername = "";
                if (clickedType.equals(CLUB_ACCOUNT_ID)){
                    clubUsername = clubAccountUsernames.get(i);
                } else {
                    clubUsername = eventOrganizers.get(i);
                }

                Intent profileActivityIntent = new Intent(activity, ProfileActivity.class);
                profileActivityIntent.putExtra("username", username);
                profileActivityIntent.putExtra("clubUsername", clubUsername);
                startActivity(profileActivityIntent);
            } else if (clickedType.equals(EVENT_TYPE_ID)) {
                List<String> relevantClubAccountNames = new ArrayList<>();
                List<String> relevantClubAccountUsernames = new ArrayList<>();

                String eventTypeDocumentId = eventTypeDocumentIds.get(i);
                db.collection(Event.COLLECTION_NAME).whereEqualTo("eventType", eventTypeDocumentId).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        for (DocumentSnapshot document : documentSnapshots){
                            String clubUsername  = document.getString("organizer");
                            String clubName = clubAccountUsernameMap.get(clubUsername);
                            relevantClubAccountUsernames.add(clubUsername);
                            relevantClubAccountNames.add(clubName);
                        }
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.layout_club_list_dialog, null);
                        dialogBuilder.setView(dialogView);
                        ListView eventTypeClubList = dialogView.findViewById(R.id.eventTypeClubList);
                        ArrayAdapter<String> eventTypeClubListAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, relevantClubAccountNames);
                        eventTypeClubList.setAdapter(eventTypeClubListAdapter);
                        eventTypeClubList.setOnItemClickListener((adapterView1, view2, i1, l1) -> {
                            Intent profileActivityIntent = new Intent(activity, ProfileActivity.class);
                            profileActivityIntent.putExtra("username", username);
                            profileActivityIntent.putExtra("clubUsername", relevantClubAccountUsernames.get(i1));
                            startActivity(profileActivityIntent);
                        });
                        AlertDialog dialog = dialogBuilder.create();
                        dialog.show();
                    } else {
                        makeToast("Failed to fetch clubs matching this event type");
                    }
                });
            }
        });
    }

    private void createAndSetAdapter() {
        FragmentActivity activity = getActivity();
        ArrayAdapter<String> testAdapter = new SearchList(activity, android.R.layout.simple_list_item_2, android.R.id.text1, results, resultTypes);
        itemsList.setAdapter(testAdapter);
    }

    private void updateResults() {
        results.clear();
        resultTypes.clear();
        eventOrganizers.clear();
        clubAccountUsernames.clear();
        eventTypeDocumentIds.clear();
        for (Pair<String, String> pair : clubAccounts) {
            results.add(pair.first);
            resultTypes.add(CLUB_ACCOUNT_ID);
            clubAccountUsernames.put(results.size() - 1, pair.second);
        }
        for (Pair<String, String> pair : events) {
            results.add(pair.first);
            resultTypes.add(EVENT_ID);
            eventOrganizers.put(results.size() - 1, pair.second);
        }
        for (Pair<String, String> pair : eventTypes) {
            results.add(pair.first);
            resultTypes.add(EVENT_TYPE_ID);
            eventTypeDocumentIds.put(results.size() - 1, pair.second);
        }
        createAndSetAdapter();
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

    private void makeToast(String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}
