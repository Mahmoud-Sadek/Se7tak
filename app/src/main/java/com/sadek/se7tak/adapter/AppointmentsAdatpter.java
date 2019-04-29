package com.sadek.se7tak.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sadek.se7tak.R;
import com.sadek.se7tak.activity.MainActivity;
import com.sadek.se7tak.activity.MapsActivity;
import com.sadek.se7tak.dialogs.RateDialog;
import com.sadek.se7tak.firebase.FireDatabase;
import com.sadek.se7tak.model.Doctor;
import com.sadek.se7tak.model.Order;
import com.sadek.se7tak.model.Rate;
import com.sadek.se7tak.utils.Common;
import com.sadek.se7tak.utils.LocaleUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;


public class AppointmentsAdatpter extends RecyclerView.Adapter<AppointmentsAdatpter.ViewHolder> {

    private Context context;
    private List<Order> data;

    FireDatabase fireDatabase;

    public AppointmentsAdatpter(Context context, List<Order> data) {
        this.context = context;
        this.data = data;
        fireDatabase = new FireDatabase(context);

        Paper.init(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    RateDialog rateDialog;
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Common.FIREBASE_DOCTOR, data.get(position).getDoctorId());
                ((MainActivity) context).switchToPage(3, bundle, R.string.about_the_doctor);

            }
        });

        if (data.get(position).getNotes()==null)
            holder.order_note_layout.setVisibility(View.GONE);
        else {
            holder.order_note_layout.setVisibility(View.VISIBLE);
            holder.order_note_TV.setText(data.get(position).getNotes());
        }

        if (!data.get(position).getStuatus().equals(Common.ORDER_STATUS_COMPLETED))
            holder.rate_img.setVisibility(View.GONE);
        else
            holder.rate_img.setVisibility(View.VISIBLE);
        if (!data.get(position).getStuatus().equals(Common.ORDER_STATUS_PENDING))
            holder.delete_img.setVisibility(View.GONE);
        else
            holder.delete_img.setVisibility(View.VISIBLE);
        holder.rate_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateDialog = new RateDialog(context, new RateDialog.rateServiceAction() {
                    @Override
                    public void onGetRate(String code, float rating) {
                        Rate rate = new Rate();
                        rate.setComment(code);
                        rate.setRate(rating+"");
                        rate.setDoctorId(data.get(position).getDoctorId());
                        rate.setUserId(data.get(position).getUserId());
                        fireDatabase.addRate(rate, new FireDatabase.ResultCallback() {
                            @Override
                            public void onCallback(boolean success) {
                                rateDialog.dismiss();
                            }
                        });
                    }
                });
                rateDialog.show();
            }
        });
        holder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.showDeleteAlert(context, new FireDatabase.ResultCallback() {
                    @Override
                    public void onCallback(boolean success) {
                        if (success) {
                            Common.sendNotificationOrder(data.get(position).getDoctorId(), "تم إلغاء طلب", (Activity)context);
                            fireDatabase.deleteAppointment(data.get(position), new FireDatabase.ResultCallback() {
                                @Override
                                public void onCallback(boolean success) {

                                }
                            });
                        }
                    }
                });

            }
        });


        try {
            fireDatabase.getDoctor(data.get(position).getDoctorId(), new FireDatabase.DoctorCallback() {
                @Override
                public void onCallback(Doctor model) {
                    if(Paper.book().read(Common.language).equals(LocaleUtils.ARABIC)){

                        holder.doctor_name_TV.setText(model.getLastName());
                        holder.doctor_specialty_TV.setText(model.getSpecialty().getTitleAr() + "");
                        holder.doctor_location_TV.setText(model.getClinicDoctor().getLocationAR().trim() + "");
                    }else{
                        holder.doctor_name_TV.setText(model.getFirstName());
                        holder.doctor_specialty_TV.setText(model.getSpecialty().getTitleEn() + "");
                        holder.doctor_location_TV.setText(model.getClinicDoctor().getLocationEN().trim() + "");

                    }
                    Picasso.with(context).load(model.getProfileImage()).into(holder.docotr_profile_img);


                    holder.location_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, MapsActivity.class);
                            intent.putExtra(Common.lat, model.getClinicDoctor().getLat());
                            intent.putExtra(Common.lng, model.getClinicDoctor().getLang());
                            context.startActivity(intent);
                        }
                    });
                }
            });

            holder.docotr_examinations_TV.setText(data.get(position).getExamination() + context.getString(R.string.egp) + "");
            holder.order_date_TV.setText(data.get(position).getOrderDate() + "");
            holder.order_time_TV.setText(data.get(position).getOrderTime() + "");

        } catch (Exception e) {

        }


    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.docotr_profile_img)
        ImageView docotr_profile_img;
        @BindView(R.id.delete_img)
        ImageView delete_img;
        @BindView(R.id.rate_img)
        ImageView rate_img;
        @BindView(R.id.location_img)
        ImageView location_img;
        @BindView(R.id.doctor_name_TV)
        TextView doctor_name_TV;
        @BindView(R.id.doctor_specialty_TV)
        TextView doctor_specialty_TV;
        @BindView(R.id.doctor_location_TV)
        TextView doctor_location_TV;
        @BindView(R.id.order_date_TV)
        TextView order_date_TV;
        @BindView(R.id.order_time_TV)
        TextView order_time_TV;
        @BindView(R.id.docotr_examinations_TV)
        TextView docotr_examinations_TV;
        @BindView(R.id.order_note_TV)
        TextView order_note_TV;
        @BindView(R.id.order_note_layout)
        View order_note_layout;


        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
