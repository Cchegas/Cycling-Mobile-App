package com.example.cyclingmobileapp.lib.user;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


// line 35 "model.ump"
// line 97 "model.ump"
public class AdminAccount extends Account {

    //------------------------
    // MEMBER VARIABLES
    //------------------------
    public static final String ROLE = "admin";
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

}
  
  
  