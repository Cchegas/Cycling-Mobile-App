package com.example.cyclingmobileapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class SearchActivity extends AppCompatActivity {

    private String clubName ;

    private String eventName ;
    private String eventTypeName ;
    private FirebaseFirestore db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_search);
        this.db = FirebaseFirestore.getInstance() ;
    }

    //what this activity does:
    //allows participants to search for clubs
    //displays results from database
}