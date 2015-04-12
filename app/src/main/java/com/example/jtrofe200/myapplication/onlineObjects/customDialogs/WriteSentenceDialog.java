package com.example.jtrofe200.myapplication.onlineObjects.customDialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jtrofe200.myapplication.R;
import com.example.jtrofe200.myapplication.onlineObjects.NewGameActivity;

public class WriteSentenceDialog extends Dialog{

    private NewGameActivity mContext;

    private EditText mSentence;

    public WriteSentenceDialog(Context context){
        super(context);
        setContentView(R.layout.dialog_write_sentence);

        setTitle(context.getResources().getString(R.string.txt_dialog_title_enter));

        mContext = (NewGameActivity) context;

        //--Get views
        mSentence = (EditText) findViewById(R.id.edit_text_caption);
        Button okButton = (Button) findViewById(R.id.btn_submit);

        //--Add event listener
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnChosen();
            }
        });
    }

    private void OnChosen(){
        String caption = mSentence.getText().toString().trim();

        if(caption.isEmpty()){
            Toast.makeText((Context) mContext,
                    mContext.getResources().getString(R.string.txt_dialog_enter_sentence),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mContext.OnChoose(caption);
        this.dismiss();
    }
}
