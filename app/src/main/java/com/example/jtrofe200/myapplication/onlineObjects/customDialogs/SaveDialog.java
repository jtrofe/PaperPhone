package com.example.jtrofe200.myapplication.onlineObjects.customDialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.example.jtrofe200.myapplication.R;
import com.parse.ParseException;

public class SaveDialog extends Dialog{

    private Context mContext;
    private SaveInterface mSaveInterface;

    public SaveDialog(Context context){
        super(context);
        setContentView(R.layout.dialog_save);

        setTitle("Saving");

        mContext = context;
        mSaveInterface = (SaveInterface) mContext;
    }

    public void OnSaveDone(ParseException e){
        //TODO handle saving errors
        boolean success;
        if(e == null){
            System.out.println("Object saved!");
            //Save success
            Toast.makeText(mContext, "Saved!", Toast.LENGTH_SHORT).show();
            success = true;
        }else{
            //Save failure
            System.out.println("Object not saved!");
            Toast.makeText(mContext, "Not saved!", Toast.LENGTH_SHORT).show();
            success = false;
        }
        this.dismiss();
        mSaveInterface.OnSaveDone(success);
    }
}
