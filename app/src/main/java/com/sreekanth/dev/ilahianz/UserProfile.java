package com.sreekanth.dev.ilahianz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sreekanth.dev.ilahianz.Supports.Supports;
import com.sreekanth.dev.ilahianz.Supports.ViewSupport;
import com.sreekanth.dev.ilahianz.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class UserProfile extends AppCompatActivity {

    FirebaseUser fuser;
    DatabaseReference reference, reference1;
    CollapsingToolbarLayout toolbarLayout;
    ImageView profile_Image;
    TextView online, lastSeen, email, about, locationStatus, phone;
    Calendar calendar;
    ImageView callBtn;
    User myInfo = new User();
    LinearLayout info;
    RelativeLayout progress;
    String latitude;
    String longitude;
    TextView birthday;
    String PhoneNumber = null;
    Intent intent;
    AppBarLayout appBar;
    String username;
    String category;
    FloatingActionButton locationBtn;
    public static final int REQUEST_CALL = 635;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        appBar = findViewById(R.id.app_bar);
        locationBtn = findViewById(R.id.fab);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone_number);
        about = findViewById(R.id.about_text);
        lastSeen = findViewById(R.id.lastSeen);
        calendar = Calendar.getInstance();
        profile_Image = findViewById(R.id.profile_Image);
        toolbarLayout = findViewById(R.id.collapsingtoolbar);
        locationStatus = findViewById(R.id.locationStatus);
        latitude = null;
        longitude = null;
        callBtn = findViewById(R.id.call_btn);
        online = findViewById(R.id.online);
        info = findViewById(R.id.info);
        birthday = findViewById(R.id.birth_date);
        progress = findViewById(R.id.progress);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        info.setVisibility(View.GONE);
        intent = getIntent();
        progress.setVisibility(View.VISIBLE);
        if (Supports.Connected(this)) {

        } else {
            init();
        }

        profile_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserProfile.this,
                        new Pair<View, String>(profile_Image, "imageTransition"));
                startActivity(new Intent(UserProfile.this, ShowProfilePicture.class)
                        .putExtra("userId", intent.getStringExtra("UId")), options.toBundle());
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.equals(category, "Teacher")) {
                    if (latitude != null && longitude != null) {
                        if (TextUtils.equals("Not Provided", longitude)
                                || TextUtils.equals("Not Provided", longitude)) {
                            Snackbar.make(v, "Location not Provided ", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        } else {
                            LatLng location = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                            MapsActivity.setLOCATION(location, username);
                            startActivity(new Intent(UserProfile.this, MapsActivity.class));
                        }

                    } else {
                        Snackbar.make(v, "Location not available ", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(v, "You cannot access Teacher's Location", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

    }

    private void init() {

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference1 = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myInfo = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(intent.getStringExtra("UId"));
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                assert user != null;
                toolbarLayout.setTitle(user.getUsername());
                info.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                username = user.getUsername();
                category = user.getCategory();
                //////////////////////////BIRTHDAY PRIVACY//////////////////////////
                /////////////////////////////////
                if (!TextUtils.equals(user.getBirthdayPrivacy(), "Nobody")) {
                    if (TextUtils.equals(user.getBirthdayPrivacy(), "Students Only")
                            && TextUtils.equals(myInfo.getCategory(), "Student")) {
                        birthday.setText(getBirthday(user));
                    } else if (TextUtils.equals(user.getBirthdayPrivacy(), "Teachers Only")
                            && TextUtils.equals(myInfo.getCategory(), "Teacher")) {
                        birthday.setText(getBirthday(user));
                    } else if (TextUtils.equals(user.getBirthdayPrivacy(), "Everyone")) {
                        birthday.setText(getBirthday(user));
                    } else {
                        birthday.setText("Content is private");
                    }
                } else {
                    birthday.setText("Content is private");
                }

//////////////////////////PHONE PRIVACY////////////////////////////////////////
                //////////////////////////////////
                if (!user.getPhonePrivacy().equals("Nobody")) {
                    if (myInfo.getCategory().equals("Student")) {

                        switch (user.getPhonePrivacy()) {
                            case "Student Only":
                                phone.setText(user.getPhoneNumber());
                                callBtn.setVisibility(View.VISIBLE);
                                phone.setTextColor(Color.parseColor("#000000"));
                                break;
                            case "Everyone":
                                phone.setText(user.getPhoneNumber());
                                callBtn.setVisibility(View.VISIBLE);
                                phone.setTextColor(Color.parseColor("#000000"));
                                break;
                            default:
                                phone.setText("Phone Number is Private");
                                phone.setTextColor(Color.parseColor("#FFD50000"));
                                callBtn.setVisibility(View.GONE);
                                break;
                        }
                    } else {
                        switch (user.getPhonePrivacy()) {
                            case "Teachers Only":
                                phone.setText(user.getPhoneNumber());
                                callBtn.setVisibility(View.VISIBLE);
                                phone.setTextColor(Color.parseColor("#000000"));
                                break;
                            case "Everyone":
                                phone.setText(user.getPhoneNumber());
                                callBtn.setVisibility(View.VISIBLE);
                                phone.setTextColor(Color.parseColor("#000000"));
                                break;
                            default:
                                phone.setText("Phone Number is Private");
                                phone.setTextColor(Color.parseColor("#FFD50000"));
                                callBtn.setVisibility(View.GONE);
                                break;
                        }
                    }
                } else {
                    phone.setText("Phone Number is Private");
                    phone.setTextColor(Color.parseColor("#FFD50000"));
                    callBtn.setVisibility(View.GONE);
                }
                callBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhoneNumber = user.getPhoneNumber();
                        if (PhoneNumber != null && !TextUtils.equals(PhoneNumber, "Not Provided"))
                            makePhoneCall();
                    }
                });
/////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////LOCATION PRIVACY///////////////////////////////////
                if (!user.getLocationPrivacy().equals("Nobody")) {
                    if (myInfo.getCategory().equals("Student")) {
                        switch (user.getLocationPrivacy()) {
                            case "Students Only":
                                locationStatus.setText("Location Available");
                                locationBtn.setEnabled(true);
                                latitude = user.getLatitude();
                                longitude = user.getLongitude();
                            case "Everyone":
                                locationStatus.setText("Location Available");
                                locationBtn.setEnabled(true);
                                latitude = user.getLatitude();
                                longitude = user.getLongitude();
                                break;
                            default:
                                locationStatus.setText("Location Not Available");
                                locationStatus.setTextColor(Color.parseColor("#FFD50000"));
                                locationBtn.setEnabled(false);
                                break;
                        }
                    } else {
                        switch (user.getLocationPrivacy()) {
                            case "Teachers Only":
                                locationStatus.setText("Location Available");
                                locationBtn.setEnabled(true);
                                latitude = user.getLatitude();
                                longitude = user.getLongitude();
                                break;
                            case "Everyone":
                                locationStatus.setText("Location Available");
                                locationBtn.setEnabled(true);
                                latitude = user.getLatitude();
                                longitude = user.getLongitude();
                                break;
                            default:
                                locationStatus.setText("Location Not Available");
                                locationStatus.setTextColor(Color.parseColor("#FFD50000"));
                                locationBtn.setEnabled(false);
                                break;
                        }
                    }
                } else {
                    locationStatus.setText("Location Not Available");
                    locationStatus.setTextColor(Color.parseColor("#FFD50000"));
                    locationBtn.setEnabled(false);
                }
                ///////////////////////////////////////////////////
                //////////////////////////////////////////////////
/////////////////////////////EMAIL PRIVACY SETTINGS///////////////////////////////////////////
                //////////////////////////////////////////////////////
                if (!user.getEmailPrivacy().equals("Nobody")) {
                    if (myInfo.getCategory().equals("Student")) {
                        switch (user.getEmailPrivacy()) {
                            case "Students Only":
                                email.setText(user.getEmail());
                                break;
                            case "Everyone":
                                email.setText(user.getEmail());
                                break;
                            default:
                                email.setText("Content is Private");
                                email.setTextColor(Color.parseColor("#FFD50000"));
                                break;
                        }
                    } else {
                        switch (user.getEmailPrivacy()) {
                            case "Teachers Only":
                                email.setText(user.getEmail());
                                break;
                            case "Everyone":
                                email.setText(user.getEmail());
                                break;
                            default:
                                email.setText("Content is Private");
                                email.setTextColor(Color.parseColor("#FFD50000"));
                                break;
                        }
                    }
                } else {
                    email.setText("Content is Private");
                    email.setTextColor(Color.parseColor("#FFD50000"));
                }
//////////////////////////////////////////////////////////////////////////////////////////////////
                ///////////////////////////////////////////////////////////////////
                ////////////////ABOUT SETTINGS///////////////////////////////
                //////////////////////////////////////////////////
                if (!user.getAboutPrivacy().equals("Nobody")) {
                    if (myInfo.getCategory().equals("Student")) {
                        switch (user.getAboutPrivacy()) {
                            case "Students Only":
                                about.setText(user.getDescription());
                                break;
                            case "Everyone":
                                about.setText(user.getDescription());
                                break;
                            default:
                                about.setText("Content is Private");
                                about.setTextColor(Color.parseColor("#FFD50000"));
                                break;
                        }
                    } else {
                        switch (user.getAboutPrivacy()) {
                            case "Teachers Only":
                                about.setText(user.getDescription());
                                break;
                            case "Everyone":
                                about.setText(user.getDescription());
                                break;
                            default:
                                about.setText("Content is Private");
                                about.setTextColor(Color.parseColor("#FFD50000"));
                                break;
                        }
                    }
                } else {
                    about.setText("Content is Private");
                    about.setTextColor(Color.parseColor("#FFD50000"));
                }
                //////////////////////////////////////////////
                ////////////////////////////////////////////////
                ViewSupport.setProfileWithPrivacy(user, myInfo, profile_Image, 1000, 1000);
////////////////////////////////PRIVACY SETTINGS LAST SEEN/////////////////////////////////////
                ////////////////////////////////////////////
                switch (user.getStatus()) {
                    case "online":
                        online.setVisibility(View.VISIBLE);
                        online.setText("Online");
                        lastSeen.setVisibility(View.GONE);
                        break;
                    default:
                        online.setVisibility(View.VISIBLE);
                        online.setText(user.getStatus());
                        if (!user.getLastSeenPrivacy().equals("Nobody")) {
                            if (myInfo.getCategory().equals("Student") && user.getLastSeenPrivacy().equals("Students Only")) {
                                lastSeen.setText(user.getLastSeen());
                                online.setText(user.getStatus());
                            } else if (myInfo.getCategory().equals("Teacher") && user.getLastSeenPrivacy().equals("Teachers Only")) {
                                lastSeen.setText(user.getLastSeen());
                                online.setText(user.getStatus());
                            } else if (user.getLastSeenPrivacy().equals("Everyone")) {
                                lastSeen.setText(user.getLastSeen());
                                online.setText(user.getStatus());
                            } else {
                                lastSeen.setText("Content is Private");
                                lastSeen.setTextColor(Color.parseColor("#FFD50000"));
                                online.setVisibility(View.GONE);
                            }
                        } else {
                            lastSeen.setText("Content is Private");
                            lastSeen.setTextColor(Color.parseColor("#FFD50000"));
                            online.setVisibility(View.GONE);
                        }
                        break;
                }// }/////////////////////////////////////////////////////////////////////////
                ///////////////////////////////////////////


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void status(String status, String last_seen) {
        reference = FirebaseDatabase.getInstance()
                .getReference("Users").child(fuser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        hashMap.put("lastSeen", last_seen);
        reference.updateChildren(hashMap);

    }

    ///////////////////////////////////PROFILE PRIVACY//////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String getBirthday(User user) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(user.getBirthday()));
        calendar.set(Calendar.MONTH, Integer.parseInt(user.getBirthMonth()));
        calendar.set(Calendar.YEAR, Integer.parseInt(user.getBirthYear()));
        SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");
        return format.format(calendar.getTime());
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online", "active");
    }

    @Override
    protected void onPause() {
        super.onPause();
        calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
        String time = format.format(calendar.getTime());
        SimpleDateFormat format1 = new SimpleDateFormat("EEE, MMM d, yyyy");
        String date = format1.format(calendar.getTime());
        status(time, date);
    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            if (PhoneNumber != null) {
                String dial = "tel:" + PhoneNumber;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            }
        }
    }

}
