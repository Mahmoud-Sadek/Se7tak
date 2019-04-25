package com.sadek.se7tak.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sadek.se7tak.R;
import com.sadek.se7tak.adapter.DoctorAdatpter;
import com.sadek.se7tak.adapter.RateAdatpter;
import com.sadek.se7tak.firebase.FireDatabase;
import com.sadek.se7tak.model.Doctor;
import com.sadek.se7tak.model.Rate;
import com.sadek.se7tak.utils.Common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RateFragment extends BottomSheetDialogFragment {

    Unbinder unbinder;
    @BindView(R.id.specialty_list)
    RecyclerView recycler;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.emptyTV)
    TextView emptyTV;

    RateAdatpter adatpter;
    List<Rate> list;
    FireDatabase database;

    String doctorId;

//
//    @Override
//    public void setupDialog(Dialog dialog, int style) {
//        super.setupDialog(dialog, style);
//        View contentView = View.inflate(getContext(), R.layout.fragment_reviews, null);
//        dialog.setContentView(contentView);
//        CoordinatorLayout.LayoutParams layoutParams =
//                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
//        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
//        if (behavior != null && behavior instanceof BottomSheetBehavior) {
//            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
//        }
//
//    }
    private LinearLayoutManager mLinearLayoutManager;

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        doctorId = getArguments().getString(Common.FIREBASE_DOCTOR);
        list = new ArrayList<Rate>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        adatpter = new RateAdatpter(getContext(), list);
        recycler.setAdapter(adatpter);


        database = new FireDatabase(getContext());
        loading.setVisibility(View.VISIBLE);
            database.getDoctorRatesList(doctorId, new FireDatabase.RateListCallback() {
                @Override
                public void onCallback(ArrayList<Rate> model) {
                    try {
                    list = model;
                    if (list.size() == 0) {
                        loading.setVisibility(View.GONE);
                        emptyTV.setVisibility(View.VISIBLE);
                    } else {
                        loading.setVisibility(View.GONE);
                        emptyTV.setVisibility(View.GONE);
                        Collections.reverse(list);
                        adatpter = new RateAdatpter(getContext(), list);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
