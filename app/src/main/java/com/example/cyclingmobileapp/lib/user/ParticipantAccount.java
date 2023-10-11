package com.example.cyclingmobileapp.lib.user;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


import androidx.annotation.NonNull;

import com.example.cyclingmobileapp.lib.event.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// line 18 "model.ump"
// line 91 "model.ump"
public class ParticipantAccount extends Account {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    // line 23 "model.ump"
    final String role = "participant";
    //------------------------
    // CONSTRUCTOR
    //------------------------
    //ParticipantAccount Associations
    private final List<Event> registeredEvents;
    //ParticipantAccount Attributes
    private String firstName;
    private String lastName;

    //------------------------
    // INTERFACE
    //------------------------

    public ParticipantAccount(String aUsername, String aEmail, String aPassword, String aFirstName, String aLastName) {
        super(aUsername, aEmail, aPassword);
        firstName = aFirstName;
        lastName = aLastName;
        registeredEvents = new ArrayList<Event>();
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfRegisteredEvents() {
        return 0;
    }

    public boolean setFirstName(String aFirstName) {
        boolean wasSet = false;
        firstName = aFirstName;
        wasSet = true;
        return wasSet;
    }

    public boolean setLastName(String aLastName) {
        boolean wasSet = false;
        lastName = aLastName;
        wasSet = true;
        return wasSet;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /* Code from template association_GetMany */
    public Event getRegisteredEvent(int index) {
        return registeredEvents.get(index);
    }

    public List<Event> getRegisteredEvents() {
        return Collections.unmodifiableList(registeredEvents);
    }

    public int numberOfRegisteredEvents() {
        return registeredEvents.size();
    }

    public boolean hasRegisteredEvents() {
        return registeredEvents.size() > 0;
    }

    public int indexOfRegisteredEvent(Event aRegisteredEvent) {
        return registeredEvents.indexOf(aRegisteredEvent);
    }

    /* Code from template association_AddManyToManyMethod */
    public boolean addRegisteredEvent(Event aRegisteredEvent) {
        boolean wasAdded = false;
        if (registeredEvents.contains(aRegisteredEvent)) {
            return false;
        }
        registeredEvents.add(aRegisteredEvent);
        if (aRegisteredEvent.indexOfParticipantAccount(this) != -1) {
            wasAdded = true;
        } else {
            wasAdded = aRegisteredEvent.addParticipantAccount(this);
            if (!wasAdded) {
                registeredEvents.remove(aRegisteredEvent);
            }
        }
        return wasAdded;
    }

    /* Code from template association_RemoveMany */
    public boolean removeRegisteredEvent(Event aRegisteredEvent) {
        boolean wasRemoved = false;
        if (!registeredEvents.contains(aRegisteredEvent)) {
            return wasRemoved;
        }

        int oldIndex = registeredEvents.indexOf(aRegisteredEvent);
        registeredEvents.remove(oldIndex);
        if (aRegisteredEvent.indexOfParticipantAccount(this) == -1) {
            wasRemoved = true;
        } else {
            wasRemoved = aRegisteredEvent.removeParticipantAccount(this);
            if (!wasRemoved) {
                registeredEvents.add(oldIndex, aRegisteredEvent);
            }
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addRegisteredEventAt(Event aRegisteredEvent, int index) {
        boolean wasAdded = false;
        if (addRegisteredEvent(aRegisteredEvent)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfRegisteredEvents()) {
                index = numberOfRegisteredEvents() - 1;
            }
            registeredEvents.remove(aRegisteredEvent);
            registeredEvents.add(index, aRegisteredEvent);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveRegisteredEventAt(Event aRegisteredEvent, int index) {
        boolean wasAdded = false;
        if (registeredEvents.contains(aRegisteredEvent)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfRegisteredEvents()) {
                index = numberOfRegisteredEvents() - 1;
            }
            registeredEvents.remove(aRegisteredEvent);
            registeredEvents.add(index, aRegisteredEvent);
            wasAdded = true;
        } else {
            wasAdded = addRegisteredEventAt(aRegisteredEvent, index);
        }
        return wasAdded;
    }

    public void delete() {
        ArrayList<Event> copyOfRegisteredEvents = new ArrayList<Event>(registeredEvents);
        registeredEvents.clear();
        for (Event aRegisteredEvent : copyOfRegisteredEvents) {
            aRegisteredEvent.removeParticipantAccount(this);
        }
        super.delete();
    }

    @Override
    // line 27 "model.ump"
    public String greetingMessage() {
        return "Welcome " + firstName + ". You are logged in as \"" + role + "\".";
    }

    // line 29 "model.ump"
    public void update() {

    }

    public String getRole() {
        return role;
    }
    //------------------------
    // DEVELOPER CODE - PROVIDED AS-IS
    //------------------------

    @NonNull
    public String toString() {
        return super.toString() + "[" +
                "firstName" + ":" + getFirstName() + "," +
                "lastName" + ":" + getLastName() + "]";
    }


}



