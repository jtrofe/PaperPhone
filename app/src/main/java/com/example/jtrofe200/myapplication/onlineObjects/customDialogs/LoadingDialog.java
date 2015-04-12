package com.example.jtrofe200.myapplication.onlineObjects.customDialogs;

import android.app.Dialog;
import android.content.Context;

import com.example.jtrofe200.myapplication.R;

public class LoadingDialog extends Dialog{

    public LoadingDialog(Context context, String title){
        super(context);
        setContentView(R.layout.dialog_loading);

        setTitle(title);
    }

    public void ShowTitle(String title){
        setTitle(title);
        this.show();
    }
}
