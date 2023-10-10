package com.example.cyclingmobileapp.lib.event;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


import com.example.cyclingmobileapp.lib.user.ClubAccount;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// line 60 "model.ump"
// line 107 "model.ump"
public class EventType {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //EventType Associations
    private final List<Event> events;
    //EventType Attributes
    private String label;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public EventType(String aLabel) {
        label = aLabel;
        events = new ArrayList<Event>();
    }

    //------------------------
    // INTERFACE
    //------------------------

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfEvents() {
        return 0;
    }

    public boolean setLabel(String aLabel) {
        boolean wasSet = false;
        label = aLabel;
        wasSet = true;
        return wasSet;
    }

    public String getLabel() {
        return label;
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
    public Event addEvent(Instant aStartDate, Instant aEndDate, String aLocation, String aDescription, String aDifficulty, int aParticipantLimit, int aFee, ClubAccount aOrganizer) {
        return new Event(aStartDate, aEndDate, aLocation, aDescription, aDifficulty, aParticipantLimit, aFee, aOrganizer, this);
    }

    public boolean addEvent(Event aEvent) {
        boolean wasAdded = false;
        if (events.contains(aEvent)) {
            return false;
        }
        EventType existingEventType = aEvent.getEventType();
        boolean isNewEventType = existingEventType != null && !this.equals(existingEventType);
        if (isNewEventType) {
            aEvent.setEventType(this);
        } else {
            events.add(aEvent);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeEvent(Event aEvent) {
        boolean wasRemoved = false;
        //Unable to remove aEvent, as it must always have a eventType
        if (!this.equals(aEvent.getEventType())) {
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
    }


    public String toString() {
        return super.toString() + "[" +
                "label" + ":" + getLabel() + "]";
    }
}



