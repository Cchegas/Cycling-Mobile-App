package com.example.cyclingmobileapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
    private int selectedDeletionIndex;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accounts = new ArrayList<Account>();
        selectedDeletionIndex = -1;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        ListView listViewAccounts = (ListView) getView().findViewById(R.id.accountListView);

        // Setup deletion confirmation dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_delete_dialog, null);
        dialogBuilder.setView(dialogView);
        AlertDialog deletionDialog = dialogBuilder.create();

        TextView deleteDialogContent = (TextView) dialogView.findViewById(R.id.deleteDialogContent);
        Button deleteDialogCancelButton = (Button) dialogView.findViewById(R.id.deleteDialogCancelButton);
        Button deleteDialogDeleteButton = (Button) dialogView.findViewById(R.id.deleteDialogDeleteButton);

        deleteDialogCancelButton.setOnClickListener(view1 -> deletionDialog.dismiss());
        // Remove the account at the last selected index by the user
        deleteDialogDeleteButton.setOnClickListener(view13 -> {
            if (selectedDeletionIndex >= 0){
                Account account = accounts.get(selectedDeletionIndex);
                accounts.remove(selectedDeletionIndex);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection(Account.COLLECTION_NAME).document(account.getUsername()).delete();
                updateAccountListView();
            }
            deletionDialog.dismiss();
        });
        listViewAccounts.setOnItemLongClickListener((adapterView, view12, i, l) -> {
            Account account = accounts.get(i);
            String confirmationContent = "Delete " + account.getRole() + " user " + account.getUsername();
            deleteDialogContent.setText(confirmationContent);
            deletionDialog.show();
            // Update the selected index based on which account was clicked (in order)
            selectedDeletionIndex = i;
            return  true;
        });

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
            updateAccountListView();
        });
    }

    private void updateAccountListView() {
        ListView listViewAccounts = (ListView) getView().findViewById(R.id.accountListView);
        AccountList accountList = new AccountList(getActivity(), accounts);
        listViewAccounts.setAdapter(accountList);
    }
}
