package com.example.bookproject.model;

public class BookModel {
    String id,uid,bookName,author,price,categoryID,description,imgUrl;
    long timestamp;

    public BookModel(String id, String uid, String bookName, String author, String price, String categoryID, String description,String imgUrl, long timestamp) {
        this.id = id;
        this.uid = uid;
        this.bookName = bookName;
        this.author = author;
        this.price = price;
        this.categoryID = categoryID;
        this.description = description;
        this.imgUrl = imgUrl;
        this.timestamp = timestamp;
    }

    public BookModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
