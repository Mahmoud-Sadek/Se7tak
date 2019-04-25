package com.sadek.se7tak.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sadek.se7tak.R;
import com.sadek.se7tak.firebase.FireAuth;
import com.sadek.se7tak.firebase.FireDatabase;
import com.sadek.se7tak.model.PatientUser;
import com.sadek.se7tak.utils.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditAccountFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.username_input)
    EditText username_input;
    @BindView(R.id.phone_input)
    EditText phone_input;
    @BindView(R.id.email_input)
    EditText email_input;
    @BindView(R.id.gender_RB)
    RadioGroup gender_RB;


    @BindView(R.id.toolbars)
    Toolbar toolbar;
    @BindView(R.id.tabTxt)
    TextView tabTxt;
    String itemName;

    FireAuth auth;
    FireDatabase database;
    PatientUser user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            itemName = bundle.getString(Common.SCREEN_NAME);
            tabTxt.setText(itemName);
        }
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        auth = new FireAuth(getContext());
        database = new FireDatabase(getContext());

        getUserInfo();

    }

    private void getUserInfo() {
        database.getUser(new FireDatabase.UserCallback() {
            @Override
            public void onCallback(PatientUser model) {
                user = model;
                user.setPassword(model.getPassword());
                email_input.setText(model.getEmail());
                email_input.setEnabled(false);
                username_input.setText(model.getUserName());
                phone_input.setText(model.getPhoneNumber());
                if (model.getGender().equals(getString(R.string.male))) {
                    gender_RB.check(R.id.maleRB);
                } else gender_RB.check(R.id.femaleRB);
            }
        });
    }

    @OnClick(R.id.btnConfirm)
    void btnConfirm(View view) {
        if (!validate()) {
            return;
        }

        user.setUserName(username_input.getText().toString());
        user.setEmail(email_input.getText().toString());
        user.setPhoneNumber(phone_input.getText().toString());
        if (gender_RB.getCheckedRadioButtonId() == R.id.maleRB)
            user.setGender(getString(R.string.male));
        else
            user.setGender(getString(R.string.female));
        database.editUser(user);


    }

    private Boolean validate() {
        email_input.setError(null);
        username_input.setError(null);
        phone_input.setError(null);

        String email = email_input.getText().toString();
        String userName = username_input.getText().toString();
        String phone = phone_input.getText().toString();
        if (userName.isEmpty()) {
            username_input.setError(getString(R.string.enter_valid_name));
            return false;
        } else if (phone.isEmpty()) {
            phone_input.setError(getString(R.string.enter_valid_phone));
            return false;
        } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_input.setError(getString(R.string.enter_valid_email));
            return false;
        } else if (gender_RB.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), R.string.chosse_gender, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unbind the view to free some memory
        unbinder.unbind();
    }

}
