package com.example.alex.literary.mainactivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public String bookTitle;

    TextView tvTitle;
    TextView tvAuthor;
    WebView wbDescription;
    ImageView ivCover;


    public BookProfileFragment() {
        // Required empty public constructor
    }

    public BookProfileFragment(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_book_profile, container, false);

        System.out.println(bookTitle);
        sampleCode(bookTitle);

        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle2);
        tvAuthor = (TextView) rootView.findViewById(R.id.tvAuthor2);
        ivCover = (ImageView)  rootView.findViewById(R.id.ivCover2);
        wbDescription = (WebView) rootView.findViewById(R.id.wbDescription2);
        wbDescription.setBackgroundColor(Color.TRANSPARENT);

        return rootView;
    }

    public void sampleCode(String newText){




        if(newText.length()>0)
        {
            final String copyNewText = newText;
            newText = newText.replace(" ", "+");
            newText = newText.replace("-", "");
            String url = "https://www.googleapis.com/books/v1/volumes?q=";
            url = url + newText;

            AsyncHttpClient client = new AsyncHttpClient();
            final String finalNewText = newText;
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    java.util.List<myBook> bookList = new ArrayList<myBook>();

                    String json = new String(responseBody);

                    try {
                        JSONObject object = new JSONObject(json);
                        JSONArray array = object.getJSONArray("items");

                        for (int i = 0; i < array.length(); i++) {

                            myBook book = new myBook();
                            JSONObject item = array.getJSONObject(i);

                            JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                            if(volumeInfo.has("title") && volumeInfo.getString("title").equals(copyNewText) && volumeInfo.has("authors") && volumeInfo.has("description") && volumeInfo.has("imageLinks")){

                                System.out.println("Am I here?");

                                String title = volumeInfo.getString("title");
                                book.setBookTitle(title);
                                if(title.equals(copyNewText)){
                                    System.out.println(book.getBookTitle());
                                    tvTitle.setText(book.getBookTitle());}
                                else{
                                    tvTitle.setText(copyNewText);
                                }

                                JSONArray authors = volumeInfo.getJSONArray("authors");
                                String author = authors.getString(0);
                                book.setBookAuthor(author);
                                System.out.println(book.getBookAuthor());
                                tvAuthor.setText("Author: " + book.getBookAuthor());

                                String description = volumeInfo.getString("description");
                                System.out.println(description);

                                String text;
                                text = "<html><body><p align=\"justify\">";
                                text+= description;
                                text+= "</p></body></html>";
                                wbDescription.loadData(text, "text/html", "utf-8");


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


                                break;
                            }else {
                                String title = volumeInfo.getString("title");
                                book.setBookTitle(title);
                                System.out.println(book.getBookTitle());
                                tvTitle.setText(book.getBookTitle());

                                JSONArray authors = volumeInfo.getJSONArray("authors");
                                String author = authors.getString(0);
                                book.setBookAuthor(author);
                                System.out.println(book.getBookAuthor());
                                tvAuthor.setText("Author: " + book.getBookAuthor());


                                if (volumeInfo.has("imageLinks")) {
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
                                } else {
                                    book.setBookImageURL("No URL");
                                }
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
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                }

            });
        }


    }


}
