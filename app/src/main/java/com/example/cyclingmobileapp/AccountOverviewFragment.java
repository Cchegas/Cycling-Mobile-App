package com.example.cyclingmobileapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.cyclingmobileapp.lib.event.EventType;
import com.example.cyclingmobileapp.lib.user.Account;
import com.example.cyclingmobileapp.lib.user.ClubAccount;
import com.example.cyclingmobileapp.lib.user.ParticipantAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AccountOverviewFragment extends Fragment {

    private List<Account> accounts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accounts = new ArrayList<Account>();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        ListView listViewAccounts = (ListView) getView().findViewById(R.id.accountListView);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Account.COLLECTION_NAME).addSnapshotListener((value, error) -> {
            accounts.clear();

            for (QueryDocumentSnapshot doc : value){
                String role = (String) doc.get("role");
                Account account = null;

                String username = (String) doc.get("username");
                String email = (String) doc.get("email");
                String password = (String) doc.get("password");

                // Only participant and club accounts are shown
                if (role.equals(ParticipantAccount.ROLE)){
                    String firstName = (String) doc.get("firstName");
                    String lastName = (String) doc.get("lastName");
                    account = new ParticipantAccount(username,email,password,firstName,lastName);
                } else if (role.equals(ClubAccount.ROLE)){
                    String name = (String) doc.get("name");
                    account = new ClubAccount(username,email,password,name);
                } else {
                    continue;
                }
                accounts.add(account);
            }
            AccountList accountList = new AccountList(activity, accounts);
            listViewAccounts.setAdapter(accountList);
        });
    }
}
