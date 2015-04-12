package com.example.jtrofe200.myapplication.onlineObjects;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jtrofe200.myapplication.Drawing.DrawingView;
import com.example.jtrofe200.myapplication.R;
import com.example.jtrofe200.myapplication.onlineObjects.customDialogs.LoadingDialog;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class AddOnActivity extends Activity {
    private String AddType;
    public static final String DIVE_IN = "dive";

    //----------------//
    //---UI Objects---//
    //----------------//
    private LoadingDialog mDialog;
    private TextView mStaticCaption;
    private DrawingView mDynamicDrawing;
    private ImageView mStaticDrawing;
    private EditText mDynamicCaption;
    private Button mSubmitButton;

    //--------------------//
    //---Game variables---//
    //--------------------//
    private String objectID;
    private String partNum;
    private String partType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_on);

        //--Get views
        mStaticCaption = (TextView) findViewById(R.id.txt_received_caption);
        mDynamicDrawing = (DrawingView) findViewById(R.id.img_sent_drawing);

        mStaticDrawing = (ImageView) findViewById(R.id.img_received_drawing);
        mDynamicCaption = (EditText) findViewById(R.id.txt_sent_caption);

        mSubmitButton = (Button) findViewById(R.id.btn_submit);

        //--Load paper based on type
        AddType = getIntent().getStringExtra("addType");
        LoadPaper(AddType);

        //--Add event listeners
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSubmitClick();
            }
        });
    }

    //---------------------//
    //---EVENT LISTENERS---//
    //---------------------//
    private void OnSubmitClick(){
        mSubmitButton.setVisibility(View.INVISIBLE);
        switch(AddType){
            case DIVE_IN:
                final HashMap<String, Object> params = new HashMap<>();
                params.put("userID", ParseUser.getCurrentUser().getObjectId());
                params.put("paperID", objectID);
                params.put("partNum", partNum);
                params.put("partType", partType);

                if(partType.equals("caption")){
                    params.put("caption", mDynamicCaption.getText().toString());
                    mDialog.ShowTitle("Saving");
                    SavePaper(params);
                }else{
                    Bitmap drawing = mDynamicDrawing.getDrawingCache();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    drawing.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] data = stream.toByteArray();

                    final ParseFile imgFile = new ParseFile("drawing.png", data);
                    mDialog.ShowTitle("Saving");

                    imgFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                params.put("imgFile", imgFile);
                                SavePaper(params);
                            }
                        }
                    });
                }
                break;
        }
    }


    //-------------------//
    //---Saving papers---//
    //-------------------//
    private void SavePaper(HashMap<String, Object> params){
        ParseCloud.callFunctionInBackground("AddToPaper", params,
                new FunctionCallback<Object>() {
                    @Override
                    public void done(Object o, ParseException e) {
                        OnSaveDone(e);
                    }
                });
    }

    private void OnSaveDone(ParseException e){
        mDialog.dismiss();
        if(e == null){
            Toast.makeText(this, "Saved successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, OnlineActivity.class);
            startActivity(intent);
        }else{
            //TODO Handle exceptions
        }
    }

    //--------------------//
    //---Loading papers---//
    //--------------------//
    private void LoadPaper(String addType){
        switch(addType){
            case DIVE_IN:
                mDialog = new LoadingDialog(this, "Loading random game...");
                HashMap<String, Object> params = new HashMap<>();
                params.put("userID", ParseUser.getCurrentUser().getObjectId());
                //TODO for deployment change this to true
                params.put("strangersOnly", false);

                ParseCloud.callFunctionInBackground("GetPaper", params,
                        new FunctionCallback<Map<String, Object>>() {
                            @Override
                            public void done(Map<String, Object> o, ParseException e) {
                                OnRandomLoaded(o, e);
                            }
                        });
                mDialog.ShowTitle("Loading");
                break;
        }
    }

    private void OnRandomLoaded(Map<String, Object> mapObject, ParseException e){
        if(e == null){
            String msg = mapObject.get("msg").toString();
            if(!msg.isEmpty()){
                //TODO handle these errors
                System.out.println("Error " + mapObject.get("msg").toString());
                if(msg.equals("No results found")){
                    Toast.makeText(this, "No available games found", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, OnlineActivity.class));
                }
            }else {
                objectID = mapObject.get("id").toString();
                partNum = mapObject.get("partNum").toString();
                partType = mapObject.get("partType").toString();
                System.out.println("Got object " + objectID);
                String cap = mapObject.get("caption").toString();
                String url = mapObject.get("fileURL").toString();



                if(cap.isEmpty()){
                    new LoadImageFromURL().execute(url);
                }else{
                    OnReceiveCaption(cap);
                }
            }
        }else{
            if(e.getCode() == ParseException.OTHER_CAUSE) {
                System.out.println("Error other");
            }else if(e.getCode() == ParseException.CONNECTION_FAILED) {
                System.out.println("Connection failed");
            }else{
                System.out.println("Some error " + e.getCode());
            }
        }
    }

    private class LoadImageFromURL extends AsyncTask<String, String, Bitmap> {
        protected Bitmap doInBackground(String... args){
            Bitmap bitmap = null;
            try{
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            }catch(Exception e){}
            return bitmap;
        }

        protected void onPostExecute(Bitmap image){
            if(image != null){
                OnReceiveDrawing(image);
            }else{
                OnReceiveDrawingFail();
            }
        }
    }



    private void OnReceiveCaption(String caption){
        mStaticCaption.setText(caption);

        mStaticCaption.setVisibility(View.VISIBLE);
        mDynamicDrawing.setVisibility(View.VISIBLE);
        mSubmitButton.setVisibility(View.VISIBLE);
        mDialog.dismiss();
    }

    private void OnReceiveDrawing(Bitmap drawing){
        mStaticDrawing.setImageBitmap(drawing);

        mStaticDrawing.setVisibility(View.VISIBLE);
        mDynamicCaption.setVisibility(View.VISIBLE);
        mSubmitButton.setVisibility(View.VISIBLE);
        mDialog.dismiss();
    }

    //----------//
    //--ERRORS--//
    //----------//
    private void OnReceiveCaptionFail(){}
    private void OnReceiveDrawingFail(){}
}
