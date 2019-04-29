package com.sadek.se7tak.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.sadek.se7tak.R;
import com.sadek.se7tak.dialogs.OrderConfirmedDialog;
import com.sadek.se7tak.firebase.FireDatabase;
import com.sadek.se7tak.model.DataMessage;
import com.sadek.se7tak.model.Doctor;
import com.sadek.se7tak.model.MyResponse;
import com.sadek.se7tak.model.Order;
import com.sadek.se7tak.model.PatientUser;
import com.sadek.se7tak.model.Token;
import com.sadek.se7tak.utils.APIService;
import com.sadek.se7tak.utils.Common;
import com.sadek.se7tak.utils.LocaleUtils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderDoctorFragment extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.docotr_profile_img)
    ImageView docotr_profile_img;
    @BindView(R.id.doctor_name_TV)
    TextView doctor_name_TV;
    @BindView(R.id.doctor_fees_TV)
    TextView doctor_fees_TV;
    @BindView(R.id.clinic_time_TV)
    TextView clinic_time_TV;
    @BindView(R.id.reserve_date_TV)
    TextView reserve_date_TV;
    @BindView(R.id.doctor_location_TV)
    TextView doctor_location_TV;

    @BindView(R.id.toolbars)
    Toolbar toolbar;
    @BindView(R.id.tabTxt)
    TextView tabTxt;
    String itemName;

    @BindView(R.id.username_input)
    EditText username_input;
    @BindView(R.id.phone_input)
    EditText phone_input;

    Doctor model;
    String selectedDate, selectedTime, phoneNumber, userName;
    FireDatabase fireDatabase;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_doctor, container, false);

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

        // init service


        fireDatabase = new FireDatabase(getContext());
        try {

            model = (Doctor) getArguments().getSerializable(Common.FIREBASE_DOCTOR);
            selectedDate = getArguments().getString(Common.ORDER_DATE);
            selectedTime = getArguments().getString(Common.ORDER_TIME);
            Picasso.with(getContext()).load(model.getProfileImage()).into(docotr_profile_img);


            doctor_fees_TV.setText(model.getClinicDoctor().getExaminationFees() + getString(R.string.egp) + "");
            clinic_time_TV.setText(selectedTime);
            reserve_date_TV.setText(selectedDate);

            if (Paper.book().read(Common.language).equals(LocaleUtils.ARABIC)) {
                doctor_name_TV.setText(model.getLastName());
                doctor_location_TV.setText(model.getClinicDoctor().getLocationAR().trim() + "");
            } else {
                doctor_name_TV.setText(model.getFirstName());
                doctor_location_TV.setText(model.getClinicDoctor().getLocationEN().trim() + "");
            }
            fireDatabase.getUser(new FireDatabase.UserCallback() {
                @Override
                public void onCallback(PatientUser model) {
                    username_input.setText(model.getUserName());
                    username_input.setEnabled(false);
                    phone_input.setText(model.getPhoneNumber());
                    phoneNumber = model.getPhoneNumber();
                    userName = model.getUserName();

                }
            });

        } catch (Exception e) {

        }
    }

    @OnClick(R.id.order_confirm)
    void order_confirm(View view) {
        if (phone_input.getText().toString().isEmpty())
            phoneNumber = phone_input.getText().toString();

        Order order = new Order();
        order.setDoctorId(model.getId());
        order.setUserId(FirebaseAuth.getInstance().getUid());
        order.setPhoneNumber(phoneNumber);
        order.setUserName(userName);
        order.setOrderDate(selectedDate);
        order.setOrderTime(selectedTime);
        order.setStuatus(Common.ORDER_STATUS_PENDING);
        order.setExamination(model.getClinicDoctor().getExaminationFees());
        order.setOrderTimestamp(ServerValue.TIMESTAMP);
        fireDatabase.addOrder(order, new FireDatabase.ResultCallback() {
            @Override
            public void onCallback(boolean success) {
                if (success)
                    new OrderConfirmedDialog(getContext(), order, model.getFirstName(), model.getClinicDoctor().getLocationEN(), new OrderConfirmedDialog.DialogInterAction() {
                        @Override
                        public void onDismissed() {
                            getActivity().onBackPressed();
                        }
                    }).show();
            }
        });

        Common.sendNotificationOrder(model.getId(), "يوجد طلب جديد", getActivity());

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
