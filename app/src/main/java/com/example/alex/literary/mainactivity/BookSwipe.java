package com.example.alex.literary.mainactivity;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

import cz.msebera.android.httpclient.Header;



public class BookSwipe extends AppCompatActivity {
    TextView tvTitle;
    TextView tvAuthor;
    WebView wbDescription;
    ImageView ivCover;

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
//        sampleCode(bookTitle);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getActionBar()));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }


    private class CustomAdapter extends android.support.v4.app.FragmentPagerAdapter {

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
                    return new BookWordsFragment();
                case 2:
                    return new BookQuotesFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }
}
