package com.sreekanth.dev.ilahianz;

/**
 * This code
 * Created in 2019
 * Author Sreekanth K R
 * Name Ilahianz
 * Github https://github.com/sreekanthblackdevil/Ilahianz
 */

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sreekanth.dev.ilahianz.model.User;

import java.util.HashMap;
import java.util.Objects;

public class PrivacyActivity extends AppCompatActivity {

    Dialog dialog;
    TextView lastSeen,
            about,
            profile,
            location,
            phoneNumber,
            EmailAddress,
            caption, birthStatus;
    RelativeLayout edit_lastSeen,
            edit_about,
            edit_profile,
            edit_location,
            edit_phone,
            edit_email,
            birthday;
    RadioGroup statusContainer;
    RadioButton nobody, TO, SO, EO;
    ImageView ok, cancel;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    User userInfo = new User();
    ScrollView content;
    RelativeLayout progress;
    AnimationDrawable animationDrawable;
    ImageView progressBar;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        init();
        getUserInfo();

        edit_lastSeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nobody.setVisibility(View.VISIBLE);
                caption.setText("Last Seen");
                switch (userInfo.getLastSeenPrivacy()) {

                    case "Students Only":
                        SO.setChecked(true);
                        break;
                    case "Teachers Only":
                        TO.setChecked(true);
                        break;
                    case "Nobody":
                        nobody.setChecked(true);
                        break;
                    case "Everyone":
                        EO.setChecked(true);
                        break;
                    default:
                        break;
                }
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = statusContainer.getCheckedRadioButtonId();
                        RadioButton value = dialog.findViewById(id);
                        statusUpdate("LastSeenPrivacy", value.getText().toString());
                        dialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        edit_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nobody.setVisibility(View.VISIBLE);
                caption.setText("About");
                switch (userInfo.getAboutPrivacy()) {

                    case "Students Only":
                        SO.setChecked(true);
                        break;
                    case "Teachers Only":
                        TO.setChecked(true);
                        break;
                    case "Nobody":
                        nobody.setChecked(true);
                        break;
                    case "Everyone":
                        EO.setChecked(true);
                        break;
                    default:
                        break;
                }
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = statusContainer.getCheckedRadioButtonId();
                        RadioButton value = dialog.findViewById(id);
                        statusUpdate("AboutPrivacy", value.getText().toString());
                        dialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nobody.setVisibility(View.VISIBLE);
                caption.setText("Phone Number");
                switch (userInfo.getPhonePrivacy()) {

                    case "Students Only":
                        SO.setChecked(true);
                        break;
                    case "Teachers Only":
                        TO.setChecked(true);
                        break;
                    case "Nobody":
                        nobody.setChecked(true);
                        break;
                    case "Everyone":
                        EO.setChecked(true);
                        break;
                    default:
                        break;
                }
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = statusContainer.getCheckedRadioButtonId();
                        RadioButton value = dialog.findViewById(id);
                        statusUpdate("PhonePrivacy", value.getText().toString());
                        dialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        edit_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nobody.setVisibility(View.VISIBLE);
                caption.setText("Location");
                switch (userInfo.getLocationPrivacy()) {

                    case "Students Only":
                        SO.setChecked(true);
                        break;
                    case "Teachers Only":
                        TO.setChecked(true);
                        break;
                    case "Nobody":
                        nobody.setChecked(true);
                        break;
                    case "Everyone":
                        EO.setChecked(true);
                        break;
                    default:
                        break;
                }
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = statusContainer.getCheckedRadioButtonId();
                        RadioButton value = dialog.findViewById(id);
                        statusUpdate("LocationPrivacy", value.getText().toString());
                        dialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nobody.setVisibility(View.VISIBLE);
                caption.setText("Email Address");
                switch (userInfo.getEmailPrivacy()) {

                    case "Students Only":
                        SO.setChecked(true);
                        break;
                    case "Teachers Only":
                        TO.setChecked(true);
                        break;
                    case "Nobody":
                        nobody.setChecked(true);
                        break;
                    case "Everyone":
                        EO.setChecked(true);
                        break;
                    default:
                        break;
                }
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = statusContainer.getCheckedRadioButtonId();
                        RadioButton value = dialog.findViewById(id);
                        statusUpdate("EmailPrivacy", value.getText().toString());
                        dialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caption.setText("Profile Photo");
                nobody.setVisibility(View.GONE);

                switch (userInfo.getProfilePrivacy()) {

                    case "Students Only":
                        SO.setChecked(true);
                        break;
                    case "Teachers Only":
                        TO.setChecked(true);
                        break;
                    case "Nobody":
                        nobody.setChecked(true);
                        break;
                    case "Everyone":
                        EO.setChecked(true);
                        break;
                    default:
                        break;
                }

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = statusContainer.getCheckedRadioButtonId();
                        RadioButton value = dialog.findViewById(id);
                        statusUpdate("ProfilePrivacy", value.getText().toString());
                        dialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caption.setText("Date of Birth");
                nobody.setVisibility(View.VISIBLE);

                switch (userInfo.getBirthdayPrivacy()) {

                    case "Students Only":
                        SO.setChecked(true);
                        break;
                    case "Teachers Only":
                        TO.setChecked(true);
                        break;
                    case "Nobody":
                        nobody.setChecked(true);
                        break;
                    case "Everyone":
                        EO.setChecked(true);
                        break;
                    default:
                        break;
                }

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = statusContainer.getCheckedRadioButtonId();
                        RadioButton value = dialog.findViewById(id);
                        statusUpdate("BirthdayPrivacy", value.getText().toString());
                        dialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    private void init() {
        progressBar = findViewById(R.id.progressBar);
        edit_about = findViewById(R.id.layout3);
        edit_lastSeen = findViewById(R.id.layout1);
        edit_profile = findViewById(R.id.layout2);
        edit_location = findViewById(R.id.LocationPrivacy);
        edit_email = findViewById(R.id.email);
        edit_phone = findViewById(R.id.phone_number);
        lastSeen = findViewById(R.id.lastSeen_privacy);
        about = findViewById(R.id.about_status);
        profile = findViewById(R.id.profile_status);
        location = findViewById(R.id.location_status);
        phoneNumber = findViewById(R.id.phone_status);
        birthday = findViewById(R.id.birthday);
        EmailAddress = findViewById(R.id.email_status);
        progress = findViewById(R.id.progress);
        content = findViewById(R.id.content);
        birthStatus = findViewById(R.id.birthStatus);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.last_seen);
        statusContainer = dialog.findViewById(R.id.rg_status);
        nobody = dialog.findViewById(R.id.nobody);
        caption = dialog.findViewById(R.id.caption1);
        ok = dialog.findViewById(R.id.ok_btn);
        TO = dialog.findViewById(R.id.to);
        EO = dialog.findViewById(R.id.eo);
        SO = dialog.findViewById(R.id.so);
        cancel = dialog.findViewById(R.id.cancel_btn);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        content.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        progressBar.setBackgroundResource(R.drawable.fly);
        animationDrawable = (AnimationDrawable) progressBar.getBackground();
        animationDrawable.start();
    }

    private void setUserInfo(User userInfo) {
        lastSeen.setText(userInfo.getLastSeenPrivacy());
        about.setText(userInfo.getAboutPrivacy());
        profile.setText(userInfo.getProfilePrivacy());
        location.setText(userInfo.getLocationPrivacy());
        phoneNumber.setText(userInfo.getPhonePrivacy());
        EmailAddress.setText(userInfo.getPhonePrivacy());
        birthStatus.setText(userInfo.getBirthdayPrivacy());
    }

    private void statusUpdate(String key, String value) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(key, value);
        reference.updateChildren(hashMap);

    }

    private void getUserInfo() {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(User.class);
                assert userInfo != null;
                setUserInfo(userInfo);
                content.setVisibility(View.VISIBLE);
                animationDrawable.stop();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
