package com.example.alex.literary.mainactivity;

/**
 * Created by Alex on 8/3/2016.
 */
public interface Constants {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "books.db";
    public static final String TABLE_BOOKS = "books";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BOOK_TITLE = "booktitle";

    public static final String[] ALL_KEYS = new String[] {COLUMN_ID, COLUMN_BOOK_TITLE};



}
