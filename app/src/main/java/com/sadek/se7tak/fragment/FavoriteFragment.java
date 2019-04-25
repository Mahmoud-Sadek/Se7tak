package com.sadek.se7tak.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.sadek.se7tak.R;
import com.sadek.se7tak.adapter.DoctorAdatpter;
import com.sadek.se7tak.firebase.FireDatabase;
import com.sadek.se7tak.firebase.FireStorage;
import com.sadek.se7tak.model.Doctor;
import com.sadek.se7tak.model.Favorite;
import com.sadek.se7tak.model.SpecialtyModel;
import com.sadek.se7tak.utils.Common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoriteFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.specialty_list)
    RecyclerView recycler;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.emptyTV)
    TextView emptyTV;

    DoctorAdatpter adatpter;
    List<Doctor> list;
    FireDatabase database;
    FireStorage storage;
    SpecialtyModel model;

    @BindView(R.id.toolbars)
    Toolbar toolbar;
    @BindView(R.id.tabTxt)
    TextView tabTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        tabTxt.setText(R.string.favorites);

        list = new ArrayList<Doctor>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        adatpter = new DoctorAdatpter(getContext(), list);
        recycler.setAdapter(adatpter);

        try {
            model = (SpecialtyModel) getArguments().getSerializable(Common.FIREBASE_SPECIALTY);
        } catch (Exception e) {

        }


        storage = new FireStorage(getContext());
        database = new FireDatabase(getContext());
        loading.setVisibility(View.VISIBLE);
            database.getUserFavorites(new FireDatabase.FavoriteListCallback() {
                @Override
                public void onCallback(ArrayList<Favorite> model) {
                    list = new ArrayList<>();
                    for (int i = 0; i < model.size(); i++) {
                        database.getDoctor(model.get(i).getDoctorId(), new FireDatabase.DoctorCallback() {
                            @Override
                            public void onCallback(Doctor model) {
                                try {


                                    list.add(model);
                                    if (list.size() == 0) {
                                        loading.setVisibility(View.GONE);
                                        emptyTV.setVisibility(View.VISIBLE);
                                    } else {
                                        loading.setVisibility(View.GONE);
                                        emptyTV.setVisibility(View.GONE);
                                        Collections.reverse(list);
                                        adatpter = new DoctorAdatpter(getContext(), list);
                                        recycler.setAdapter(adatpter);
                                    }
                                }catch (Exception e){

                                }
                            }
                        });
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
