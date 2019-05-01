package com.sreekanth.dev.ilahianz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sreekanth.dev.ilahianz.Supports.ViewSupport;
import com.sreekanth.dev.ilahianz.model.User;

public class ShowProfilePicture extends AppCompatActivity {

    ImageView profileImage;

    DatabaseReference reference, reference1;
    FirebaseUser firebaseUser;
    Intent intent;
    User myInfo = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);

        setContentView(R.layout.show_profile_picture);
        profileImage = findViewById(R.id.profile_Image);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        intent = getIntent();
        reference1 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myInfo = dataSnapshot.getValue(User.class);
                reference = FirebaseDatabase.getInstance().getReference("Users").child(intent.getStringExtra("userId"));
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        assert user != null;
                        ViewSupport.setProfileWithPrivacy(user, myInfo, profileImage, 1000, 1000);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
