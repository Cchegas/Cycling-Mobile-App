package com.example.cyclingmobileapp.lib.user;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


// line 35 "model.ump"
// line 97 "model.ump"
public class AdminAccount extends Account {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //------------------------
    // CONSTRUCTOR
    //------------------------

    // line 38 "model.ump"
    final String role = "admin";

    //------------------------
    // INTERFACE
    //------------------------

    public AdminAccount(String aUsername, String aEmail, String aPassword) {
        super(aUsername, aEmail, aPassword);
    }

    public void delete() {
        super.delete();
    }

    // line 41 "model.ump"
    public void deleteAccount(String username) {

    }

    // line 44 "model.ump"
    public AdminAccount createAdminAccount(String username, String email, String password) {
        return null;
    }

    //------------------------
    // DEVELOPER CODE - PROVIDED AS-IS
    //------------------------

    // line 47 "model.ump"
    public void update() {

    }

    public String getRole() {
        return role;
    }


}
