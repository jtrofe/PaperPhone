package com.example.jtrofe200.myapplication.onlineObjects;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jtrofe200.myapplication.Drawing.DrawingView;
import com.example.jtrofe200.myapplication.R;
import com.example.jtrofe200.myapplication.onlineObjects.customDialogs.SaveDialog;
import com.example.jtrofe200.myapplication.onlineObjects.customDialogs.SaveInterface;
import com.example.jtrofe200.myapplication.onlineObjects.customDialogs.WriteSentenceDialog;
import com.example.jtrofe200.myapplication.sentenceGenerator.RandomSentenceGenerator;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class NewGameActivity extends Activity implements SaveInterface {


    private Button sen0;
    private Button sen1;
    private Button sen2;
    private Button cust;


    private DrawingView mDrawing;
    private TextView mCaption;
    private Button mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        //--Get sentence views and put in random sentences
        RandomSentenceGenerator rnd = new RandomSentenceGenerator(this);
        sen0 = (Button) findViewById(R.id.btn_sentence_0);
        sen1 = (Button) findViewById(R.id.btn_sentence_1);
        sen2 = (Button) findViewById(R.id.btn_sentence_2);
        cust = (Button) findViewById(R.id.btn_custom_sentence);

        sen0.setText(rnd.GetSentence());
        sen1.setText(rnd.GetSentence());
        sen2.setText(rnd.GetSentence());

        //--Get views
        mCaption = (TextView) findViewById(R.id.txt_chosen_sentence);
        mDrawing = (DrawingView) findViewById(R.id.drawing_view);
        mSubmitButton = (Button) findViewById(R.id.button_submit_game);


        //--Add listeners
        sen0.setOnClickListener(OnSentenceClick);
        sen1.setOnClickListener(OnSentenceClick);
        sen2.setOnClickListener(OnSentenceClick);
        cust.setOnClickListener(OnSentenceClick);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSubmitClick();
            }
        });
    }


    private void OnSubmitClick(){
        final SaveDialog dialog = new SaveDialog(this);

        ParseUser user = ParseUser.getCurrentUser();
        String caption = mCaption.getText().toString();
        Bitmap drawing = mDrawing.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        drawing.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();

        ParseFile imgFile = new ParseFile("drawing.png", data);



        ParseObject parsePaper = new ParseObject("ParsePaper");
        parsePaper.put("completed", false);
        parsePaper.put("user_0", user);
        parsePaper.put("lastUser", user);
        parsePaper.put("secondLastUser", user);
        parsePaper.put("part_0_caption", caption);
        parsePaper.put("part_0_drawing", imgFile);
        parsePaper.put("last_user", user);
        parsePaper.put("language", "en");//TODO make language reflect phone settings
        parsePaper.put("inUse", false);
        parsePaper.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.OnSaveDone(e);
            }
        });

        dialog.show();
    }

    public void OnSaveDone(boolean success){
        if(success){
            Intent intent = new Intent(this, OnlineActivity.class);
            startActivity(intent);
        }
    }

    private View.OnClickListener OnSentenceClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch(v.getTag().toString()){
                case "rndSentence":
                    OnChoose(((Button) v).getText().toString());
                    break;
                case "customSentence":
                    new WriteSentenceDialog(NewGameActivity.this).show();
                    break;
            }
        }
    };

    public void OnChoose(String caption){
        mCaption.setText(caption);
        sen0.setVisibility(View.GONE);
        sen1.setVisibility(View.GONE);
        sen2.setVisibility(View.GONE);
        cust.setVisibility(View.GONE);

        mCaption.setVisibility(View.VISIBLE);
        mDrawing.setVisibility(View.VISIBLE);
        mSubmitButton.setVisibility(View.VISIBLE);
    }
}