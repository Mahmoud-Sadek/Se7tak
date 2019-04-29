package com.sadek.se7tak.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sadek.se7tak.R;
import com.sadek.se7tak.adapter.SpecialtyAdatpter;
import com.sadek.se7tak.firebase.FireDatabase;
import com.sadek.se7tak.firebase.FireStorage;
import com.sadek.se7tak.model.SpecialtyModel;
import com.sadek.se7tak.utils.Common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SpecialtyFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.specialty_list)
    RecyclerView recycler;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.emptyTV)
    TextView emptyTV;

    SpecialtyAdatpter adatpter;
    List<SpecialtyModel> list;
    @BindView(R.id.toolbars)
    Toolbar toolbar;
    @BindView(R.id.tabTxt)
    TextView tabTxt;

    String itemName;
    FireDatabase database;
    FireStorage storage;
    SpecialtyModel model;

    public static Bitmap specialtyImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_specialty, container, false);

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


        list = new ArrayList<SpecialtyModel>();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recycler.setLayoutManager(layoutManager);
        adatpter = new SpecialtyAdatpter(getContext(), list);
        recycler.setAdapter(adatpter);

        storage = new FireStorage(getContext());
        database = new FireDatabase(getContext());
        loading.setVisibility(View.VISIBLE);
        database.getSpecialty(new FireDatabase.SpecialtyCallback() {
            @Override
            public void onCallback(ArrayList<SpecialtyModel> model) {
                try{
                list = model;
                if (list.size() == 0) {
                    loading.setVisibility(View.GONE);
                    emptyTV.setVisibility(View.VISIBLE);
                } else {
                    loading.setVisibility(View.GONE);
                    emptyTV.setVisibility(View.GONE);
                    Collections.reverse(list);
                    adatpter = new SpecialtyAdatpter(getContext(), list);
                    recycler.setAdapter(adatpter);
                }
                }catch (Exception e){

                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unbinder.unbind();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

}
