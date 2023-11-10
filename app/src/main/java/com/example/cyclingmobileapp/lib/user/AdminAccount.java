package com.example.cyclingmobileapp.lib.user;



import android.widget.AdapterView ;
import android.widget.Button;
import android.widget.EditText ;
import android.widget.ListView ;
import android.widget.Spinner ;
import android.widget.TextView ;
import android.widget.Toast ;
import android.content.Context ;

import com.google.firebase.database.DataSnapshot ;
import com.google.firebase.database.DatabaseError ;
import com.google.firebase.database.DatabaseReference ;
import com.google.firebase.database.FirebaseDatabase ;
import com.google.firebase.database.ValueEventListener ;
import java.util.ArrayList ;
import java.util.List ;


/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


// line 35 "model.ump"
// line 97 "model.ump"
public class AdminAccount extends Account {

    //------------------------
    // MEMBER VARIABLES
    //------------------------
    private static final String ROLE = "admin";
    //------------------------
    // CONSTRUCTOR
    //------------------------

    public AdminAccount(String aUsername, String aEmail, String aPassword) {
        super(aUsername, aEmail, aPassword, ROLE);
    }

    //------------------------
    // INTERFACE
    //------------------------

    public void delete() {
        super.delete();
    }

    // line 22 "model.ump"
    public void deleteAccount(String username) {

    }

    // line 24 "model.ump"
    public AdminAccount createAdminAccount(String username, String email, String password) {
        throw new UnsupportedOperationException();
    }


    //ALL CODE BELOW WAS ADDED BY MARKUS
    //---------------------------
    // EVENT TYPE CREATION/DELETION
    //---------------------------
    public boolean createEventType() {
        return false ;
    }

    public boolean deleteEventType() {
        return false ;
    }

    //---------------------------
    // ACCOUNT DELETION METHODS
    //---------------------------
    public boolean deleteClubAccount() {
        return false ;
    }

    //Toast will appear on the CreateEventTypeActivity
    public boolean deleteParticipantAccount(String username) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(username) ; //not sure what the reference is
        dR.removeValue() ;
        //Toast.makeText(getApplicationContext(),"Deleted participant account",Toast.LENGTH_LONG).show() ;
        return true;
    }
}
  
  
  