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
        name = aName;
        return true;
    }

    public boolean setType(String aType) {
        type = aType;
        return true;
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
        //Must provide eventType to requiredField
        if (aEventType == null) {
            return false;
        }

        if (eventType != null && eventType.numberOfRequiredFields() <= EventType.minimumNumberOfRequiredFields()) {
            return false;
        }

        EventType existingEventType = eventType;
        eventType = aEventType;
        if (existingEventType != null && !existingEventType.equals(aEventType)) {
            boolean didRemove = existingEventType.removeRequiredField(this);
            if (!didRemove) {
                eventType = existingEventType;
                return false;
            }
        }
        eventType.addRequiredField(this);
        return true;
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