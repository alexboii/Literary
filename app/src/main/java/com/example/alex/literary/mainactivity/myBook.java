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
    public HashMap <String, String> picture;

        myBook(String bookTitle, ArrayList<String> words, ArrayList<String> quotes, HashMap <String, String> picture){

            words = this.words;
            quotes = this.quotes;
            bookTitle = this.bookTitle;
            picture = this.picture;

        }
        myBook(String bookTitle){

            this.bookTitle = bookTitle;

        }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }




}
