package com.example.cyclingmobileapp.lib.event;

/*PLEASE DO NOT EDIT THIS CODE*/

/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/

// line 62 "model.ump"
// line 108 "model.ump"
public class RequiredField {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //RequiredField Attributes
    private String name;
    private String type;

    //RequiredField Associations
    private EventType eventType;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public RequiredField(String aName, String aType, EventType aEventType) {
        name = aName;
        type = aType;
        boolean didAddEventType = setEventType(aEventType);
        if (!didAddEventType) {
            throw new RuntimeException("Unable to create requiredField due to eventType. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setName(String aName) {
        boolean wasSet = false;
        name = aName;
        wasSet = true;
        return wasSet;
    }

    public boolean setType(String aType) {
        boolean wasSet = false;
        type = aType;
        wasSet = true;
        return wasSet;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    /* Code from template association_GetOne */
    public EventType getEventType() {
        return eventType;
    }

    /* Code from template association_SetOneToMandatoryMany */
    public boolean setEventType(EventType aEventType) {
        boolean wasSet = false;
        //Must provide eventType to requiredField
        if (aEventType == null) {
            return wasSet;
        }

        if (eventType != null && eventType.numberOfRequiredFields() <= EventType.minimumNumberOfRequiredFields()) {
            return wasSet;
        }

        EventType existingEventType = eventType;
        eventType = aEventType;
        if (existingEventType != null && !existingEventType.equals(aEventType)) {
            boolean didRemove = existingEventType.removeRequiredField(this);
            if (!didRemove) {
                eventType = existingEventType;
                return wasSet;
            }
        }
        eventType.addRequiredField(this);
        wasSet = true;
        return wasSet;
    }

    public void delete() {
        EventType placeholderEventType = eventType;
        this.eventType = null;
        if (placeholderEventType != null) {
            placeholderEventType.removeRequiredField(this);
        }
    }


    public String toString() {
        return super.toString() + "[" +
                "name" + ":" + getName() + "," +
                "type" + ":" + getType() + "]" + System.getProperties().getProperty("line.separator") +
                "  " + "eventType = " + (getEventType() != null ? Integer.toHexString(System.identityHashCode(getEventType())) : "null");
    }
}