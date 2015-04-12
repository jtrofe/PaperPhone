package com.example.jtrofe200.myapplication.onlineObjects.customDialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jtrofe200.myapplication.R;
import com.example.jtrofe200.myapplication.onlineObjects.OnlineActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LogInDialog extends Dialog{

    private Context mContext;

    private ProgressBar spinner;

    private TextView logInButton;
    private EditText logInName;
    private EditText logInPass;
    private Button logInSubmitButton;

    private TextView signUpButton;
    private EditText signUpName;
    private EditText signUpPass1;
    private EditText signUpPass2;
    private Button signUpSubmitButton;

    public LogInDialog(Context context){
        super(context);

        mContext = context;

        setContentView(R.layout.dialog_not_signed_in);
        setTitle("You are not signed in!");

        //--Set views
        spinner = (ProgressBar) findViewById(R.id.progress_spinner);

        logInButton = (TextView) findViewById(R.id.txt_log_in);
        logInName = (EditText) findViewById(R.id.edit_text_name);
        logInPass = (EditText) findViewById(R.id.edit_text_pass);
        logInSubmitButton = (Button) findViewById(R.id.btn_log_in);

        signUpButton = (TextView) findViewById(R.id.txt_sign_up);
        signUpName = (EditText) findViewById(R.id.edit_text_name_2);
        signUpPass1 = (EditText) findViewById(R.id.edit_text_pass_2);
        signUpPass2 = (EditText) findViewById(R.id.edit_text_pass_verify);
        signUpSubmitButton = (Button) findViewById(R.id.btn_sign_up);

        //--Set events
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpName.setVisibility(View.GONE);
                signUpPass1.setVisibility(View.GONE);
                signUpPass2.setVisibility(View.GONE);
                signUpSubmitButton.setVisibility(View.GONE);

                logInName.setVisibility(View.VISIBLE);
                logInPass.setVisibility(View.VISIBLE);
                logInSubmitButton.setVisibility(View.VISIBLE);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInName.setVisibility(View.GONE);
                logInPass.setVisibility(View.GONE);
                logInSubmitButton.setVisibility(View.GONE);

                signUpName.setVisibility(View.VISIBLE);
                signUpPass1.setVisibility(View.VISIBLE);
                signUpPass2.setVisibility(View.VISIBLE);
                signUpSubmitButton.setVisibility(View.VISIBLE);
            }
        });

        logInSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogIn();
            }
        });

        signUpSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }

    private void ShowSpinner(){
        logInButton.setVisibility(View.GONE);
        logInName.setVisibility(View.GONE);
        logInPass.setVisibility(View.GONE);
        logInSubmitButton.setVisibility(View.GONE);

        signUpButton.setVisibility(View.GONE);
        signUpName.setVisibility(View.GONE);
        signUpPass1.setVisibility(View.GONE);
        signUpPass2.setVisibility(View.GONE);
        signUpSubmitButton.setVisibility(View.GONE);

        spinner.setVisibility(View.VISIBLE);
    }

    private void HideSpinner(boolean loggingIn){
        if(loggingIn) {
            logInButton.setVisibility(View.VISIBLE);
            logInName.setVisibility(View.VISIBLE);
            logInPass.setVisibility(View.VISIBLE);
            logInSubmitButton.setVisibility(View.VISIBLE);

            signUpButton.setVisibility(View.VISIBLE);
            signUpName.setVisibility(View.GONE);
            signUpPass1.setVisibility(View.GONE);
            signUpPass2.setVisibility(View.GONE);
            signUpSubmitButton.setVisibility(View.GONE);
        }else{
            logInButton.setVisibility(View.VISIBLE);
            logInName.setVisibility(View.GONE);
            logInPass.setVisibility(View.GONE);
            logInSubmitButton.setVisibility(View.GONE);

            signUpButton.setVisibility(View.VISIBLE);
            signUpName.setVisibility(View.VISIBLE);
            signUpPass1.setVisibility(View.VISIBLE);
            signUpPass2.setVisibility(View.VISIBLE);
            signUpSubmitButton.setVisibility(View.VISIBLE);
        }

        spinner.setVisibility(View.GONE);
    }

    private void LogIn(){
        ShowSpinner();
        //--User names are stored lowercase to prevent two people with the same
        //---name but different capitalization
        String name = logInName.getText().toString().trim().toLowerCase();
        String pass = logInPass.getText().toString();

        if(name.isEmpty() || pass.isEmpty()){
            Toast.makeText(mContext, "Enter a name and password", Toast.LENGTH_SHORT).show();
            HideSpinner(true);
            return;
        }

        ParseUser.logInInBackground(name, pass, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                OnLogInDone(e);
            }
        });
    }

    private void OnLogInDone(ParseException e){
        if(e == null){
            this.dismiss();
            Intent intent = new Intent(mContext, OnlineActivity.class);
            mContext.startActivity(intent);
        }else{
            //TODO read error and tell user why they weren't signed up
            String errMsg = "Error logging in: " + e.getCode();
            if(e.getCode() == ParseException.CONNECTION_FAILED){
                errMsg = "Check internet connection";
            }
            Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            HideSpinner(true);
        }
    }

    private void SignUp(){
        ShowSpinner();
        //--User names are stored lowercase to prevent two people with the same
        //---name but different capitalization
        String name = signUpName.getText().toString().trim().toLowerCase();
        String pass = signUpPass1.getText().toString();
        String passVerify = signUpPass2.getText().toString();

        if(!pass.equals(passVerify)){
            Toast.makeText(mContext, "Passwords do not match", Toast.LENGTH_SHORT).show();
            HideSpinner(false);
            return;
        }
        if(name.isEmpty() || pass.isEmpty()){
            Toast.makeText(mContext, "Enter a name and password", Toast.LENGTH_SHORT).show();
            HideSpinner(false);
            return;
        }

        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(pass);

        user.signUpInBackground(new SignUpCallback(){
            @Override
            public void done(ParseException e){
                OnSignUpDone(e);
            }
        });
    }

    private void OnSignUpDone(ParseException e){
        if(e == null){
            this.dismiss();
            //No errors, sign up worked
            Toast.makeText(mContext, "Signed up successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, OnlineActivity.class);
            mContext.startActivity(intent);
        }else{
            //TODO read error and tell user why they weren't signed up
            String errMsg = "Error signing up: " + e.getCode();
            if(e.getCode() == ParseException.USERNAME_TAKEN){
                errMsg = "User name is taken";
            }else if(e.getCode() == ParseException.CONNECTION_FAILED){
                errMsg = "Check internet connection";
            }
            Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();

            HideSpinner(true);
        }
    }
}