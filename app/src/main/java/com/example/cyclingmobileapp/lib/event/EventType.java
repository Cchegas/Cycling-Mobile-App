package com.example.cyclingmobileapp.lib.event;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/

import com.example.cyclingmobileapp.lib.user.ClubAccount;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// line 37 "model.ump"
// line 100 "model.ump"
public class EventType {

    public static final String COLLECTION_NAME = "EventTypes";

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //EventType Attributes
    private String label;

    //EventType Associations
    private final List<Event> events;
    private final List<RequiredField> requiredFields;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public EventType(String aLabel) {
        label = aLabel;
        events = new ArrayList<Event>();
        requiredFields = new ArrayList<RequiredField>();
    }

    //------------------------
    // INTERFACE
    //------------------------

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfEvents() {
        return 0;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfRequiredFields() {
        return 1;
    }

    public boolean setLabel(String aLabel) {
        label = aLabel;
        return true;
    }

    public String getLabel() {
        return label;
    }

    /* Code from template association_GetMany */
    public Event getEvent(int index) {
        return events.get(index);
    }

    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public int numberOfEvents() {
        return events.size();
    }

    public boolean hasEvents() {
        return events.size() > 0;
    }

    public int indexOfEvent(Event aEvent) {
        return events.indexOf(aEvent);
    }

    /* Code from template association_GetMany */
    public RequiredField getRequiredField(int index) {
        return requiredFields.get(index);
    }

    public List<RequiredField> getRequiredFields() {
        return Collections.unmodifiableList(requiredFields);
    }

    public int numberOfRequiredFields() {
        return requiredFields.size();
    }

    public boolean hasRequiredFields() {
        return requiredFields.size() > 0;
    }

    public int indexOfRequiredField(RequiredField aRequiredField) {
        return requiredFields.indexOf(aRequiredField);
    }

    /* Code from template association_AddManyToOne */
    public Event addEvent(String aTitle, ZonedDateTime aStartDate, ZonedDateTime aEndDate, String aLocation, String aDescription, String aDifficulty, int aParticipantLimit, int aFee, ClubAccount aOrganizer) {
        return new Event(aTitle, aStartDate, aEndDate, aLocation, aDescription, aDifficulty, aParticipantLimit, aFee, aOrganizer, this);
    }

    public boolean addEvent(Event aEvent) {
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
        return true;
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

    /* Code from template association_IsNumberOfValidMethod */
    public boolean isNumberOfRequiredFieldsValid() {
        return numberOfRequiredFields() >= minimumNumberOfRequiredFields();
    }

    /* Code from template association_AddMandatoryManyToOne */
    public RequiredField addRequiredField(String aName, String aType) {
        return new RequiredField(aName, aType, this);
    }

    public boolean addRequiredField(RequiredField aRequiredField) {
        boolean wasAdded = false;
        if (requiredFields.contains(aRequiredField)) {
            return false;
        }
        EventType existingEventType = aRequiredField.getEventType();
        boolean isNewEventType = existingEventType != null && !this.equals(existingEventType);

        if (isNewEventType && existingEventType.numberOfRequiredFields() <= minimumNumberOfRequiredFields()) {
            return wasAdded;
        }
        if (isNewEventType) {
            aRequiredField.setEventType(this);
        } else {
            requiredFields.add(aRequiredField);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeRequiredField(RequiredField aRequiredField) {
        //Unable to remove aRequiredField, as it must always have a eventType
        if (this.equals(aRequiredField.getEventType())) {
            return false;
        }

        //eventType already at minimum (1)
        if (numberOfRequiredFields() <= minimumNumberOfRequiredFields()) {
            return false;
        }

        requiredFields.remove(aRequiredField);
        return true;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addRequiredFieldAt(RequiredField aRequiredField, int index) {
        boolean wasAdded = false;
        if (addRequiredField(aRequiredField)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfRequiredFields()) {
                index = numberOfRequiredFields() - 1;
            }
            requiredFields.remove(aRequiredField);
            requiredFields.add(index, aRequiredField);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveRequiredFieldAt(RequiredField aRequiredField, int index) {
        boolean wasAdded = false;
        if (requiredFields.contains(aRequiredField)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfRequiredFields()) {
                index = numberOfRequiredFields() - 1;
            }
            requiredFields.remove(aRequiredField);
            requiredFields.add(index, aRequiredField);
            wasAdded = true;
        } else {
            wasAdded = addRequiredFieldAt(aRequiredField, index);
        }
        return wasAdded;
    }

    public void delete() {
        for (int i = events.size(); i > 0; i--) {
            Event aEvent = events.get(i - 1);
            aEvent.delete();
        }
        for (int i = requiredFields.size(); i > 0; i--) {
            RequiredField aRequiredField = requiredFields.get(i - 1);
            aRequiredField.delete();
        }
    }


    public String toString() {
        return super.toString() + "[" +
                "label" + ":" + getLabel() + "]";
    }
}