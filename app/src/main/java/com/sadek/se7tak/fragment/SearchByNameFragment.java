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
import com.sadek.se7tak.model.SpecialtyModel;
import com.sadek.se7tak.utils.Common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchByNameFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.specialty_list)
    RecyclerView recycler;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.emptyTV)
    TextView emptyTV;
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;

    @BindView(R.id.toolbars)
    Toolbar toolbar;
    @BindView(R.id.tabTxt)
    TextView tabTxt;
    String itemName;

    DoctorAdatpter adatpter;
    List<Doctor> list;
    FireDatabase database;
    FireStorage storage;
    SpecialtyModel model;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_by_name, container, false);

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

        list = new ArrayList<Doctor>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        adatpter = new DoctorAdatpter(getContext(), list);
        recycler.setAdapter(adatpter);

        try {
            model = (SpecialtyModel) getArguments().getSerializable(Common.FIREBASE_SPECIALTY);
        } catch (Exception e) {

        }

        intSearch();

        storage = new FireStorage(getContext());
        database = new FireDatabase(getContext());
        loading.setVisibility(View.VISIBLE);
        if (model != null)
            database.getDoctorListBySpecialty(model, new FireDatabase.DoctorListCallback() {
                @Override
                public void onCallback(ArrayList<Doctor> model) {
                    list = model;
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
                }
            });
        else
            database.getDoctorList(new FireDatabase.DoctorListCallback() {
                @Override
                public void onCallback(ArrayList<Doctor> model) {
                    try {

                    list = model;
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

    private void intSearch() {
        //Search
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                mSearchView.showProgress();
                List<Doctor> suggest = new ArrayList<>();
                for (Doctor search : list) {
                    if ((search.getFirstName()+search.getLastName()).toLowerCase().contains(currentQuery.toLowerCase()))
                        suggest.add(search);
                }
                if (suggest.size() > 0)
                    emptyTV.setVisibility(View.GONE);
                else emptyTV.setVisibility(View.VISIBLE);
                adatpter = new DoctorAdatpter(getContext(), suggest);
                //                recyclerView.setAdapter(serviceAdapter);
                recycler.setAdapter(adatpter);
                mSearchView.hideProgress();
            }
        });
        mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                mSearchView.showProgress();
                adatpter = new DoctorAdatpter(getContext(), list);
                recycler.setAdapter(adatpter);
                emptyTV.setVisibility(View.GONE);
                mSearchView.hideProgress();
            }
        });
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (newQuery.equals("")) {
                    mSearchView.showProgress();
                    adatpter = new DoctorAdatpter(getContext(), list);
                    recycler.setAdapter(adatpter);
                    emptyTV.setVisibility(View.GONE);
                    mSearchView.hideProgress();
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
