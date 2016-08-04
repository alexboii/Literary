package com.example.alex.literary.mainactivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

/**
 * Created by Alex on 8/3/2016.
 */
public class MyDBHandler extends SQLiteOpenHelper implements Constants {


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_BOOKS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BOOK_TITLE + " TEXT " + ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS +  ";");
        onCreate(db);

    }

    public void addBook(Book book){
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_TITLE, book.getBookTitle());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_BOOKS, null, values);
        db.close();
    }

    public void deleteBook(String bookTitle){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOK_TITLE + "=\"" + bookTitle + "\";");
    }

//    public Cursor getAllRows() {
//        String where = null;
//        Cursor c = 	db.query(true, TABLE_BOOKS, ALL_KEYS,
//                where, null, null, null, null, null);
//        if (c != null) {
//            c.moveToFirst();
//        }
//        return c;
//    }

    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM" + TABLE_BOOKS + "WHERE" + COLUMN_ID + "> -1;");
    }

    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKS + " WHERE 1";

        //Cursor points to a location in your results
        Cursor recordSet = db.rawQuery(query, null);
        //Move to the first row in your results
        recordSet.moveToFirst();

        //Position after the last row means the end of the results
        while (!recordSet.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex("booktitle")) != null) {
                dbString += recordSet.getString(recordSet.getColumnIndex("booktitle"));
                dbString += "\n";
            }
            recordSet.moveToNext();
        }
        db.close();

        return dbString;
    }
}
