package com.example.cyclingmobileapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cyclingmobileapp.lib.user.Account;

import java.util.List;

public class AccountList extends ArrayAdapter<Account> {
    private final Activity context;
    List<Account> accounts;

    public AccountList(Activity context, List<Account> accounts) {
        super(context, R.layout.layout_account_list, accounts);
        this.context = context;
        this.accounts = accounts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_account_list, null, true);

        TextView accountUsername = listViewItem.findViewById(R.id.accountUsername);
        TextView accountRole = listViewItem.findViewById(R.id.accountRole);

        Account account = accounts.get(position);
        accountUsername.setText(account.getUsername());

        String role = account.getRole();
        // Capitalize the first letter of the role to display it more formally
        String displayRole = role.substring(0, 1).toUpperCase() + role.substring(1) + " Account";
        accountRole.setText(displayRole);

        return listViewItem;
    }
}
