package com.example.jtrofe200.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jtrofe200.myapplication.onlineObjects.customDialogs.LogInDialog;
import com.example.jtrofe200.myapplication.onlineObjects.OnlineActivity;
import com.parse.Parse;
import com.parse.ParseUser;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "gxQ7HxEA3jsT8SUloDUDYUUu6f7F5O6gweEn28QJ", "xftGUNqO167I1hItxHW8kQZoEvEGMsiHpDSgeuJh");

        //--Get views
        Button onlineButton = (Button) findViewById(R.id.btn_online);

        //--Set event listeners
        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnlineClick();
            }
        });
    }

    private void OnlineClick(){
        if(ParseUser.getCurrentUser() == null){
            new LogInDialog(this).show();
            //TODO handle all log in/sign up errors
        }else{
            Intent intent = new Intent(this, OnlineActivity.class);
            startActivity(intent);
        }
    }


    /*

    private void GetGamePaper(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParsePaper");
        query.whereEqualTo("user_0", mUser);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null){
                    System.out.println(parseObjects.size() + " objects found");

                    ParseObject obj = parseObjects.get(0);

                    ParseFile imgFile = obj.getParseFile("drawing_0");
                    String caption = obj.getString("caption_0");

                    byte[] data = null;
                    try {
                        data = imgFile.getData();
                    }catch(Exception ex){

                    }

                    if(data != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        System.out.println("Bitmap loaded");

                        imgView.setImageBitmap(bitmap);
                        imgCaption.setText(caption);
                    }

                    //Found!
                }else{
                    System.out.println("Query error");
                }
            }
        });
    }

    private void SaveGamePaper(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();

        ParseFile imgFile = new ParseFile("drawing.png", data);



        ParseObject parsePaper = new ParseObject("ParsePaper");
        parsePaper.put("user_0", mUser);
        parsePaper.put("caption_0", "Hello");
        parsePaper.put("drawing_0", imgFile);
        parsePaper.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    System.out.println("Object saved!");
                    //Save success
                }else{
                    //Save failure
                    System.out.println("Object not saved!");
                }
            }
        });
    }


    private class LoadImage extends AsyncTask<String, String, Bitmap>{
        protected Bitmap doInBackground(String... args){
            Bitmap bitmap = null;
            try{
                System.out.println("Loading image");
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            }catch(Exception e){}
            return bitmap;
        }

        protected void onPostExecute(Bitmap image){
            if(image != null){
                System.out.println("Image loaded");
                imgView.setImageBitmap(image);
            }else{
                System.out.println("Image null");
            }
        }
    }*/
}