package android.portfolio.petshuddle.Entity;

import java.util.Date;

public class Event {

    private int eventId;
    private String eventTitle;
    private String eventDetails;
    private String eventLocation;
    private String eventDate;
    private String userId;
    private int numberOfPetAttendees;

    public Event(int eventId, String eventTitle, String eventDetails, String eventLocation, String eventDate, String userId) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDetails = eventDetails;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.userId = userId;
    }

    //constructor with attendees for the event fragment to get the number of pet attendees
    public Event(int eventId, String eventTitle, String eventDetails, String eventLocation, String eventDate, String userId, int numberOfPetAttendees) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDetails = eventDetails;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.userId = userId;
        this.numberOfPetAttendees = numberOfPetAttendees;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getNumberOfPetAttendees() {
        return numberOfPetAttendees;
    }

    public void setNumberOfPetAttendees(int numberOfPetAttendees) {
        this.numberOfPetAttendees = numberOfPetAttendees;
    }
}

