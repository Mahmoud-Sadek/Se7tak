package com.sadek.se7tak.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.sadek.se7tak.R;
import com.sadek.se7tak.adapter.BottomBarAdapter;
import com.sadek.se7tak.adapter.NoSwipePager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MainFragment extends Fragment {

    Unbinder unbinder;


    //the icons of tablayout  icon white  don't selected
    private int[] tabIcons = {
            R.drawable.icon_search,
            R.drawable.icon_schedule,
            R.drawable.icon_favourite,
            R.drawable.icon_more

    };
    // icon of tab layout selected blue icons
    private int[] tabIconsSelected = {
            R.drawable.icon_search,
            R.drawable.icon_schedule_filled,
            R.drawable.icon_favourite_filled,
            R.drawable.icon_more_filled
    };

    //inti the views
    @BindView(R.id.viewpager)
    NoSwipePager viewPager;
    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;

    private BottomBarAdapter pagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);
        try {
            setupViewPager(view);

            setupBottomNavBehaviors();
            setupBottomNavStyle();
            bottomNavigation.setTitleTextSize(30f, 30f);

            addBottomNavigationItems();
            bottomNavigation.setCurrentItem(0);


            bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
                @Override
                public boolean onTabSelected(int position, boolean wasSelected) {

                    if (!wasSelected)
                        viewPager.setCurrentItem(position);


                    return true;
                }
            });
        } catch (Exception e) {
//            BaseActitvty.restartApp(getActivity(), getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

        private void setupViewPager(View view) {
            viewPager.setPagingEnabled(false);

            pagerAdapter = new BottomBarAdapter(getChildFragmentManager());

            pagerAdapter.addFragments(createFragment(android.R.color.darker_gray, new SearchFragment()));
            pagerAdapter.addFragments(createFragment(android.R.color.darker_gray, new AppointmentsStatusFragment()));
            pagerAdapter.addFragments(createFragment(android.R.color.darker_gray, new FavoriteFragment()));
            pagerAdapter.addFragments(createFragment(android.R.color.darker_gray, new MyAccountFragment()));

            viewPager.setAdapter(pagerAdapter);
        }

        @NonNull
        private Fragment createFragment(int color, Fragment fragment) {

            fragment.setArguments(passFragmentArguments(fetchColor(color)));
            return fragment;
        }

        @NonNull
        private Bundle passFragmentArguments(int color) {
            Bundle bundle = new Bundle();
            bundle.putInt("color", color);
            return bundle;
        }


        public void setupBottomNavBehaviors() {

            bottomNavigation.setTranslucentNavigationEnabled(true);
        }




        private void setupBottomNavStyle() {

            bottomNavigation.setDefaultBackgroundColor(Color.WHITE);
            bottomNavigation.setAccentColor(fetchColor(R.color.colorAccent));
            bottomNavigation.setInactiveColor(fetchColor(R.color.colorAccent));

            // Colors for selected (active) and non-selected items.
            bottomNavigation.setColoredModeColors(fetchColor(android.R.color.white),
                    fetchColor(android.R.color.darker_gray));

            //  Enables Reveal effect
            bottomNavigation.setColored(true);

            //  Displays item Title always (for selected and non-selected items)
            bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
            bottomNavigation.setCurrentItem(0);
        }


        private void addBottomNavigationItems() {
            AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.search, tabIcons[0], R.color.colorPrimary);
            AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.appintments, tabIcons[1], R.color.colorPrimary);
            AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.favorites, tabIcons[2], R.color.colorPrimary);
            AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.more, tabIcons[3], R.color.colorPrimary);


            bottomNavigation.addItem(item1);
            bottomNavigation.addItem(item2);
            bottomNavigation.addItem(item3);
            bottomNavigation.addItem(item4);
        }



        private int fetchColor(@ColorRes int color) {
            return ContextCompat.getColor(getActivity(), color);
        }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unbind the view to free some memory
        unbinder.unbind();
    }

}
