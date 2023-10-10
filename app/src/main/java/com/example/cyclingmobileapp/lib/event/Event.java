package com.example.cyclingmobileapp.lib.event;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


import com.example.cyclingmobileapp.lib.user.ClubAccount;
import com.example.cyclingmobileapp.lib.user.ParticipantAccount;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// line 66 "model.ump"
// line 113 "model.ump"
public class Event {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    private final List<ParticipantAccount> participantAccounts;
    //Event Attributes
    private Instant startDate;
    private Instant endDate;
    private String location;
    private String description;
    private String difficulty;
    private int participantLimit;
    private int fee;
    //Event Associations
    private ClubAccount organizer;
    private EventType eventType;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Event(Instant aStartDate, Instant aEndDate, String aLocation, String aDescription, String aDifficulty, int aParticipantLimit, int aFee, ClubAccount aOrganizer, EventType aEventType) {
        startDate = aStartDate;
        endDate = aEndDate;
        location = aLocation;
        description = aDescription;
        difficulty = aDifficulty;
        participantLimit = aParticipantLimit;
        fee = aFee;
        boolean didAddOrganizer = setOrganizer(aOrganizer);
        if (!didAddOrganizer) {
            throw new RuntimeException("Unable to create event due to organizer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
        participantAccounts = new ArrayList<ParticipantAccount>();
        boolean didAddEventType = setEventType(aEventType);
        if (!didAddEventType) {
            throw new RuntimeException("Unable to create event due to eventType. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
    }

    //------------------------
    // INTERFACE
    //------------------------

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfParticipantAccounts() {
        return 0;
    }

    public boolean setStartDate(Instant aStartDate) {
        boolean wasSet = false;
        startDate = aStartDate;
        wasSet = true;
        return wasSet;
    }

    public boolean setEndDate(Instant aEndDate) {
        boolean wasSet = false;
        endDate = aEndDate;
        wasSet = true;
        return wasSet;
    }

    public boolean setLocation(String aLocation) {
        boolean wasSet = false;
        location = aLocation;
        wasSet = true;
        return wasSet;
    }

    public boolean setDescription(String aDescription) {
        boolean wasSet = false;
        description = aDescription;
        wasSet = true;
        return wasSet;
    }

    public boolean setDifficulty(String aDifficulty) {
        boolean wasSet = false;
        difficulty = aDifficulty;
        wasSet = true;
        return wasSet;
    }

    public boolean setParticipantLimit(int aParticipantLimit) {
        boolean wasSet = false;
        participantLimit = aParticipantLimit;
        wasSet = true;
        return wasSet;
    }

    public boolean setFee(int aFee) {
        boolean wasSet = false;
        fee = aFee;
        wasSet = true;
        return wasSet;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Alternatively, difficulty can be represented as an enum..?
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Max size of registeredParticipants is participantLimit
     */
    public int getParticipantLimit() {
        return participantLimit;
    }

    public int getFee() {
        return fee;
    }

    /* Code from template association_GetOne */
    public ClubAccount getOrganizer() {
        return organizer;
    }

    /* Code from template association_GetMany */
    public ParticipantAccount getParticipantAccount(int index) {
        ParticipantAccount aParticipantAccount = participantAccounts.get(index);
        return aParticipantAccount;
    }

    public List<ParticipantAccount> getParticipantAccounts() {
        List<ParticipantAccount> newParticipantAccounts = Collections.unmodifiableList(participantAccounts);
        return newParticipantAccounts;
    }

    public int numberOfParticipantAccounts() {
        int number = participantAccounts.size();
        return number;
    }

    public boolean hasParticipantAccounts() {
        boolean has = participantAccounts.size() > 0;
        return has;
    }

    public int indexOfParticipantAccount(ParticipantAccount aParticipantAccount) {
        int index = participantAccounts.indexOf(aParticipantAccount);
        return index;
    }

    /* Code from template association_GetOne */
    public EventType getEventType() {
        return eventType;
    }

    /* Code from template association_SetOneToMany */
    public boolean setOrganizer(ClubAccount aOrganizer) {
        boolean wasSet = false;
        if (aOrganizer == null) {
            return wasSet;
        }

        ClubAccount existingOrganizer = organizer;
        organizer = aOrganizer;
        if (existingOrganizer != null && !existingOrganizer.equals(aOrganizer)) {
            existingOrganizer.removeEvent(this);
        }
        organizer.addEvent(this);
        wasSet = true;
        return wasSet;
    }

    /* Code from template association_AddManyToManyMethod */
    public boolean addParticipantAccount(ParticipantAccount aParticipantAccount) {
        boolean wasAdded = false;
        if (participantAccounts.contains(aParticipantAccount)) {
            return false;
        }
        participantAccounts.add(aParticipantAccount);
        if (aParticipantAccount.indexOfRegisteredEvent(this) != -1) {
            wasAdded = true;
        } else {
            wasAdded = aParticipantAccount.addRegisteredEvent(this);
            if (!wasAdded) {
                participantAccounts.remove(aParticipantAccount);
            }
        }
        return wasAdded;
    }

    /* Code from template association_RemoveMany */
    public boolean removeParticipantAccount(ParticipantAccount aParticipantAccount) {
        boolean wasRemoved = false;
        if (!participantAccounts.contains(aParticipantAccount)) {
            return wasRemoved;
        }

        int oldIndex = participantAccounts.indexOf(aParticipantAccount);
        participantAccounts.remove(oldIndex);
        if (aParticipantAccount.indexOfRegisteredEvent(this) == -1) {
            wasRemoved = true;
        } else {
            wasRemoved = aParticipantAccount.removeRegisteredEvent(this);
            if (!wasRemoved) {
                participantAccounts.add(oldIndex, aParticipantAccount);
            }
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addParticipantAccountAt(ParticipantAccount aParticipantAccount, int index) {
        boolean wasAdded = false;
        if (addParticipantAccount(aParticipantAccount)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfParticipantAccounts()) {
                index = numberOfParticipantAccounts() - 1;
            }
            participantAccounts.remove(aParticipantAccount);
            participantAccounts.add(index, aParticipantAccount);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveParticipantAccountAt(ParticipantAccount aParticipantAccount, int index) {
        boolean wasAdded = false;
        if (participantAccounts.contains(aParticipantAccount)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfParticipantAccounts()) {
                index = numberOfParticipantAccounts() - 1;
            }
            participantAccounts.remove(aParticipantAccount);
            participantAccounts.add(index, aParticipantAccount);
            wasAdded = true;
        } else {
            wasAdded = addParticipantAccountAt(aParticipantAccount, index);
        }
        return wasAdded;
    }

    /* Code from template association_SetOneToMany */
    public boolean setEventType(EventType aEventType) {
        boolean wasSet = false;
        if (aEventType == null) {
            return wasSet;
        }

        EventType existingEventType = eventType;
        eventType = aEventType;
        if (existingEventType != null && !existingEventType.equals(aEventType)) {
            existingEventType.removeEvent(this);
        }
        eventType.addEvent(this);
        wasSet = true;
        return wasSet;
    }

    public void delete() {
        ClubAccount placeholderOrganizer = organizer;
        this.organizer = null;
        if (placeholderOrganizer != null) {
            placeholderOrganizer.removeEvent(this);
        }
        ArrayList<ParticipantAccount> copyOfParticipantAccounts = new ArrayList<ParticipantAccount>(participantAccounts);
        participantAccounts.clear();
        for (ParticipantAccount aParticipantAccount : copyOfParticipantAccounts) {
            aParticipantAccount.removeRegisteredEvent(this);
        }
        EventType placeholderEventType = eventType;
        this.eventType = null;
        if (placeholderEventType != null) {
            placeholderEventType.removeEvent(this);
        }
    }

    // line 77 "model.ump"
    public boolean isComplete() {
        return Instant.now().isAfter(endDate);
    }


    public String toString() {
        return super.toString() + "[" +
                "location" + ":" + getLocation() + "," +
                "description" + ":" + getDescription() + "," +
                "difficulty" + ":" + getDifficulty() + "," +
                "participantLimit" + ":" + getParticipantLimit() + "," +
                "fee" + ":" + getFee() + "]" + System.getProperties().getProperty("line.separator") +
                "  " + "startDate" + "=" + (getStartDate() != null ? !getStartDate().equals(this) ? getStartDate().toString().replaceAll("  ", "    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
                "  " + "endDate" + "=" + (getEndDate() != null ? !getEndDate().equals(this) ? getEndDate().toString().replaceAll("  ", "    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
                "  " + "organizer = " + (getOrganizer() != null ? Integer.toHexString(System.identityHashCode(getOrganizer())) : "null") + System.getProperties().getProperty("line.separator") +
                "  " + "eventType = " + (getEventType() != null ? Integer.toHexString(System.identityHashCode(getEventType())) : "null");
    }
}




