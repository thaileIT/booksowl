package com.example.bookproject.model;

public class BookOrdered {
    long timestamp;
    String bookID,UID;
    int numberOrders;

    public BookOrdered(long timestamp, String bookID, String UID, int numberOrders) {
        this.timestamp = timestamp;
        this.bookID = bookID;
        this.UID = UID;
        this.numberOrders = numberOrders;
    }

    public BookOrdered() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public int getNumberOrders() {
        return numberOrders;
    }

    public void setNumberOrders(int numberOrders) {
        this.numberOrders = numberOrders;
    }
}
