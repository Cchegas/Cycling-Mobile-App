package com.example.cyclingmobileapp.Event;

import com.example.cyclingmobileapp.User.*;

import java.util.Date;

public class Event {
    // Instance Variables ****************
    private Date startDate;
    private Date endDate;
    private String location;
    private String description;
    private String difficulty;
    private int participantLimit;
    private int fee;
    private EventType eventType;
    private Club organizer;
    private Participant[] participants;


    // Constructor ****************
    public Event(Date start, Date end, String loc, String desc, String dif,
                 int partLimit, int fee, EventType type, Club org){
        this.startDate = start;
        this.endDate = end;
        this.location = loc;
        this.description = desc;
        this.difficulty = dif;
        this.participantLimit = partLimit;
        this.fee = fee;
        this.eventType = type;
        this.participants = new Participant[this.participantLimit];
        this.organizer = org;
    }

    // Instance Methods ****************
    public Date getEndDate() {
        return endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getParticipantLimit() {
        return participantLimit;
    }

    public int getFee() {
        return fee;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Club getOrganizer() {
        return organizer;
    }

    public Participant[] getParticipants() {
        return participants;
    }

    public int getNumParticipants() {
        return participants.length;
    }





    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setParticipantLimit(int participantLimit) {
        this.participantLimit = participantLimit;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }



//    public void addParticipant(Participant p) {
//        this.participants[this.getNumParticipants()] = p;
//    }

//    public void removeParticipants(Participant p) {
//
//    }
}
