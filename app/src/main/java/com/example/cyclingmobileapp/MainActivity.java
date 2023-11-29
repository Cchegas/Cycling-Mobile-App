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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private String username;
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
            username = getIntent().getExtras().getString("username");
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = headerView.findViewById(R.id.nav_header_username);
            navUsername.setText(username);
            String role = getIntent().getExtras().getString("role");
            navigationView.getMenu().clear();
            if (role != null && role.equals("admin")) {
                navigationView.inflateMenu(R.menu.menu_admin);
            } else if (role != null && role.equals("club")) {
                navigationView.inflateMenu(R.menu.menu_club);
            } else {
                navigationView.inflateMenu(R.menu.menu_participant);
            }

        }
        // Manually select the first item in the navigation menu, and, correspondingly, show the first fragment
        if (getIntent().getExtras() != null && Objects.equals(getIntent().getExtras().getString("role"), "club")) {
            navigationView.setCheckedItem(R.id.eventFragmentMenuItem);
            Bundle bundle = new Bundle();
            bundle.putString("username", getIntent().getExtras().getString("username"));
            Fragment fragment = new EventsFragment();
            fragment.setArguments(bundle);
            selectFragment(fragment);
        } else if (getIntent().getExtras() != null && Objects.equals(getIntent().getExtras().getString("role"), "admin")) {
            navigationView.setCheckedItem(R.id.eventTypeFragmentMenuItem);
            selectFragment(new EventTypeFragment());
        } else {
            navigationView.setCheckedItem(R.id.accountOverviewFragmentMenuItem);
            selectFragment(new AccountOverviewFragment());
        }
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
                if (getIntent().getExtras() != null && Objects.equals(getIntent().getExtras().getString("role"), "club")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    fragment = new EventsFragment();
                    fragment.setArguments(bundle);
                } else {
                    fragment = null;
                }
            } else if (itemId == R.id.profileActivityMenuItem){
                fragment = null;
                Intent profileActivityIntent = new Intent(this, ProfileActivity.class);
                profileActivityIntent.putExtra("username", username);
                startActivity(profileActivityIntent);
            }
            else if (itemId == R.id.signout) {
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
