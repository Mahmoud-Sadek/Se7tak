package com.sadek.se7tak.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sadek.se7tak.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private List<String> contents;
    private Context mContext;
    public PhotosAdapter(List<String> contents, Context mContext) {
        this.contents = contents;
        this.mContext = mContext;
    }


    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;


        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_clinic_photos, parent, false);
        return new ViewHolder(view);


    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        int position = positions % contents.size();

        //todo here to get the image and load it
        if (mContext != null)
//            Common.BASE_IMAGE_URL+
            Picasso.with(mContext).load(contents.get(position)).into(holder.image);


    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.home_offer_image)ImageView image;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }

}