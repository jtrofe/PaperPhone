package com.example.jtrofe200.myapplication.onlineObjects;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.example.jtrofe200.myapplication.MainActivity;
import com.example.jtrofe200.myapplication.R;
import com.parse.ParseUser;


public class OnlineActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        //--Set title
        String uName = ParseUser.getCurrentUser().getUsername();
        uName = uName.substring(0, 1).toUpperCase() + uName.substring(1);
        getSupportActionBar().setSubtitle("Hello " + uName);

        //--Get all of the views
        Button profileButton = (Button) findViewById(R.id.btn_profile);
        Button newGameButton = (Button) findViewById(R.id.btn_new);
        Button diveButton = (Button) findViewById(R.id.btn_dive);
        Button logOutButton = (Button) findViewById(R.id.btn_log_out);



        //--Set event listeners
        profileButton.setOnClickListener(mListener);
        newGameButton.setOnClickListener(mListener);
        diveButton.setOnClickListener(mListener);
        logOutButton.setOnClickListener(mListener);
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getTag().toString()){
                case "profile":
                    OpenProfile();
                    break;
                case "new":
                    NewGame();
                    break;
                case "dive":
                    DiveIn();
                    break;
                case "logOut":
                    LogOut();
                    break;
            }
        }
    };

    private void OpenProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("username", ParseUser.getCurrentUser().getUsername());
        intent.putExtra("userID", ParseUser.getCurrentUser().getObjectId());
        startActivity(intent);
    }

    private void NewGame(){
        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);
    }

    private void DiveIn(){
        Intent intent = new Intent(this, AddOnActivity.class);
        intent.putExtra("addType", AddOnActivity.DIVE_IN);
        startActivity(intent);
    }

    private void LogOut(){
        ParseUser.logOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}