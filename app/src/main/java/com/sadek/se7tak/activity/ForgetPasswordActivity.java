package com.sadek.se7tak.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.sadek.se7tak.R;
import com.sadek.se7tak.firebase.FireAuth;
import com.sadek.se7tak.firebase.FireDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPasswordActivity extends BaseActivity {

    @BindView(R.id.email_input)
    EditText email_input;


    FireAuth auth;
    FireDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        auth = new FireAuth(this);
        database = new FireDatabase(this);

    }

    @OnClick(R.id.btnForgetPass)
    void btnForgetPass(View view) {
        if (!validate()) {
            return;
        }
        auth.forgetPassword(email_input.getText().toString());
    }


    private Boolean validate() {
        email_input.setError(null);

        String email = email_input.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_input.setError(getString(R.string.enter_valid_email));
            return false;
        }
        return true;

    }

}
