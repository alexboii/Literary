package com.example.alex.literary.mainactivity;

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

import com.example.alex.literary.R;
import com.example.alex.literary.dictionary.English;

import java.util.ArrayList;
import java.util.List;

public class WordManagement extends AppCompatActivity {

    Button addBtn;
    EditText wordField;
    ListView wordList;
    public List<Book> books;
    public List<String> bookTitles;
    public ArrayAdapter wordListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_management);

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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputWord = wordField.getText().toString();

                books.add(new Book(inputWord));

                int index = books.size() - 1;

                System.out.println(index);
                bookTitles.add(books.get(index).getBookTitle());

//                for(int i = 0; i < books.size(); i++){
//
//                    System.out.println("Did I get here?");
//
//                    bookTitles.add(books.get(i).getBookTitle());
//                    System.out.println(books.get(i).getBookTitle());
//
//                }

                wordListAdapter.notifyDataSetChanged();
            }
        });




    }
}
