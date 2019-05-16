package com.sreekanth.dev.ilahianz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sreekanth.dev.ilahianz.model.User;

import java.util.Objects;


/*
 * Written by Sreekanth K R in (2018)
 * for Ilahia college of arts and science
 * */

public class start extends AppCompatActivity {
    public static boolean newUser;
    Dialog NoInternetPopup;
    Handler handler = new Handler();
    FirebaseUser firebaseUser;
    ImageView image;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (firebaseUser != null) {
                Intent intent = new Intent(start.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(start.this, signinActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        image = findViewById(R.id.imageView);
        //firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (Connected(start.this)) {
            Toast.makeText(this, "No Internet !", Toast.LENGTH_SHORT).show();
        }
        handler.postDelayed(runnable, 1000);
        newUser = false;
    }

    public void popupNoInternet(Context context) {
        NoInternetPopup = new Dialog(context);
        NoInternetPopup.setContentView(R.layout.popup_no_internet);
        Objects.requireNonNull(NoInternetPopup.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        NoInternetPopup.show();
    }

    public boolean Connected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return (mobile == null || !mobile.isConnectedOrConnecting()) && (wifi == null || !wifi.isConnectedOrConnecting());
        } else return true;
    }

    public void setUserInfo(User user) {
        setUserInfo("username", user.getUsername());
        setUserInfo("number", user.getPhoneNumber());
        setUserInfo("gender", user.getGender());
        setUserInfo("className", user.getClassName());
        setUserInfo("email", user.getEmail());
        setUserInfo("Birthday", user.getBirthday());
        setUserInfo("BirthYear", user.getBirthYear());
        setUserInfo("BirthMonth", user.getBirthMonth());
        setUserInfo("nickname", user.getNickname());
        setUserInfo("category", user.getCategory());
        setUserInfo("description", user.getDescription());
        setUserInfo("LastSeenPrivacy", user.getLastSeenPrivacy());
        setUserInfo("ProfilePrivacy", user.getProfilePrivacy());
        setUserInfo("AboutPrivacy", user.getAboutPrivacy());
        setUserInfo("LocationPrivacy", user.getLocationPrivacy());
        setUserInfo("EmailPrivacy", user.getEmailPrivacy());
        setUserInfo("PhonePrivacy", user.getPhonePrivacy());
        setUserInfo("BirthdayPrivacy", user.getBirthdayPrivacy());
    }

    private void setUserInfo(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }
}
