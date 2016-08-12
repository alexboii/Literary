package com.example.alex.literary.mainactivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView;

import com.example.alex.literary.R;

import java.util.ArrayList;
import java.util.List;

public class WordManagement extends AppCompatActivity implements Constants {

    Button addBtn;
    EditText wordField;
    ListView wordList;
    MyDBHandler dbHandler;
    SQLiteDatabase newDB;
    public List<myBook> myBooks;
    public List<String> bookTitles;
    public ArrayAdapter wordListAdapter;
    public boolean isDelete;


    // TODO: Edit class myBook to have database properties
    // TODO: Create myBook object from Database that will be passed.

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


        myBooks = new ArrayList<myBook>();
        bookTitles = new ArrayList<String>();

        String hello = "";
        Log.d(hello, "I'm here");

        wordListAdapter = new CustomListAdapter(getApplicationContext(), R.layout.custom_list, bookTitles);

        wordList.setAdapter(wordListAdapter);

        String[] argument = new String[]{"The Birth of Tragedy"};

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
                else{

                    Intent intent = new Intent(view.getContext(), BookSwipe.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("data", item);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }


            }
        });

        openAndQueryDatabase();

        // USE SIMPLE COURSE ADAPTER

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputWord = wordField.getText().toString();
                myBook myBook = new myBook(inputWord);
                dbHandler.addBook(myBook);
                System.out.println(dbHandler.databaseToString());
                bookTitles.add(inputWord);
                wordListAdapter.notifyDataSetChanged();


            }
        });


    }

    public void deleteBtnClicked(View view) {

        System.out.println(isDelete);


        if(isDelete == false){
            isDelete = true;
        }else{
            isDelete = false;
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
                        String id = c.getString(c.getColumnIndex("id"));
                        System.out.println(id);
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