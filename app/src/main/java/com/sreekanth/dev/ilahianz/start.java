package com.sreekanth.dev.ilahianz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sreekanth.dev.ilahianz.Supports.Supports;


/*
 * Written by Sreekanth K R in (2018)
 * */

public class start extends AppCompatActivity {
    public static boolean newUser;
    Handler handler = new Handler();
    FirebaseUser firebaseUser;

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
        if (Supports.Connected(start.this)) {
            Toast.makeText(this, "No Internet !", Toast.LENGTH_SHORT).show();
        }
        handler.postDelayed(runnable, 1000);
        newUser = false;
    }
}
