package com.sadek.se7tak.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sadek.se7tak.R;
import com.sadek.se7tak.activity.MainActivity;
import com.sadek.se7tak.model.Doctor;
import com.sadek.se7tak.model.SpecialtyModel;
import com.sadek.se7tak.utils.Common;
import com.sadek.se7tak.utils.LocaleUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;


public class DoctorAdatpter extends RecyclerView.Adapter<DoctorAdatpter.ViewHolder> {

    private Context context;

    private List<Doctor> data;

    public DoctorAdatpter(Context context, List<Doctor> data) {
        this.context = context;
        this.data = data;
        Paper.init(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
//                bundle.putSerializable(Common.FIREBASE_DOCTOR, data.get(position));
                bundle.putString(Common.FIREBASE_DOCTOR, data.get(position).getId());
                ((MainActivity) context).switchToPage(3, bundle, R.string.about_the_doctor);

            }
        });

        try {

            if(Paper.book().read(Common.language).equals(LocaleUtils.ARABIC)) {
                holder.doctor_name_TV.setText(data.get(position).getLastName());
                holder.doctor_specialty_TV.setText(data.get(position).getSpecialty().getTitleAr()+"");
                holder.doctor_location_TV.setText(data.get(position).getClinicDoctor().getLocationAR().trim()+"");
                holder.doctor_about_TV.setText(data.get(position).getAboutDoctor().getAboutAR()+"");

            }else {
                holder.doctor_name_TV.setText(data.get(position).getFirstName());
                holder.doctor_specialty_TV.setText(data.get(position).getSpecialty().getTitleEn()+"");
                holder.doctor_location_TV.setText(data.get(position).getClinicDoctor().getLocationEN().trim()+"");
                holder.doctor_about_TV.setText(data.get(position).getAboutDoctor().getAboutEN()+"");

            }
                Picasso.with(context).load(data.get(position).getProfileImage()).into(holder.docotr_profile_img);

        holder.doctor_rating_TV.setText(context.getString(R.string.over_rate)+" "+
                (data.get(position).getRateCount()==null ?"0":data.get(position).getRateCount())+
                        " "+context.getString(R.string.vistors));

        holder.docotr_examinations_TV.setText(data.get(position).getClinicDoctor().getExaminationFees()+context.getString(R.string.egp)+"");
            holder.doctor_rating_bar.setRating(Float.parseFloat(Double.parseDouble(data.get(position).getRateTotal())/Double.parseDouble(data.get(position).getRateCount())+""));
        }catch (Exception e){

        }


    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


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


        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
