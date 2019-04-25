package com.sadek.se7tak.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sadek.se7tak.R;

public class CompleteSocialLoginDialog extends Dialog {


    EditText phone_input;
    RadioGroup gender_RB;
    Button btnSignup;

    public CompleteSocialLoginDialog(@NonNull Context context, final DialogInterAction dialogInterAction) {
        super(context);
        setContentView(R.layout.complete_social_dialog);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        phone_input = findViewById(R.id.phone_input);
        gender_RB = findViewById(R.id.gender_RB);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phone_input.getText().toString();
                if (!validate(context,phone)) {
                    return;
                }

                String gender;
                if (gender_RB.getCheckedRadioButtonId() == R.id.maleRB)
                    gender = context.getString(R.string.male);
                else
                    gender = context.getString(R.string.female);

                dialogInterAction.onSubmit(phone, gender);
            }
        });

        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialogInterAction.onDismissed();
            }
        });
    }

    private boolean validate(Context context, String phone) {
        phone_input.setError(null);
        if (phone.isEmpty()) {
            phone_input.setError(context.getString(R.string.enter_valid_phone));
            return false;
        }else if (gender_RB.getCheckedRadioButtonId() == -1) {
            Toast.makeText(context, R.string.chosse_gender, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public interface DialogInterAction {
        void onDismissed();
        void onSubmit(String phone, String gender);
    }
}