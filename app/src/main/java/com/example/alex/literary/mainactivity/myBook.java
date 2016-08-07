package com.example.alex.literary.mainactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 7/29/2016.
 */
public class myBook {


    public ArrayList<String> words, quotes;
    public String bookTitle;

    public String bookAuthor;
    public String bookImageURL;
    public HashMap<String, String> picture;

    myBook(String bookTitle, ArrayList<String> words, ArrayList<String> quotes, HashMap<String, String> picture) {

        this.words = words;
        this.quotes = quotes;
        this.bookTitle = bookTitle;
        this.picture = picture;

    }

    myBook(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public myBook() {

    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookImageURL() {
        return bookImageURL;
    }

    public void setBookImageURL(String bookImageURL) {
        this.bookImageURL = bookImageURL;
    }


}
