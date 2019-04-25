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

public class EditPassowrdFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.old_password_input)
    EditText old_password_input;
    @BindView(R.id.new_Password_input)
    EditText new_Password_input;
    @BindView(R.id.email_input)
    EditText email_input;


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
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);

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
                email_input.setText(model.getEmail());
                email_input.setEnabled(false);

            }
        });
    }

    @OnClick(R.id.btnConfirm)
    void btnConfirm(View view) {
        if (!validate()) {
            return;
        }
        auth.updatePassword(new_Password_input.getText().toString(), user);


    }

    private Boolean validate() {
        old_password_input.setError(null);
        new_Password_input.setError(null);
        email_input.setError(null);

        String oldPass = old_password_input.getText().toString();
        String newPass = new_Password_input.getText().toString();
        String email = email_input.getText().toString();

        if (oldPass.isEmpty()) {
            old_password_input.setError(getString(R.string.enter_valid_password));
            return false;
        } else if (newPass.isEmpty()) {
            new_Password_input.setError(getString(R.string.enter_valid_password));
            return false;
        }else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_input.setError(getString(R.string.enter_valid_email));
            return false;
        }else if(!oldPass.equals(user.getPassword())){
            old_password_input.setError(getString(R.string.enter_valid_password));
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
