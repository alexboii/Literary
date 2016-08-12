package com.example.alex.literary.mainactivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.alex.literary.R;

import java.util.ArrayList;
import java.util.List;


public class BookWordsFragment extends Fragment implements Constants {

    MyDBHandler dbHandler;
    SQLiteDatabase newDB;
    Cursor cursor;

    public static CustomListAdapter wordListAdapter;
    public ListView wordList;

    public String bookTitle;

    public static List<String> stringArray = new ArrayList<>();



    public BookWordsFragment() {

    }

    public BookWordsFragment(String bookTitle){
        this.bookTitle = bookTitle;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_book_words, container, false);


        wordList = (ListView) rootView.findViewById(R.id.savedWordsList);



        stringArray = populateArray();

        wordListAdapter = new CustomListAdapter(getActivity(), R.layout.custom_list, stringArray);
        wordList.setAdapter(wordListAdapter);


        if(!stringArray.isEmpty()){

        }

        wordListAdapter.notifyDataSetChanged();

        return rootView;
    }


    public List<String> populateArray(){

        List<String> list = new ArrayList();

        dbHandler = new MyDBHandler(this.getActivity(), null, null, 1);
        newDB = dbHandler.getWritableDatabase();

        String query2 = "SELECT " + COLUMN_WORDS + " FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOK_TITLE + "=\"" + bookTitle + "\"";
        cursor = newDB.rawQuery(query2, null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() != true) {

                String itemname = cursor.getString(cursor.getColumnIndex("words"));
                System.out.println("THIS IS : " + itemname);

                if(itemname != null){
                    list = SearchActivity.convertStringToList(itemname);
                    System.out.println(stringArray);
                }

                break;
            }
        }

        return list;
    }

    public static void refreshList(ArrayList stringArrayCopy){
        stringArray = stringArrayCopy;
        wordListAdapter.setList((ArrayList<String>) stringArray);
    }

    @Override
    public void onResume() {
        super.onResume();
        stringArray = populateArray();
        wordListAdapter.setList((ArrayList<String>) stringArray);
    }


}
