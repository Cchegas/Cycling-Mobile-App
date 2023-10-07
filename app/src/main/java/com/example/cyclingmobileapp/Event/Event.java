package com.example.cyclingmobileapp.Event;

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

    // Constructor ****************
    public Event(Date start, Date end, String loc, String desc, String dif, int partLimit, int fee, EventType type){
        this.startDate = start;
        this.endDate = end;
        this.location = loc;
        this.description = desc;
        this.difficulty = dif;
        this.participantLimit = partLimit;
        this.fee = fee;
        this.eventType = type;
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
}
