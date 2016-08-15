package com.example.alex.literary.mainactivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.alex.literary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private Button takePictureButton;
    private ImageView imageView;
    private Uri file;
    private String directory;
    private boolean isSaved = false, pictureTaken;
    private EditText descriptionText;
    private String bookTitle;
    JSONObject temp;
    private String copyJSON;
    MyDBHandler dbHandler;
    ArrayList<String> arrStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        dbHandler = new MyDBHandler(this.getApplicationContext(), null, null, 1);

        takePictureButton = (Button) findViewById(R.id.button_image);
        imageView = (ImageView) findViewById(R.id.imageview);
        descriptionText = (EditText)findViewById(R.id.descriptionText);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);


        startActivityForResult(intent, 100);

    }


    public void takePicture(View view) {
        isSaved = true;

        JSONObject item = new JSONObject();

        try {
            item.put("path", directory);
            item.put("description", descriptionText.getText().toString());
            arrStr.add(item.toString());
            JSONObject json = new JSONObject();
            json.put("quotes", new JSONArray(arrStr));
            String arrayList = json.toString();
            System.out.println("AM I HERE?" + temp.toString());
            System.out.println("HELOLLLLLL");
            dbHandler.addQuotes(bookTitle, arrayList);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onPause(){
        super.onPause();

        if(!isSaved && pictureTaken){

            System.out.println(directory + " about to get deleted");
            File delete = new File(file.getPath());
            boolean deleted = delete.delete();

        }

        System.out.println(pictureTaken);

    }



    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("CameraDemo", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle bundle = getIntent().getExtras();

        System.out.println("RECEIVED DATA : " + bundle.getString("data"));
        System.out.println(bundle.getString("booktitle"));
        copyJSON = bundle.getString("data");
        bookTitle = bundle.getString("booktitle");

        arrStr = new ArrayList<String>();


        try {
            temp = new JSONObject(copyJSON);
            JSONArray jArray = temp.optJSONArray("quotes");

            if(copyJSON != null ) {
                for (int i = 0; i < jArray.length(); i++) {
                    arrStr.add(jArray.getString(i));
                }

                System.out.println(arrStr.toString());
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }catch(NullPointerException e){
            e.printStackTrace();
        }

//        JSONArray jArray = copyJSON.getJSONArray("");



        directory = file.getPath();

        System.out.println(directory);


        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(file);
                pictureTaken = true;
            }
        }

        if(!pictureTaken){
            finish();
        }
    }
}

