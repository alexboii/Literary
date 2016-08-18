package com.example.alex.literary.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.alex.literary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;


public class BookQuotesFragment extends Fragment implements Constants {


    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    GridView grid;
    static GridViewAdapter adapter;
    File file;

    private MyDBHandler dbHandler;
    private SQLiteDatabase newDB;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public String bookTitle;

//    private OnFragmentInteractionListener mListener;

    public BookQuotesFragment() {
        // Required empty public constructor
    }

    public BookQuotesFragment(String bookTitle){
        this.bookTitle = bookTitle;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_book_quotes, container, false);

//        Button tempBtn = (Button)rootView.findViewById(R.id.tempBtn);
//
//        tempBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), CameraActivity.class);
//                startActivity(intent);
//
//            }
//        });

        // Check for SD Card
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
//            Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
//                    .show();
        } else {
            // Locate the image folder in your SD Card
            file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "SDImageTutorial" + File.separator + bookTitle);
            // Create a new folder if no folder named SDImageTutorial exist
//            if (!file.exists()){
//                if (!file.mkdirs()){
//                    Log.d("CameraDemo", "failed to create directory");
//                    return null;
//                }
//            }

            file.mkdir();
        }

//        if (file.isDirectory()) {
//            listFile = file.listFiles();
//            // Create a String array for FilePathStrings
//            FilePathStrings = new String[listFile.length];
//            // Create a String array for FileNameStrings
//            FileNameStrings = new String[listFile.length];
//
//            for (int i = 0; i < listFile.length; i++) {
//                // Get the path of the image file
//                FilePathStrings[i] = listFile[i].getAbsolutePath();
//                // Get the name image file
//                FileNameStrings[i] = listFile[i].getName();
//            }
//        }

        populateArray();

        // Locate the GridView in gridview_main.xml
        grid = (GridView) rootView.findViewById(R.id.gridview);
        // Pass String arrays to LazyAdapter Class
        adapter = new GridViewAdapter(this.getActivity(), FilePathStrings, FileNameStrings);
        // Set the LazyAdapter to the GridView
//        adapter.notifyDataSetChanged();
        grid.setAdapter(adapter);


        // Capture gridview item click
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(getContext(), ViewImage.class);
                // Pass String arrays FilePathStrings
                i.putExtra("filepath", FilePathStrings);
                // Pass String arrays FileNameStrings
                i.putExtra("filename", FileNameStrings);
                // Pass click position
                i.putExtra("position", position);
                startActivity(i);
            }

        });


        adapter.notifyDataSetChanged();

        return rootView;
    }

    public void tempListener(View view){

        Intent intent = new Intent(view.getContext(), CameraActivity.class);
        startActivity(intent);

    }

    public void populateArray() {

        ArrayList<String> arrayNames = new ArrayList<String>();
        ArrayList<String> arrayPage = new ArrayList<String>();

        try {
            JSONObject object = new JSONObject(getJSONQuotes());
            String temp = object.getString("quotes");
            JSONArray array = new JSONArray(temp);
            for (int i = 0; i < array.length(); i++) {
//
                String temp2 = array.getString(i);
                JSONObject item = new JSONObject(temp2);

                String title = item.getString("path");
                System.out.println(title + "// p. " + item.getString("page"));
                arrayNames.add(item.getString("path"));
                arrayPage.add(item.getString("page"));

            }

//            for(int i = 0; i < arrayNames.length; i++){
//                System.out.println(arrayNames[i] + "//" + arrayPage[i]);
//            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (file.isDirectory()) {
            listFile = file.listFiles();
            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];
            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }

        }

        for (int i = 0; i < listFile.length; i++) {
            System.out.println(FileNameStrings[i]);
            if (arrayNames.get(i) != null && arrayNames.get(i).equals(FileNameStrings[i])) {
                FileNameStrings[i] = "Page " + arrayPage.get(i);
                System.out.println("I'm in the if statement: " + FileNameStrings[i]);
            }
        }
    }

    public void onResume(){
        super.onResume();
        populateArray();
        adapter.setList(FilePathStrings, FileNameStrings);
        adapter.notifyDataSetChanged();
    }

    public String getJSONQuotes() {

        String JSONString = null;

        dbHandler = new MyDBHandler(this.getActivity(), null, null, 1);
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

}
