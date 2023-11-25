package com.example.cyclingmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cyclingmobileapp.lib.user.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the toolbar as the app bar for the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Add the ActionBarDrawerToggle to the activity, connecting it to toggle the drawerLayout
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer_desc, R.string.close_drawer_desc);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Setup a listener to the navigation menu's items
        setNavigationDrawer();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        // Set the username as the header for the navigation menu
        if (getIntent().getExtras() != null) {
            String username = getIntent().getExtras().getString("username");
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = headerView.findViewById(R.id.nav_header_username);
            navUsername.setText(username);
        }
        // Manually select the first item in the navigation menu, and, correspondingly, show the first fragment
        navigationView.setCheckedItem(R.id.eventTypeFragmentMenuItem);
        selectFragment(new EventTypeFragment());
    }

    public void signOut() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    private void setNavigationDrawer() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.navigation_view);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        navView.setNavigationItemSelectedListener(menuItem -> {
            Fragment fragment;
            int itemId = menuItem.getItemId();
            if (itemId == R.id.eventTypeFragmentMenuItem) {
                fragment = new EventTypeFragment();
            } else if (itemId == R.id.accountOverviewFragmentMenuItem) {
                fragment = new AccountOverviewFragment();
            } else if (itemId == R.id.eventFragmentMenuItem) {
                fragment = new EventsFragment();

                if (getIntent().getExtras() != null && Objects.equals(getIntent().getExtras().getString("role"), "club")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("username", getIntent().getExtras().getString("username"));
                    fragment.setArguments(bundle);
                }

            } else if (itemId == R.id.signout) {
                fragment = null;
                signOut();
            } else {
                fragment = null;
            }
            if (fragment != null) {
                selectFragment(fragment);
                drawerLayout.closeDrawers();
                return true;
            }
            return false;
        });
    }

    /*
     * Helper method to show a fragment in the current view
     */
    private void selectFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
