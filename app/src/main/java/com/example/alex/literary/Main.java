package com.example.alex.literary;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import android.util.Log;
import android.os.Environment;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import android.os.Bundle;
import android.preference.PreferenceActivity;


import edu.mit.jwi.morph.WordnetStemmer;

import android.widget.*;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;



import java.net.MalformedURLException;

public class Main extends AppCompatActivity {

    EditText dfnField;
    static TextView dfnDisplay;
    Button dfnBtn;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dfnField = (EditText) findViewById(R.id.dfnField);
        dfnDisplay = (TextView) findViewById(R.id.dfnDisplay);
        dfnBtn = (Button) findViewById(R.id.dfnBtn);

        verifyStoragePermissions(this);


        dfnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyAssets();
                String hello = "";
                Log.d(hello, "I'm here");
                String inputWord = dfnField.getText().toString();
                processDefinition(inputWord);
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        System.out.println(PackageManager.PERMISSION_GRANTED);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private static Collection getPartsOfSpeech(IDictionary dict, String word) {
        ArrayList<POS> parts = new ArrayList<POS>();
        WordnetStemmer stemmer = new WordnetStemmer(dict);
        // Check every part of speech.
        for (POS pos : POS.values()) {
            // Check every stem, because WordNet doesn't have every surface
            // form in its database.
            for (String stem : stemmer.findStems(word, pos)) {
                IIndexWord iw = dict.getIndexWord(stem, pos);
                if (iw != null) {
                    parts.add(pos);
                }
            }
        }
        return parts;
    }

    private void copyAssets(){
        AssetManager assetManager = getAssets();
        String[] files = null;
        System.out.println("Am I in copyAssets?");
        try{
            files = assetManager.list("dict");
            System.out.println("Am I in copyAssets? 2");

        } catch (IOException e) {
            System.out.println("Am I in second error 2?");
            e.printStackTrace();
        }

//        for(int i = 0; i < files.length; i++){
//            System.out.println(files);
//        }

        for(String fileName : files) {
            System.out.println("Am I in copyAssets? 4");
            System.out.println("Files: " + fileName);
            InputStream in = null;
            OutputStream out = null;


            try {
                System.out.println("Am I in copyAssets? 3");
                in = assetManager.open("dict/" + fileName);
                String path = Environment.getExternalStorageDirectory().toString() + "/" +fileName;
                System.out.println(path);
                out = new FileOutputStream(path);
                copyFiles(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e) {
                System.out.println("Am I in in the error?");
                Log.e("Tag", e.getMessage());

            }

        }
    }

    private void copyFiles(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read=in.read(buffer)) != -1){
            out.write(buffer,0,read);
        }
    }

    private boolean shouldAskPermission(){

        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    private static void processDefinition(String inputWord) {

        String path = "src" + File.separator + "main" + File.separator + "dict";

        URL url = null;

        StringBuffer fpath = new StringBuffer(400);
        fpath.append(Environment.getExternalStorageDirectory().toString());

        System.out.println(fpath);

        File f = new File(fpath.toString());

        try {
            System.out.println("Am I here?");
            url = new URL("file", null, path);
            System.out.println("Am I here? 2");
            IDictionary dict = new Dictionary(f);
            System.out.println("Am I here? 3");
            dict.open();
            System.out.println("Am I here? 4");


            WordnetStemmer stem = new WordnetStemmer(dict);
            System.out.println("test " + stem.findStems(inputWord, null));

            List<String> answer = stem.findStems(inputWord, null);

            if (answer.isEmpty())

            {
                System.out.println("Sorry, we cannot find the word you're looking for!");
            }

            try {

                for (int i = 0; i < answer.size(); i++) {

                    System.out.println("_______________");
                    System.out.println(answer.get(i));

                    boolean verb = getPartsOfSpeech(dict, answer.get(i)).contains(POS.VERB);
                    boolean noun = getPartsOfSpeech(dict, answer.get(i)).contains(POS.NOUN);
                    boolean adjective = getPartsOfSpeech(dict, answer.get(i)).contains(POS.ADJECTIVE);
                    boolean adverb = getPartsOfSpeech(dict, answer.get(i)).contains(POS.ADVERB);

                    System.out.println(verb + " // " + noun + " // " + adjective + " // " +
                            adverb);


                    if (verb == true) {
                        IIndexWord idxWord = dict.getIndexWord(answer.get(i), POS.VERB);
                        System.out.println("Verb:\n");
                        printDefinition(dict, idxWord);
                    }

                    if (noun == true) {
                        IIndexWord idxWord = dict.getIndexWord(answer.get(i), POS.NOUN);
                        System.out.println("Noun:\n");
                        printDefinition(dict, idxWord);
                    }

                    if (adjective == true) {
                        IIndexWord idxWord = dict.getIndexWord(answer.get(i), POS.ADJECTIVE);
                        System.out.println("Adjective:\n");
                        printDefinition(dict, idxWord);
                    }

                    if (adverb == true) {
                        IIndexWord idxWord = dict.getIndexWord(answer.get(i), POS.ADVERB);
                        System.out.println("Adverb\n");
                        printDefinition(dict, idxWord);

                    }


                }

            } catch(IndexOutOfBoundsException e){
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            System.out.println("Completed 2!");
        } catch (IOException e2) {
            e2.printStackTrace();
//            dfnDisplay.setText("woh");
            System.out.println("Completed!");
        }
    }


    private static void printDefinition(IDictionary dict, IIndexWord idxWord) {

        WordnetStemmer stemmer = new WordnetStemmer(dict);


        for (int i = 0; i >= 0; i++) {

            String hello = "";
            Log.d(hello, "I'm here 2");

            try {
                if (idxWord != null) {
                    IWordID wordID = idxWord.getWordIDs().get(i);
                    IWord word = dict.getWord(wordID);
                    dfnDisplay.setText("(" + (i + 1) + ") " + word.getSynset().getGloss() + "\n");
                    System.out.println("(" + (i + 1) + ") " + word.getSynset().getGloss());
                    System.out.println("");
                }
            } catch (NullPointerException e) {
//                dfnDisplay.setText("Sorry, we cannot find the word you're looking for!" + "\n");
                System.out.println("Sorry, we cannot find the word you're looking for!");
                break;
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                break;
            }
            catch(IndexOutOfBoundsException e){
                System.out.println("I'm in this Index");
                e.printStackTrace();
                break;
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.alex.literary/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.alex.literary/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
