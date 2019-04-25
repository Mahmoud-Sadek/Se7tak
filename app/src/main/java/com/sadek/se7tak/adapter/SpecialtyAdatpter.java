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
import android.widget.TextView;

import com.sadek.se7tak.R;
import com.sadek.se7tak.activity.MainActivity;
import com.sadek.se7tak.model.SpecialtyModel;
import com.sadek.se7tak.utils.Common;
import com.sadek.se7tak.utils.LocaleUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;


public class SpecialtyAdatpter extends RecyclerView.Adapter<SpecialtyAdatpter.ViewHolder> {

    private Context context;

    private List<SpecialtyModel> data;

    public SpecialtyAdatpter(Context context, List<SpecialtyModel> data) {
        this.context = context;
        this.data = data;
        Paper.init(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_specialty, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Common.FIREBASE_SPECIALTY, data.get(position));
                ((MainActivity) context).switchToPage(2, bundle, R.string.search_by_doctor_name);
            }
        });
        if (Paper.book().read(Common.language).equals(LocaleUtils.ARABIC)) {
            holder.nameTV.setText(data.get(position).getTitleAr());
        } else
            holder.nameTV.setText(data.get(position).getTitleEn());
        Picasso.with(context).load(data.get(position).getImage()).into(holder.photoIV);


    }

    @Override
    public int getItemCount() {

        return data.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nameTV)
        TextView nameTV;
        @BindView(R.id.photoIV)
        ImageView photoIV;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
