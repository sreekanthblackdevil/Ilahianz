package com.sreekanth.dev.ilahianz;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class signinActivity extends AppCompatActivity {

    Button signIn_btn, signUp_btn;
    EditText password, email;
    start start = new start();
    Dialog progressbar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        signIn_btn = findViewById(R.id.login_btn);
        signUp_btn = findViewById(R.id.signup_btn);
        password = findViewById(R.id.passwordField);
        email = findViewById(R.id.EmailField);
        progressbar = new Dialog(signinActivity.this);
        auth = FirebaseAuth.getInstance();
        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signinActivity.this, signupActivity.class);
                startActivity(intent);
            }
        });
        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_password = password.getText().toString();
                String txt_email = email.getText().toString();
                if (start.Connected(signinActivity.this)) {
                    start.popupNoInternet(signinActivity.this);
                } else if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(signinActivity.this, "All fields are required !", Toast.LENGTH_SHORT).show();
                } else {
                    progressbar.setContentView(R.layout.progressbar);
                    Objects.requireNonNull(progressbar.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    progressbar.show();
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressbar.dismiss();
                                        Intent intent = new Intent(signinActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(signinActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                        progressbar.cancel();
                                    }
                                }
                            });
                }
            }
        });
    }


}
