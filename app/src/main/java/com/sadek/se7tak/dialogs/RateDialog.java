package com.sadek.se7tak.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hsalf.smilerating.SmileRating;
import com.sadek.se7tak.R;

public class RateDialog extends Dialog {
    rateServiceAction rateServiceAction;
    EditText commentInput;
    SmileRating smileRating;
    Button done,back;


    public RateDialog(@NonNull final Context context, final rateServiceAction rateServiceAction) {
        super(context);
        setContentView(R.layout.rate_dialog);
        this.rateServiceAction=rateServiceAction;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        commentInput=findViewById(R.id.code_txt_input);
        done=findViewById(R.id.apply_btn);
        back=findViewById(R.id.back_btn);
        smileRating =  findViewById(R.id.smile_rating);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commentInput.getText().toString().isEmpty()){
                    commentInput.setError(context.getString(R.string.codeisrquerd));
                }else {
                    rateServiceAction.onGetRate(commentInput.getText().toString(), smileRating.getRating());
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }



    public interface rateServiceAction{
        void onGetRate(String code, float rating);

    }
}