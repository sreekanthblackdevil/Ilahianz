package com.sreekanth.dev.ilahianz;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sreekanth.dev.ilahianz.Supports.ViewSupport;
import com.sreekanth.dev.ilahianz.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    TextView about;
    TextView username;
    CircleImageView proImage;
    DatabaseReference reference;
    FirebaseUser fuser;
    RelativeLayout profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        RelativeLayout privacy, notification, chats, help, invite;
        privacy = findViewById(R.id.relat_layout2);
        notification = findViewById(R.id.relat_layout3);
        chats = findViewById(R.id.relat_layout4);
        help = findViewById(R.id.relat_layout5);
        invite = findViewById(R.id.relat_layout6);
        profile = findViewById(R.id.relat_layout);
        username = findViewById(R.id.username);
        about = findViewById(R.id.about_text);
        proImage = findViewById(R.id.profile_Image);

        username.setText(getUserInfo("username"));
        about.setText(getUserInfo("description"));

        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                ViewSupport.setProfileImage(user, proImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        proImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //animation
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SettingsActivity.this,
                        new Pair<View, String>(proImage, "imageTransition"));
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class), options.toBundle());
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //animation
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SettingsActivity.this,
                        new Pair<View, String>(proImage, "imageTransition"));
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class), options.toBundle());
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, PrivacyActivity.class));
            }
        });
    }

    public String getUserInfo(String key) {
        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        return preferences.getString(key, "none");
    }
}
