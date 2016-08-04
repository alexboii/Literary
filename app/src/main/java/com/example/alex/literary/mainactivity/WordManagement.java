package com.example.alex.literary.mainactivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView;

import com.example.alex.literary.R;
import com.example.alex.literary.dictionary.English;

import java.util.ArrayList;
import java.util.List;

public class WordManagement extends AppCompatActivity implements Constants {

    Button addBtn;
    EditText wordField;
    ListView wordList;
    MyDBHandler dbHandler;
    SQLiteDatabase newDB;
    public List<Book> books;
    public List<String> bookTitles;
    public ArrayAdapter wordListAdapter;
    public boolean isDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_management);

        isDelete = false;

        dbHandler = new MyDBHandler(this.getApplicationContext(), null, null, 1);

        wordField = (EditText) findViewById(R.id.wordField);

        addBtn = (Button) findViewById(R.id.addBtn);
        wordList = (ListView) findViewById(R.id.wordList);
        wordField = (EditText) findViewById(R.id.wordField);


        books = new ArrayList<Book>();
        bookTitles = new ArrayList<String>();

        String hello = "";
        Log.d(hello, "I'm here");

        wordListAdapter = new CustomListAdapter(getApplicationContext(), R.layout.custom_list, bookTitles);

        wordList.setAdapter(wordListAdapter);

        wordList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                String item = (String) wordList.getItemAtPosition(position);
                System.out.println(item + "//" + isDelete);



                if(isDelete){

                    dbHandler.deleteBook(item);

                    for ( int i = 0;  i < bookTitles.size(); i++){
                        String tempName = bookTitles.get(i);
                        if(tempName.equals(item)){
                            bookTitles.remove(i);
                            wordListAdapter.notifyDataSetChanged();
                        }
                    }

                }


            }
        });

        openAndQueryDatabase();

        // USE SIMPLE COURSE ADAPTER

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputWord = wordField.getText().toString();
                Book book = new Book(inputWord);
                dbHandler.addBook(book);
                System.out.println(dbHandler.databaseToString());
                bookTitles.add(inputWord);
                wordListAdapter.notifyDataSetChanged();


            }
        });


    }

    public void deleteBtnClicked(View view) {

        System.out.println("Am I here?");

        if (isDelete == true) {
            isDelete = false;
        }
        if(isDelete == false){
            isDelete = true;
        }

    }

    private void openAndQueryDatabase() {
        try {

            newDB = dbHandler.getWritableDatabase();

            String query = "SELECT * FROM " + TABLE_BOOKS + " WHERE 1";

            Cursor c = newDB.rawQuery(query, null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String bookTitle = c.getString(c.getColumnIndex("booktitle"));
                        bookTitles.add(bookTitle);
                        wordListAdapter.notifyDataSetChanged();
                    } while (c.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }

    }


}