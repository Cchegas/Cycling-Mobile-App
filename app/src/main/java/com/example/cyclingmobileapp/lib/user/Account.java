package com.example.cyclingmobileapp.lib.user;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


/**
 * Consider proper access modifiers (ex: private, package, etc.) and getters/setters?
 */
// line 4 "model.ump"
// line 86 "model.ump"
public abstract class Account {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Account Attributes
    private String username;
    private String email;
    private String password;
    private String role;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Account(String aUsername, String aEmail, String aPassword, String aRole) {
        username = aUsername;
        email = aEmail;
        password = aPassword;
        role = aRole;
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setUsername(String aUsername) {
        boolean wasSet = false;
        username = aUsername;
        wasSet = true;
        return wasSet;
    }

    public boolean setEmail(String aEmail) {
        boolean wasSet = false;
        email = aEmail;
        wasSet = true;
        return wasSet;
    }

    public boolean setPassword(String aPassword) {
        boolean wasSet = false;
        password = aPassword;
        wasSet = true;
        return wasSet;
    }

    public boolean setRole(String aRole) {
        boolean wasSet = false;
        role = aRole;
        wasSet = true;
        return wasSet;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Email is required? Project doc mentions "you can???t enter an invalid email"
     */
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void delete() {
    }

    // line 12 "model.ump"
    public String greetingMessage() {
        return "Welcome " + username + ". You are loggged in as \"" + role + "\".";
    }

    public abstract void update();


    public String toString() {
        return super.toString() + "[" +
                "username" + ":" + getUsername() + "," +
                "email" + ":" + getEmail() + "," +
                "password" + ":" + getPassword() + "," +
                "role" + ":" + getRole() + "]";
    }
}