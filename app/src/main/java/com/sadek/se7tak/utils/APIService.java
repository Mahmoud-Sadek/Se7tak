package com.sadek.se7tak.utils;


import com.sadek.se7tak.model.DataMessage;
import com.sadek.se7tak.model.MyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Mahmoud Sadek on 8/16/2018.
 */

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAADTzNfbM:APA91bFBYC_L-94vCM0f1XjV2PFAygIv1gANSakkSuxs8qqL-h27WXtWYxHAfq0EpUYFu369tIJQp-yxsQI44vc7IZFFtgCGXybU2gX6Fn8iF3Kh1pX1dr3eaQZTsMKu6Ae3kPOCgscN"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body DataMessage body);
}
