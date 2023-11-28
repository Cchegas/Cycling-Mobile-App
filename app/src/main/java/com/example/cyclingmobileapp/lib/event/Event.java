package com.example.cyclingmobileapp.lib.event;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/

import com.example.cyclingmobileapp.lib.user.ClubAccount;
import com.example.cyclingmobileapp.lib.user.ParticipantAccount;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


// line 44 "model.ump"
// line 92 "model.ump"
public class Event {


    public static final String COLLECTION_NAME = "Events";

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    private final List<ParticipantAccount> participantAccounts;
    //Event Attributes
    private String title;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
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

    public Event(String aTitle, ZonedDateTime aStartDate, ZonedDateTime aEndDate, String aLocation, String aDescription, String aDifficulty, int aParticipantLimit, int aFee, ClubAccount aOrganizer, EventType aEventType) {
        title = aTitle;
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

    public Event(String aTitle) {
        title = aTitle;
        participantAccounts = new ArrayList<ParticipantAccount>();
    }

    //------------------------
    // INTERFACE
    //------------------------

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfParticipantAccounts() {
        return 0;
    }

    public boolean setTitle(String aTitle) {
        title = aTitle;
        return true;
    }

    public boolean setStartDate(ZonedDateTime aStartDate) {
        startDate = aStartDate;
        return true;
    }

    public boolean setEndDate(ZonedDateTime aEndDate) {
        endDate = aEndDate;
        return true;
    }

    public boolean setLocation(String aLocation) {
        location = aLocation;
        return true;
    }

    public boolean setDescription(String aDescription) {
        description = aDescription;
        return true;
    }

    public boolean setDifficulty(String aDifficulty) {
        difficulty = aDifficulty;
        return true;
    }

    public boolean setParticipantLimit(int aParticipantLimit) {
        participantLimit = aParticipantLimit;
        return true;
    }

    public boolean setFee(int aFee) {
        fee = aFee;
        return true;
    }

    public String getTitle() {
        return title;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public ZonedDateTime getEndDate() {
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
        return participantAccounts.get(index);
    }

    public List<ParticipantAccount> getParticipantAccounts() {
        return Collections.unmodifiableList(participantAccounts);
    }

    public int numberOfParticipantAccounts() {
        return participantAccounts.size();
    }

    public boolean hasParticipantAccounts() {
        return participantAccounts.size() > 0;
    }

    public int indexOfParticipantAccount(ParticipantAccount aParticipantAccount) {
        return participantAccounts.indexOf(aParticipantAccount);
    }

    /* Code from template association_GetOne */
    public EventType getEventType() {
        return eventType;
    }

    /* Code from template association_SetOneToMany */
    public boolean setOrganizer(ClubAccount aOrganizer) {
        if (aOrganizer == null) {
            return false;
        }

        ClubAccount existingOrganizer = organizer;
        organizer = aOrganizer;
        if (existingOrganizer != null && !existingOrganizer.equals(aOrganizer)) {
            existingOrganizer.removeEvent(this);
        }
        organizer.addEvent(this);
        return true;
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
            return false;
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
        if (aEventType == null) {
            return false;
        }

        EventType existingEventType = eventType;
        eventType = aEventType;
        if (existingEventType != null && !existingEventType.equals(aEventType)) {
            existingEventType.removeEvent(this);
        }
        eventType.addEvent(this);
        return true;
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

    // line 52 "model.ump"
    public boolean isComplete() {
        throw new UnsupportedOperationException();
    }


    public String toString() {
        return super.toString() + "[" +
                "title" + ":" + getTitle() + "," +
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