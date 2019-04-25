package com.sadek.se7tak.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sadek.se7tak.R;
import com.sadek.se7tak.adapter.AppointmentsAdatpter;
import com.sadek.se7tak.adapter.CustomFragmentPagerAdapter;
import com.sadek.se7tak.firebase.FireDatabase;
import com.sadek.se7tak.firebase.FireStorage;
import com.sadek.se7tak.model.Order;
import com.sadek.se7tak.utils.Common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AppointmentsStatusFragment extends Fragment {


    ViewPager p;
    TabLayout tabsStrip;
    CustomFragmentPagerAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointments_status, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
//            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbars);
//            toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
            TextView tabTxt = view.findViewById(R.id.tabTxt);
            tabTxt.setText(R.string.appintments);
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getActivity().onBackPressed();
//                }
//            });


            adapter = new CustomFragmentPagerAdapter(getChildFragmentManager());

            AppointmentsFragment pindingOrdersFragment = new AppointmentsFragment();
            AppointmentsFragment acceptedOrdersFragment = new AppointmentsFragment();
            AppointmentsFragment completedOrdersFragment = new AppointmentsFragment();
            AppointmentsFragment rejectedOrdersFragment = new AppointmentsFragment();
            AppointmentsFragment canceledOedersFragment = new AppointmentsFragment();
            adapter.addFragment(pindingOrdersFragment, getString(R.string.pending), Common.ORDER_STATUS_PENDING);
            adapter.addFragment(acceptedOrdersFragment, getString(R.string.accepted), Common.ORDER_STATUS_ACCEPTED);
            adapter.addFragment(completedOrdersFragment, getString(R.string.compeleted), Common.ORDER_STATUS_COMPLETED);
            adapter.addFragment(rejectedOrdersFragment, getString(R.string.rejected), Common.ORDER_STATUS_REJECTED);
            adapter.addFragment(canceledOedersFragment, getString(R.string.canceled), Common.ORDER_STATUS_CANCELED);
            p = view.findViewById(R.id.orders_pager);
            tabsStrip = (TabLayout) view.findViewById(R.id.orders_status_tabs);
            p.setAdapter(adapter);
            tabsStrip.setupWithViewPager(p);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
