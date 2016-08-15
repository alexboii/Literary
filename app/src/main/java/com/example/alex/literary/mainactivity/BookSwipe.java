package com.example.alex.literary.mainactivity;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.alex.literary.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class BookSwipe extends AppCompatActivity implements Constants{
    private TextView tvTitle;
    private TextView tvAuthor;
    private WebView wbDescription;
    private ImageView ivCover;

    private MyDBHandler dbHandler;
    private SQLiteDatabase newDB;

//    public Cursor cursor;

    CustomAdapter adapter;

    String bookTitle;

    public void startActivity(Intent intent) {
        // check if search intent
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.putExtra("KEY", bookTitle);
        }

        super.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TabLayout tabLayout;
        final ViewPager viewPager;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_swipe);


        Bundle bundle = getIntent().getExtras();

        bookTitle = bundle.getString("data");

        System.out.println(bookTitle);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        adapter = new CustomAdapter(getSupportFragmentManager(), getActionBar());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

//        adapter.notifyDataSetChanged();


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
//                viewPager.setAdapter(adapter);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
//                viewPager.setAdapter(adapter);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
//                viewPager.setAdapter(adapter);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search2).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search2:
//                SearchManager searchManager =
//                        (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//                SearchView searchView =
//                        (SearchView) menu.findItem(R.id.search2).getActionView();
//                searchView.setSearchableInfo(
//                        searchManager.getSearchableInfo(getComponentName()));
                return true;
            case R.id.camera:
                Intent intent = new Intent(this, CameraActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("data", getJSONQuotes());
                bundle.putString("booktitle", getBookTitle());
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getJSONQuotes() {

        String JSONString = null;

        dbHandler = new MyDBHandler(this.getApplicationContext(), null, null, 1);
        newDB = dbHandler.getWritableDatabase();

        String query = "SELECT " + COLUMN_QUOTES + " FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOK_TITLE + "=\"" + bookTitle + "\"";
        Cursor cursor = newDB.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() != true) {

                JSONString = cursor.getString(cursor.getColumnIndexOrThrow("quotes"));

                System.out.println("HELLO: " + JSONString);

                if (JSONString == null) {

                    JSONObject obj = new JSONObject();
                    String firstJSON = obj.toString();
                    dbHandler.addQuotes(bookTitle, firstJSON);
                    JSONString = firstJSON;

                    System.out.println(firstJSON);

                }

                break;
            }
        }
        else{
            return null;
        }

        return JSONString;
    }


    private class CustomAdapter extends FragmentStatePagerAdapter {

        private String[] fragments = {"Information", "Words", "Quotes"};

        public CustomAdapter(FragmentManager supportFragmentManager, ActionBar actionBar) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new BookProfileFragment(bookTitle);
                case 1:
                    return new BookWordsFragment(bookTitle);
                case 2:
                    return new BookQuotesFragment(bookTitle);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
