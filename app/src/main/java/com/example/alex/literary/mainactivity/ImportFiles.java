package com.example.alex.literary.mainactivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.content.Context;


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


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.res.AssetManager;


import edu.mit.jwi.morph.WordnetStemmer;

import android.widget.*;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


import java.net.MalformedURLException;

import static java.security.AccessController.getContext;

/**
 * Created by Alex on 7/29/2016.
 */
public class ImportFiles {

    private Context context;

    public ImportFiles(Context context) {
        this.context = context;
        copyAssets();
    }


    private void copyFiles(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    private void copyAssets() {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        System.out.println("Am I in copyAssets?");
        try {
            files = assetManager.list("dict");
            System.out.println("Am I in copyAssets? 2");

        } catch (IOException e) {
            System.out.println("Am I in second error 2?");
            e.printStackTrace();
        }

        for (String fileName : files) {
            System.out.println("Am I in copyAssets? 4");
            System.out.println("Files: " + fileName);
            InputStream in = null;
            OutputStream out = null;


            try {
                System.out.println("Am I in copyAssets? 3");
                in = assetManager.open("dict/" + fileName);
                String path = Environment.getExternalStorageDirectory().toString() + "/" + fileName;
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


}
