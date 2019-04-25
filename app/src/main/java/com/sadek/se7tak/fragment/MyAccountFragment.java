package com.sadek.se7tak.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.sadek.se7tak.R;
import com.sadek.se7tak.activity.MainActivity;
import com.sadek.se7tak.activity.SplashActivity;
import com.sadek.se7tak.dialogs.LanguageDialog;
import com.sadek.se7tak.utils.Common;
import com.sadek.se7tak.utils.LocaleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.paperdb.Paper;

import static com.sadek.se7tak.activity.BaseActivity.restartApp;


public class MyAccountFragment extends Fragment {

    Unbinder unbinder;
    LanguageDialog languageDialog;

    @BindView(R.id.toolbars)
    Toolbar toolbar;
    @BindView(R.id.tabTxt)
    TextView tabTxt;
    String itemName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

            tabTxt.setText(R.string.edit_profile);


    }

    @OnClick(R.id.editAccountBtn)
    void editAccountBtn(View view) {
        Bundle bundle = new Bundle();
        ((MainActivity) getContext()).switchToPage(6, bundle, R.string.edit_account);
    }

    @OnClick(R.id.languageBtn)
    void languageBtn(View view) {
        channgeLanguage();
    }

    @OnClick(R.id.logoutBtn)
    void logoutBtn(View view) {
        //Ask to Login
        new MaterialDialog.Builder(getContext())
                .title(R.string.app_name)
                .content(R.string.logoutask)
                .positiveText(R.string.log_out)
                .negativeText(R.string.dismisss)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getActivity(), SplashActivity.class));
                        getActivity().finish();
                    }
                })
                .show();
    }


    private void channgeLanguage() {

        languageDialog = new LanguageDialog(getContext(), new LanguageDialog.languageDialogAction() {
            @Override
            public void onGetCode(boolean langStatus) {
                if (langStatus) {

                    Paper.book().write(Common.language, LocaleUtils.ARABIC);
                    restartApp(getContext(), getActivity());
                } else {
                    Paper.book().write(Common.language, LocaleUtils.ENGLISH);
                    restartApp(getContext(), getActivity());
                }
            }
        });
        languageDialog.show();
    }


    @OnClick(R.id.aboutUsBtn)
    void aboutUsBtn(View view) {

    }

    @OnClick(R.id.helpCenterBtn)
    void helpCenterBtn(View view) {

    }

    @OnClick(R.id.editPasswordBtn)
    void editPasswordBtn(View view) {
        Bundle bundle = new Bundle();
        ((MainActivity) getContext()).switchToPage(7, bundle, R.string.edit_account);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unbind the view to free some memory
        unbinder.unbind();
    }

}
