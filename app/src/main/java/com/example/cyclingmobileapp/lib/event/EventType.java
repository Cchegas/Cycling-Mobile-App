package com.example.cyclingmobileapp.lib.event;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/

import com.example.cyclingmobileapp.lib.user.ClubAccount;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    private boolean enabled;

    //EventType Associations
    private final List<Event> events;
    private final List<RequiredField> requiredFields;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public EventType(String aLabel, boolean aEnabled) {
        label = aLabel;
        enabled = aEnabled;
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
        boolean wasSet = false;
        label = aLabel;
        wasSet = true;
        return wasSet;
    }

    public boolean setEnabled(boolean aEnabled) {
        boolean wasSet = false;
        enabled = aEnabled;
        wasSet = true;
        return wasSet;
    }

    public String getLabel() {
        return label;
    }

    public boolean getEnabled() {
        return enabled;
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

    /* Code from template association_GetMany */
    public RequiredField getRequiredField(int index) {
        RequiredField aRequiredField = requiredFields.get(index);
        return aRequiredField;
    }

    public List<RequiredField> getRequiredFields() {
        List<RequiredField> newRequiredFields = Collections.unmodifiableList(requiredFields);
        return newRequiredFields;
    }

    public int numberOfRequiredFields() {
        int number = requiredFields.size();
        return number;
    }

    public boolean hasRequiredFields() {
        boolean has = requiredFields.size() > 0;
        return has;
    }

    public int indexOfRequiredField(RequiredField aRequiredField) {
        int index = requiredFields.indexOf(aRequiredField);
        return index;
    }

    /* Code from template association_AddManyToOne */
    public Event addEvent(String aTitle, ZonedDateTime aStartDate, ZonedDateTime aEndDate, String aLocation, String aDescription, String aDifficulty, int aParticipantLimit, int aFee, ClubAccount aOrganizer) {
        return new Event(aTitle, aStartDate, aEndDate, aLocation, aDescription, aDifficulty, aParticipantLimit, aFee, aOrganizer, this);
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

    /* Code from template association_IsNumberOfValidMethod */
    public boolean isNumberOfRequiredFieldsValid() {
        boolean isValid = numberOfRequiredFields() >= minimumNumberOfRequiredFields();
        return isValid;
    }

    /* Code from template association_AddMandatoryManyToOne */
    public RequiredField addRequiredField(String aName, String aType) {
        RequiredField aNewRequiredField = new RequiredField(aName, aType, this);
        return aNewRequiredField;
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
        boolean wasRemoved = false;
        //Unable to remove aRequiredField, as it must always have a eventType
        if (this.equals(aRequiredField.getEventType())) {
            return wasRemoved;
        }

        //eventType already at minimum (1)
        if (numberOfRequiredFields() <= minimumNumberOfRequiredFields()) {
            return wasRemoved;
        }

        requiredFields.remove(aRequiredField);
        wasRemoved = true;
        return wasRemoved;
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
                "label" + ":" + getLabel() + "," +
                "enabled" + ":" + getEnabled() + "]";
    }

    public void upload(){
        upload(this.label);
    }

    public void upload(String documentKey) {
        HashMap<String, Object> requiredFieldData = new HashMap<String, Object>();
        for (int i = 0; i < this.requiredFields.size(); i++) {
            requiredFieldData.put(this.requiredFields.get(i).getName(), this.requiredFields.get(i).getType());
        }

        HashMap<String, Object> eventTypeData = new HashMap<String, Object>();
        eventTypeData.put("label", this.label);
        eventTypeData.put("enabled", this.enabled);
        eventTypeData.put("requiredFields", requiredFieldData);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Delete any document that this could be replacing (since Firestore document keys can't seem to be changed)
        if (!documentKey.equals(this.label) && !documentKey.trim().equals("")){
            db.collection(COLLECTION_NAME).document(documentKey).delete();
        }
        db.collection(COLLECTION_NAME).document(this.label).set(eventTypeData);
    }
}