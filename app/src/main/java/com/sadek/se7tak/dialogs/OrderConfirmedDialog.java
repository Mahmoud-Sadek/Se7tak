package com.sadek.se7tak.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.sadek.se7tak.R;
import com.sadek.se7tak.model.Order;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrderConfirmedDialog extends Dialog {
    Unbinder unbinder;
    @BindView(R.id.doctor_name_TV)
    TextView doctor_name_TV;
    @BindView(R.id.clinic_time_TV)
    TextView clinic_time_TV;
    @BindView(R.id.reserve_date_TV)
    TextView reserve_date_TV;
    @BindView(R.id.doctor_location_TV)
    TextView doctor_location_TV;

    public OrderConfirmedDialog(@NonNull Context context, Order order, String name, String location, final DialogInterAction dialogInterAction) {
        super(context);
        setContentView(R.layout.order_confirm_dilog);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        unbinder = ButterKnife.bind(this);

        doctor_name_TV.setText(name);
        doctor_location_TV.setText(location.trim());
        clinic_time_TV.setText(order.getOrderTime());
        reserve_date_TV.setText(order.getOrderDate());


        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialogInterAction.onDismissed();
            }
        });
    }

    public interface DialogInterAction{
        void onDismissed();
    }
}