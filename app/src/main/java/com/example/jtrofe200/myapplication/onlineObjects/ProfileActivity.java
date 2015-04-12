package com.example.jtrofe200.myapplication.onlineObjects;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jtrofe200.myapplication.R;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProfileActivity extends Activity{
    private String mName;
    private String mUserID;

    private TextView mInProgressText;
    private TextView mCompletedText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mName = getIntent().getStringExtra("username");
        mUserID = getIntent().getStringExtra("userID");

        TextView nameHeader = (TextView) findViewById(R.id.text_view_name_header);
        nameHeader.setText(mName);

        mInProgressText = (TextView) findViewById(R.id.text_in_progress);
        mCompletedText = (TextView) findViewById(R.id.text_completed);

        LoadProfileGames();
    }

    private void LoadProfileGames(){
        //--Use cloud code function "GetProfile" to retrieve a list of
        //---Games the specified user has started
        //---Function returns a list of in progress and completed games
        HashMap<String, Object> params = new HashMap<>();
        params.put("userID", mUserID);
        ParseCloud.callFunctionInBackground("GetProfile", params, new FunctionCallback<Map<String, Object>>() {
            @Override
            public void done(Map<String, Object> mapObject, ParseException e){
                boolean isError = mapObject.get("error").toString().equals("true");
                if(isError){

                }else{
                    System.out.println("Getting lists...");
                    ArrayList<HashMap<String, Object>> inProgress = (ArrayList<HashMap<String, Object>>) mapObject.get("inProgress");
                    ArrayList<HashMap<String, Object>> completed = (ArrayList<HashMap<String, Object>>) mapObject.get("completed");

                    String progMsg = "In Progress\n";

                    if(inProgress.size() == 0) progMsg += "None";
                    for(HashMap<String, Object> obj:inProgress){
                        progMsg += obj.get("caption") + "\n";
                    }

                    String compMsg = "Completed\n";

                    if(completed.size() == 0) compMsg += "None";
                    for(HashMap<String, Object> obj:completed){
                        compMsg += obj.get("caption") + "\n";
                    }

                    mInProgressText.setText(progMsg);
                    mCompletedText.setText(compMsg);
                }
            }
        });
    }
}