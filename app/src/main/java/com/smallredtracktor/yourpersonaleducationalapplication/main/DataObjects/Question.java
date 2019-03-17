package com.smallredtracktor.yourpersonaleducationalapplication.main.DataObjects;

public class Question {


    private int id;
    private int ticketId;
    private int type;
    private String value;
    private int userStackNumber;

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getUserStackNumber() {
        return userStackNumber;
    }

    public void setUserStackNumber(int userStackNumber) {
        this.userStackNumber = userStackNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
