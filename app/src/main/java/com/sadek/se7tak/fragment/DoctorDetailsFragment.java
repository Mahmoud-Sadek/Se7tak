package com.sadek.se7tak.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.sadek.se7tak.R;
import com.sadek.se7tak.activity.MainActivity;
import com.sadek.se7tak.adapter.PhotosAdapter;
import com.sadek.se7tak.firebase.FireDatabase;
import com.sadek.se7tak.model.Doctor;
import com.sadek.se7tak.model.Favorite;
import com.sadek.se7tak.utils.Common;
import com.sadek.se7tak.utils.LocaleUtils;
import com.skyhope.weekday.WeekDaySelector;
import com.skyhope.weekday.callback.WeekItemClickListener;
import com.skyhope.weekday.model.WeekModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.paperdb.Paper;


public class DoctorDetailsFragment extends Fragment implements WeekItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.weekDaySelector)
    WeekDaySelector weekDaySelector;
    @BindView(R.id.doctor_likes_img)
    ImageView doctor_likes_img;
    @BindView(R.id.docotr_profile_img)
    ImageView docotr_profile_img;
    @BindView(R.id.doctor_name_TV)
    TextView doctor_name_TV;
    @BindView(R.id.doctor_about_TV)
    TextView doctor_about_TV;
    @BindView(R.id.doctor_rating_bar)
    RatingBar doctor_rating_bar;
    @BindView(R.id.doctor_rating_TV)
    TextView doctor_rating_TV;
    @BindView(R.id.doctor_specialty_TV)
    TextView doctor_specialty_TV;
    @BindView(R.id.doctor_location_TV)
    TextView doctor_location_TV;
    @BindView(R.id.docotr_examinations_TV)
    TextView docotr_examinations_TV;
    @BindView(R.id.clinic_time_TV)
    TextView clinic_time_TV;
    @BindView(R.id.doctor_about2_TV)
    TextView doctor_about2_TV;
    @BindView(R.id.clinic_name_TV)
    TextView clinic_name_TV;
    @BindView(R.id.assistant_name_TV)
    TextView assistant_name_TV;
    @BindView(R.id.doctor_likes_TV)
    TextView doctor_likes_TV;
    @BindView(R.id.clinic_photos_recycler)
    RecyclerView clinic_photos_recycler;


    @BindView(R.id.call_clinic_img)
    ImageView call_clinic_img;
    @BindView(R.id.call_assistant_img)
    ImageView call_assistant_img;


    @BindView(R.id.btnReserveDoctor)
    Button btnReserveDoctor;
    Doctor model;
    String selectedDate, selectedTime;
    FireDatabase fireDatabase;

    @BindView(R.id.toolbars)
    Toolbar toolbar;
    @BindView(R.id.tabTxt)
    TextView tabTxt;
    String itemName;

    //rate bottomsheet
    private BottomSheetBehavior mBottomSheetBehavior;
    @BindView(R.id.bottom_sheet)
    View bottomSheet;



    PhotosAdapter photosAdaptet;
    List<String> photosList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_detail, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);
        Paper.init(getContext());

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

        fireDatabase = new FireDatabase(getContext());


        weekDaySelector.setWeekItemClickListener(this);
        String doctorId = getArguments().getString(Common.FIREBASE_DOCTOR);
        if (doctorId != null) {
            fireDatabase.getDoctor(doctorId, new FireDatabase.DoctorCallback() {
                @Override
                public void onCallback(Doctor modelDoctor) {
                    model = modelDoctor;
                    initData();
                }
            });
        } else {
            model = (Doctor) getArguments().getSerializable(Common.FIREBASE_DOCTOR);
            initData();
        }
    }

    private void initData() {
        try {
            Set<Integer> holiday = new HashSet<>();
            for (int i = 0; i < model.getWorkDaysDoctor().size(); i++) {
                if (model.getWorkDaysDoctor().get(i).isHoliday()) {
                    holiday.add(model.getWorkDaysDoctor().get(i).getDay());
                }
            }
            if (!holiday.isEmpty())
                weekDaySelector.setHoliday(holiday);
            if (Paper.book().read(Common.language).equals(LocaleUtils.ARABIC)) {
                doctor_name_TV.setText(model.getLastName());
                doctor_specialty_TV.setText(model.getSpecialty().getTitleAr() + "");
                doctor_about_TV.setText(model.getClinicDoctor().getNameAR() + "");
                doctor_about2_TV.setText(model.getAboutDoctor().getAboutAR() + "");
                doctor_location_TV.setText(model.getClinicDoctor().getLocationAR() + "");
                clinic_name_TV.setText(model.getClinicDoctor().getNameAR() + "");
                assistant_name_TV.setText(model.getClinicDoctor().getClinicAssistantDoctor().getNameAR() + "");
            } else {
                doctor_name_TV.setText(model.getFirstName());
                doctor_specialty_TV.setText(model.getSpecialty().getTitleEn() + "");
                doctor_about_TV.setText(model.getClinicDoctor().getNameEN() + "");
                doctor_about2_TV.setText(model.getAboutDoctor().getAboutEN() + "");
                doctor_location_TV.setText(model.getClinicDoctor().getLocationEN() + "");
                clinic_name_TV.setText(model.getClinicDoctor().getNameEN() + "");
                assistant_name_TV.setText(model.getClinicDoctor().getClinicAssistantDoctor().getNameEN() + "");
            }

            btnReserveDoctor.setVisibility(View.GONE);
            clinic_time_TV.setVisibility(View.GONE);
            Picasso.with(getContext()).load(model.getProfileImage()).into(docotr_profile_img);

            doctor_rating_TV.setText(getString(R.string.over_rate) + " " +
                    (model.getRateCount() == null ? "0" : model.getRateCount()) +
                    " " + getString(R.string.vistors));


            docotr_examinations_TV.setText(model.getClinicDoctor().getExaminationFees() + getString(R.string.egp) + "");


            doctorAddedFavorite();
            doctor_rating_bar.setRating(Float.parseFloat(Double.parseDouble(model.getRateTotal()) / Double.parseDouble(model.getRateCount()) + ""));


            //home_new_offers_recycler
            final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            clinic_photos_recycler.setLayoutManager(mLayoutManager);
            photosList = new ArrayList<>();
            photosList = model.getClinicDoctor().getImges();
            photosAdaptet = new PhotosAdapter(photosList, getContext());
            clinic_photos_recycler.setAdapter(photosAdaptet);
        } catch (Exception e) {

        }


        bottomSheet();
    }

    private void bottomSheet() {

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.d("bottomSheet", "State Collapsed");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.d("bottomSheet", "State Dragging");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.d("bottomSheet", "State Expanded");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.d("bottomSheet", "State Hidden");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.d("bottomSheet", "State Settling");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }


    boolean fav;

    private boolean doctorAddedFavorite() {
        fireDatabase.getFavorites(model.getId(), new FireDatabase.ResultStringCallback() {
            @Override
            public void onCallback(String success) {
                try {
                    doctor_likes_TV.setText(success);
                } catch (Exception e) {

                }
            }
        });
        fireDatabase.isFavorite(model.getId(), new FireDatabase.ResultCallback() {
            @Override
            public void onCallback(boolean success) {
                fav = success;
                try {
                    if (success) {
                        doctor_likes_img.setImageResource(R.drawable.icon_favourite_filled);
                    } else {
                        doctor_likes_img.setImageResource(R.drawable.icon_favourite);
                    }
                } catch (Exception e) {

                }
            }
        });
        return fav;
    }

    @OnClick(R.id.doctor_likes_img)
    void doctor_likes_img(View view) {
        Favorite favorite = new Favorite();
        favorite.setDoctorId(model.getId());
        favorite.setUserId(FirebaseAuth.getInstance().getUid());
        fireDatabase.addLike(favorite, fav, new FireDatabase.ResultCallback() {
            @Override
            public void onCallback(boolean success) {
                try {
                    if (success)
                        doctor_likes_img.setImageResource(R.drawable.icon_favourite_filled);
                    else doctor_likes_img.setImageResource(R.drawable.icon_favourite);
                } catch (Exception e) {

                }
            }
        });
    }

    @OnClick(R.id.call_clinic_img)
    void call_clinic_img(View view) {
        dialContactPhone(model.getClinicDoctor().getPhone());
    }

    @OnClick(R.id.call_assistant_img)
    void call_assistant_img(View view) {
        dialContactPhone(model.getClinicDoctor().getClinicAssistantDoctor().getPhone());
    }

    @OnClick(R.id.btnReserveDoctor)
    void btnReserveDoctor(View view) {
        selectedTime = clinic_time_TV.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Common.FIREBASE_DOCTOR, model);
        bundle.putString(Common.ORDER_DATE, selectedDate);
        bundle.putString(Common.ORDER_TIME, selectedTime);
        ((MainActivity) getContext()).switchToPage(4, bundle, R.string.book);
    }

    @OnClick(R.id.doctor_rates)
    void doctor_rates(View view) {
        BottomSheetDialogFragment bottomSheetDialogFragment = new RateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Common.FIREBASE_DOCTOR, model.getId());
        bottomSheetDialogFragment.setArguments(bundle);
        bottomSheetDialogFragment.show(getChildFragmentManager(), bottomSheetDialogFragment.getTag());

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


    @Override
    public void onGetItem(WeekModel weekModel) {
        try {

            selectedDate = weekModel.getDate();
            clinic_time_TV.setText(model.getWorkDaysDoctor().get(weekModel.getCurrentDay() - 1).getHourFrom() + " : " + model.getWorkDaysDoctor().get(weekModel.getCurrentDay() - 1).getHourTo());
            btnReserveDoctor.setVisibility(View.VISIBLE);
            clinic_time_TV.setVisibility(View.VISIBLE);
            if (weekModel.isHoliday()) {
                btnReserveDoctor.setVisibility(View.GONE);
                clinic_time_TV.setVisibility(View.VISIBLE);
                clinic_time_TV.setText(getString(R.string.holiday));

            }

        } catch (Exception e) {

        }
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
}
