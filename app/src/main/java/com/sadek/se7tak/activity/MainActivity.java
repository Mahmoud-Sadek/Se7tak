package com.sadek.se7tak.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.sadek.se7tak.R;
import com.sadek.se7tak.fragment.DoctorDetailsFragment;
import com.sadek.se7tak.fragment.EditAccountFragment;
import com.sadek.se7tak.fragment.EditPassowrdFragment;
import com.sadek.se7tak.fragment.MainFragment;
import com.sadek.se7tak.fragment.OrderDoctorFragment;
import com.sadek.se7tak.fragment.SearchByNameFragment;
import com.sadek.se7tak.fragment.SpecialtyFragment;
import com.sadek.se7tak.utils.Common;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = new Bundle();
        switchToPage(1, bundle, R.string.app_name);


    }

    public void switchToPage(int page, Bundle bundle, int nameId) {
        FragmentTransaction transaction = getTransaction();
        String name = getResources().getString(nameId);
        bundle.putString(Common.SCREEN_NAME, name);
        Fragment nextFragment;
        switch (page) {

            case 1:
                nextFragment = new MainFragment();
                nextFragment.setArguments(bundle);
                transaction.replace(R.id.head_container, nextFragment, name);
                transaction.commit();
                break;

            case 2:
                nextFragment = new SearchByNameFragment();
                nextFragment.setArguments(bundle);
                transaction.replace(R.id.head_container, nextFragment, name);
                transaction.addToBackStack(name);
                transaction.commit();
                break;
            case 3:
                nextFragment = new DoctorDetailsFragment();
                nextFragment.setArguments(bundle);
                transaction.replace(R.id.head_container, nextFragment, name);
                transaction.addToBackStack(name);
                transaction.commit();
                break;
            case 4:
                nextFragment = new OrderDoctorFragment();
                nextFragment.setArguments(bundle);
                transaction.replace(R.id.head_container, nextFragment, name);
                transaction.addToBackStack(name);
                transaction.commit();
                break;
            case 5:
                nextFragment = new SpecialtyFragment();
                nextFragment.setArguments(bundle);
                transaction.replace(R.id.head_container, nextFragment, name);
                transaction.addToBackStack(name);
                transaction.commit();
                break;
            case 6:
                nextFragment = new EditAccountFragment();
                nextFragment.setArguments(bundle);
                transaction.replace(R.id.head_container, nextFragment, name);
                transaction.addToBackStack(name);
                transaction.commit();
                break;
            case 7:
                nextFragment = new EditPassowrdFragment();
                nextFragment.setArguments(bundle);
                transaction.replace(R.id.head_container, nextFragment, name);
                transaction.addToBackStack(name);
                transaction.commit();
                break;

        }
    }
}
