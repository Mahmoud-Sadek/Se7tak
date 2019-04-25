package com.sadek.se7tak.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sadek.se7tak.R;
import com.sadek.se7tak.adapter.AppointmentsAdatpter;
import com.sadek.se7tak.firebase.FireDatabase;
import com.sadek.se7tak.firebase.FireStorage;
import com.sadek.se7tak.model.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AppointmentsFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.specialty_list)
    RecyclerView recycler;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.emptyTV)
    TextView emptyTV;

    AppointmentsAdatpter adatpter;
    List<Order> list;
    FireDatabase database;
    FireStorage storage;

    String status;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointments, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        list = new ArrayList<Order>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        adatpter = new AppointmentsAdatpter(getContext(), list);
        recycler.setAdapter(adatpter);


        storage = new FireStorage(getContext());
        database = new FireDatabase(getContext());
        loading.setVisibility(View.VISIBLE);

        status = getArguments().getString("status");
        database.getAppointments(status,new FireDatabase.OrderListCallback() {
            @Override
            public void onCallback(ArrayList<Order> model) {
                try {
                    list = model;
                    adatpter.notifyDataSetChanged();
                    if (list.size() == 0) {
                        loading.setVisibility(View.GONE);
                        emptyTV.setVisibility(View.VISIBLE);
                    } else {
                        loading.setVisibility(View.GONE);
                        emptyTV.setVisibility(View.GONE);
                        Collections.reverse(list);
                        adatpter = new AppointmentsAdatpter(getContext(), list);
                        recycler.setAdapter(adatpter);

                    }
                } catch (Exception e) {

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
