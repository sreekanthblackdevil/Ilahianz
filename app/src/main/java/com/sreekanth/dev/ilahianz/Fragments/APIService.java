package com.sreekanth.dev.ilahianz.Fragments;

import com.sreekanth.dev.ilahianz.Notifications.MyResponse;
import com.sreekanth.dev.ilahianz.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-type:application/json",
                    "Authorization:key=AAAAJcQ6Jis:APA91bGRR6c73xfkmbeNNCQvcSAS_BPmLP85BAjqp0rArLQJaYhFUVxKlOd52eHfNGKEgxjXbNvM3noxkx9b5-X3x80tMi1Fk3i_VpLJK3KR63u59XPmRcVNCXJWx7zCuaoyTIiAXCza"

            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
