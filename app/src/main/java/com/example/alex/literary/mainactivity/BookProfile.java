package com.example.alex.literary.mainactivity;




import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.Books.Volumes.List;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;

import com.example.alex.literary.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookProfile extends AppCompatActivity {

    public JSONArray bookArray;
    TextView tvTitle;
    TextView tvAuthor;
    ImageView ivCover;
    public String bookTitle;

    BookProfile(String bookTitle){
        this.bookTitle = bookTitle;
    }

    BookProfile(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_profile);





        Bundle bundle = getIntent().getExtras();

        bookTitle = bundle.getString("data");

        System.out.println(bookTitle);





        sampleCode(bookTitle);


    }
    public void displayBook(JSONArray array) throws JSONException {


        for (int i = 0; i < array.length(); i++) {

            myBook book = new myBook();
            JSONObject item = array.getJSONObject(i);

            JSONObject volumeInfo = item.getJSONObject("volumeInfo");
            String title = volumeInfo.getString("title");
            book.setBookTitle(title);
            System.out.println(book.getBookTitle());
            tvTitle.setText(book.getBookTitle());

            JSONArray authors = volumeInfo.getJSONArray("authors");
            String author = authors.getString(0);
            book.setBookAuthor(author);
            System.out.println(book.getBookAuthor());
            tvAuthor.setText(book.getBookAuthor());


            if(volumeInfo.has("imageLinks")) {
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String imageLink = imageLinks.getString("thumbnail");
                book.setBookImageURL(imageLink);
                System.out.println(book.getBookImageURL());

                URL url = null;
                try {
                    url = new URL(book.getBookImageURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivCover.setImageBitmap(bmp);
            }
            else{
                book.setBookImageURL("No URL");
            }

        }

        }

    public void sampleCode(String newText){

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        ivCover = (ImageView) findViewById(R.id.ivCover);

        if(newText.length()>0)
        {
            newText = newText.replace(" ", "+");
            newText = newText.replace("-", "");
            String url = "https://www.googleapis.com/books/v1/volumes?q=";
            url = url + newText;

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                    java.util.List<myBook> bookList = new ArrayList<myBook>();

                    String json = new String(responseBody);

                    try {
                        JSONObject object = new JSONObject(json);
                        JSONArray array = object.getJSONArray("items");

                        for (int i = 0; i < 1; i++) {

                            myBook book = new myBook();
                            JSONObject item = array.getJSONObject(i);

                            JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                            String title = volumeInfo.getString("title");
                            book.setBookTitle(title);
                            System.out.println(book.getBookTitle());
                            tvTitle.setText(book.getBookTitle());

                            JSONArray authors = volumeInfo.getJSONArray("authors");
                            String author = authors.getString(0);
                            book.setBookAuthor(author);
                            System.out.println(book.getBookAuthor());
                            tvAuthor.setText("Author: " + book.getBookAuthor());


                            if(volumeInfo.has("imageLinks")) {
                                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                                String imageLink = imageLinks.getString("thumbnail");
                                book.setBookImageURL(imageLink);
                                System.out.println(book.getBookImageURL());

                                URL url = null;
                                try {
                                    url = new URL(book.getBookImageURL());
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                Bitmap bmp = null;
                                try {
                                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                ivCover.setImageBitmap(bmp);
                            }
                            else{
                                book.setBookImageURL("No URL");
                            }

                        }
//
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    LibraryFragment.showBooksList(getApplicationContext(), bookList);
                }

                @Override
                public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {

                }

            });
        }


    }

    }



