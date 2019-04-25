package com.sadek.se7tak.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sadek.se7tak.R;
import com.sadek.se7tak.firebase.FireAuth;
import com.sadek.se7tak.firebase.FireDatabase;
import com.sadek.se7tak.model.PatientUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends BaseActivity {

    @BindView(R.id.username_input)
    EditText username_input;
    @BindView(R.id.phone_input)
    EditText phone_input;
    @BindView(R.id.email_input)
    EditText email_input;
    @BindView(R.id.confPassword_input)
    EditText confPassword_input;
    @BindView(R.id.password_input)
    EditText password_input;
    @BindView(R.id.gender_RB)
    RadioGroup gender_RB;


    FireAuth auth;
    FireDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        auth = new FireAuth(this);
        database = new FireDatabase(this);
    }

    @OnClick(R.id.btnSignup)
    void btnSignup(View view) {
        if (!validate()) {
            return;
        }
        PatientUser user = new PatientUser();
        user.setUserName(username_input.getText().toString());
        user.setEmail(email_input.getText().toString());
        user.setPhoneNumber(phone_input.getText().toString());
        user.setPassword(password_input.getText().toString());
        if (gender_RB.getCheckedRadioButtonId() == R.id.maleRB)
            user.setGender(getString(R.string.male));
        else
            user.setGender(getString(R.string.female));
        auth.signUp(user);
    }

    private Boolean validate() {
        email_input.setError(null);
        password_input.setError(null);
        username_input.setError(null);
        confPassword_input.setError(null);
        phone_input.setError(null);

        String email = email_input.getText().toString();
        String userName = username_input.getText().toString();
        String confPass = confPassword_input.getText().toString();
        String phone = phone_input.getText().toString();
        String pass = password_input.getText().toString();
        if (userName.isEmpty()) {
            username_input.setError(getString(R.string.enter_valid_name));
            return false;
        } else if (phone.isEmpty()) {
            phone_input.setError(getString(R.string.enter_valid_phone));
            return false;
        } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_input.setError(getString(R.string.enter_valid_email));
            return false;
        } else if (pass.isEmpty() || pass.length() < 6) {
            password_input.setError(getString(R.string.enter_valid_password));
            return false;
        } else if (confPass.isEmpty()) {
            confPassword_input.setError(getString(R.string.enter_valid_password));
            return false;
        } else if (!pass.equals(confPass)) {
            password_input.setError(getString(R.string.enter_valid_password));
            confPassword_input.setError(getString(R.string.enter_valid_password));
            return false;
        } else if (gender_RB.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getBaseContext(), R.string.chosse_gender, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    @OnClick(R.id.btnLogin)
    public void onButtonLoginClick(View view) {
        Intent signupIntent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(signupIntent);
        finish();
    }

}
