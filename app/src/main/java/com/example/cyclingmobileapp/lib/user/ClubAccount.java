package com.example.cyclingmobileapp.lib.user;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


import com.example.cyclingmobileapp.lib.event.Event;
import com.example.cyclingmobileapp.lib.event.EventType;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClubAccount extends Account {

    //------------------------
    // MEMBER VARIABLES
    //------------------------
    public static final String ROLE = "club";
    //ClubAccount Associations
    private final List<Event> events;
    //ClubAccount Attributes
    private String name;

    private List<Integer> ratings ;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public ClubAccount(String aUsername, String aEmail, String aPassword, String aName) {
        super(aUsername, aEmail, aPassword, ROLE);
        name = aName;
        events = new ArrayList<Event>();
    }

    //------------------------
    // INTERFACE
    //------------------------

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfEvents() {
        return 0;
    }

    public boolean setName(String aName) {
        boolean wasSet = false;
        name = aName;
        wasSet = true;
        return wasSet;
    }

    public String getName() {
        return name;
    }

    /* Code from template association_GetMany */
    public Event getEvent(int index) {
        Event aEvent = events.get(index);
        return aEvent;
    }

    public List<Event> getEvents() {
        List<Event> newEvents = Collections.unmodifiableList(events);
        return newEvents;
    }

    public int numberOfEvents() {
        int number = events.size();
        return number;
    }

    public boolean hasEvents() {
        boolean has = events.size() > 0;
        return has;
    }

    public int indexOfEvent(Event aEvent) {
        int index = events.indexOf(aEvent);
        return index;
    }

    /* Code from template association_AddManyToOne */
    public Event addEvent(String aTitle, ZonedDateTime aStartDate, ZonedDateTime aEndDate, String aLocation, String aDescription, String aDifficulty, int aParticipantLimit, int aFee, EventType aEventType) {
        return new Event(aTitle, aStartDate, aEndDate, aLocation, aDescription, aDifficulty, aParticipantLimit, aFee, this, aEventType);
    }

    public boolean addEvent(Event aEvent) {
        boolean wasAdded = false;
        if (events.contains(aEvent)) {
            return false;
        }
        ClubAccount existingOrganizer = aEvent.getOrganizer();
        boolean isNewOrganizer = existingOrganizer != null && !this.equals(existingOrganizer);
        if (isNewOrganizer) {
            aEvent.setOrganizer(this);
        } else {
            events.add(aEvent);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeEvent(Event aEvent) {
        boolean wasRemoved = false;
        //Unable to remove aEvent, as it must always have a organizer
        if (!this.equals(aEvent.getOrganizer())) {
            events.remove(aEvent);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addEventAt(Event aEvent, int index) {
        boolean wasAdded = false;
        if (addEvent(aEvent)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfEvents()) {
                index = numberOfEvents() - 1;
            }
            events.remove(aEvent);
            events.add(index, aEvent);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveEventAt(Event aEvent, int index) {
        boolean wasAdded = false;
        if (events.contains(aEvent)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfEvents()) {
                index = numberOfEvents() - 1;
            }
            events.remove(aEvent);
            events.add(index, aEvent);
            wasAdded = true;
        } else {
            wasAdded = addEventAt(aEvent, index);
        }
        return wasAdded;
    }

    public void delete() {
        for (int i = events.size(); i > 0; i--) {
            Event aEvent = events.get(i - 1);
            aEvent.delete();
        }
        super.delete();
    }


    public String toString() {
        return super.toString() + "[" +
                "name" + ":" + getName() + "]";
    }
}