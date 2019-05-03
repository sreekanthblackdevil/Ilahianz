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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sreekanth.dev.ilahianz.Supports.Supports;
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
    RelativeLayout internetFailed;
    Button retry;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
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
        internetFailed = findViewById(R.id.internet_connection);
        retry = findViewById(R.id.retry_btn);

        if (!Supports.Connected(this)) {
            content.setVisibility(View.VISIBLE);
            progress.setVisibility(View.VISIBLE);
            internetFailed.setVisibility(View.GONE);
            init();
            getUserInfo();
        } else {
            content.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            internetFailed.setVisibility(View.VISIBLE);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    init();
                    getUserInfo();
                }
            });

        }
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
        internetFailed.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
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
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(key, value);
        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(PrivacyActivity.this, "Changes applied", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PrivacyActivity.this, "Failed to apply changes", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                internetFailed.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
