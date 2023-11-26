package com.example.cyclingmobileapp;

import android.os.Bundle;

import com.example.cyclingmobileapp.ui.RequestsFragment;
import com.example.cyclingmobileapp.ui.SendNotifsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cyclingmobileapp.databinding.ActivityBottomNavigationBarForClubsBinding;



public class BottomNavigationBarForClubs extends AppCompatActivity {

    private ActivityBottomNavigationBarForClubsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBottomNavigationBarForClubsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replacefragment(new EventTypeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.event_edit) {
                replacefragment(new EventTypeFragment());
            } else if (item.getItemId() == R.id.Send_Notifs) {
                replacefragment(new RequestsFragment());
            } else if (item.getItemId() == R.id.Requests) {
                replacefragment(new SendNotifsFragment());
            }
            return true;
        });
    }

    private void replacefragment(Fragment fragment){
        FragmentManager fragmentManger = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}