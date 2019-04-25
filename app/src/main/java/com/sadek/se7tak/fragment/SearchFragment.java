package com.sadek.se7tak.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sadek.se7tak.R;
import com.sadek.se7tak.activity.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SearchFragment extends Fragment {

    Unbinder unbinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

    }
    @OnClick(R.id.search_doc_name_btn)
    void search_doc_name_btn(View view) {
        Bundle bundle = new Bundle();
        ((MainActivity) getContext()).switchToPage(2, bundle, R.string.search_by_doctor_name);
    }

    @OnClick(R.id.search_doc_specialty_btn)
    void search_doc_specialty_btn(View view) {
        Bundle bundle = new Bundle();
        ((MainActivity) getContext()).switchToPage(5, bundle, R.string.search_by_specialty);
    }



        @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unbind the view to free some memory
        unbinder.unbind();
    }



}
