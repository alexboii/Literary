package com.example.alex.literary.mainactivity;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alex.literary.R;
import com.example.alex.literary.dictionary.English;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements Constants {

    TextView dfnDisplay;
    private static String LIST_SEPARATOR = "__,__";
    public String query;
    public String mValue;

    MyDBHandler dbHandler;
    SQLiteDatabase newDB;

    ArrayList stringArray = new ArrayList<>();

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dfnDisplay = (TextView) findViewById(R.id.displayDfn);


        handleIntent(getIntent());


    }

    public void saveBtnAction(View view) {


        stringArray.add(query);
        BookWordsFragment.refreshList(stringArray);
        String sendString = convertListToString(stringArray);
        dbHandler.addWords(mValue, sendString);
//--        ListView list = (ListView) this.findViewById(R.id.savedWordsList);
//--        ((CustomListAdapter)list.getAdapter()).notifyDataSetChanged();

    }


    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        mValue = intent.getStringExtra("KEY");
        System.out.println(mValue);

        dbHandler = new MyDBHandler(this.getApplicationContext(), null, null, 1);
        newDB = dbHandler.getWritableDatabase();

        String query2 = "SELECT " + COLUMN_WORDS + " FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOK_TITLE + "=\"" + mValue + "\"";
        cursor = newDB.rawQuery(query2, null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() != true) {

                String itemname = cursor.getString(cursor.getColumnIndex("words"));
                System.out.println(itemname);
                if(itemname != null){
                    stringArray = (ArrayList) convertStringToList(itemname);
                }

                break;
            }
        }

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            dfnDisplay.setText(English.processDefinition(query));
            dfnDisplay.setMovementMethod(new ScrollingMovementMethod());
        }
    }


    public static String convertListToString(List<String> stringList) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : stringList) {
            stringBuffer.append(str).append(LIST_SEPARATOR);
        }

        int lastIndex = stringBuffer.lastIndexOf(LIST_SEPARATOR);
        stringBuffer.delete(lastIndex, lastIndex + LIST_SEPARATOR.length() + 1);

        return stringBuffer.toString();
    }

    public static List<String> convertStringToList(String str) {

        List<String> list = new ArrayList(Arrays.asList(str.split(LIST_SEPARATOR)));

        return list;
    }


}
