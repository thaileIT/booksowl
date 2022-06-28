package com.example.bookproject.model;

import java.io.Serializable;

public class Book implements Serializable {
    private String bookName;
    private String author;
    private String price;
    private int imgBook;

    public Book(String bookName, String author, String price, int imgBook) {
        this.bookName = bookName;
        this.author = author;
        this.price = price;
        this.imgBook = imgBook;
    }

    public Book() {
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

    public int getImgBook() {
        return imgBook;
    }

    public void setImgBook(int imgBook) {
        this.imgBook = imgBook;
    }

}
