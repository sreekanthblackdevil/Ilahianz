package com.sreekanth.dev.ilahianz;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sreekanth.dev.ilahianz.model.User;

import java.util.Objects;

public class signinActivity extends AppCompatActivity {

    Button signIn_btn, signUp_btn;
    EditText password, email;
    start start = new start();
    Dialog progressbar;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

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
                                        reference = FirebaseDatabase.getInstance().getReference("Users")
                                                .child(firebaseUser.getUid());
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                User user = dataSnapshot.getValue(User.class);
                                                setUserInfo(user);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
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
