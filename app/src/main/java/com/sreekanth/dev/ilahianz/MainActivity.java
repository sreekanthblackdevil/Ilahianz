package com.sreekanth.dev.ilahianz;
/*
 * This Code
 * Created in 2019
 * Author Sreekanth K R
 * Name Ilahianz
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.sreekanth.dev.ilahianz.Supports.locationService;
import com.sreekanth.dev.ilahianz.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sreekanth.dev.ilahianz.model.Literals.ATTENDANCE_WEBSITE;
import static com.sreekanth.dev.ilahianz.model.Literals.DEFAULT;
import static com.sreekanth.dev.ilahianz.model.Literals.ILAHIA_LOCATION;
import static com.sreekanth.dev.ilahianz.model.Literals.ILAHIA_WEBSITE;
import static com.sreekanth.dev.ilahianz.model.Literals.IMAGE_REQUEST;
import static com.sreekanth.dev.ilahianz.model.Literals.LOCATION_REQUEST;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_ABOUT;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_ABOUT_PRIVACY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIO;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTHDAY_PRIVACY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_DAY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_MONTH;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_YEAR;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_CATEGORY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_CITY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_CLASS_NAME;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_DEPARTMENT;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_DISTRICT;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_EMAIL;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_EMAIL_PRIVACY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_GENDER;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_LAST_SEEN;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_LOCATION_PRIVACY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_NICKNAME;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_PHONE_PRIVACY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_PH_NUMBER;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_PROFILE_PRIVACY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_USERNAME;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    LinearLayout chat,
            Notes,
            remainder,
            teachers,
            notifications,
            help,
            location,
            attendance,
            website;
    Dialog profile;
    DatabaseReference reference;
    RelativeLayout profile_view;
    RelativeLayout connectionStatus;
    ProgressBar progressbar;
    CircleImageView Header_DP;
    TextView header_username,
            initial, email, retry;
    User myInfo = new User();
    LatLng CurrentLocation;
    boolean fetched = false;
    locationService locationService = new locationService();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        profile = new Dialog(this);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGE_REQUEST);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_REQUEST);


        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        Notes = findViewById(R.id.Notes);
        remainder = findViewById(R.id.remainder);
        notifications = findViewById(R.id.notification);
        teachers = findViewById(R.id.teachers);
        help = findViewById(R.id.help);
        chat = findViewById(R.id.chat);
        location = findViewById(R.id.location);
        attendance = findViewById(R.id.attendance);
        website = findViewById(R.id.website);
        profile_view = header.findViewById(R.id.profile);
        connectionStatus = header.findViewById(R.id.retry_btn);
        progressbar = header.findViewById(R.id.progressBar);
        Header_DP = header.findViewById(R.id.imageView);
        header_username = header.findViewById(R.id.username);
        initial = header.findViewById(R.id.initial);
        email = header.findViewById(R.id.email);
        retry = header.findViewById(R.id.retry_button);

        init();

        Header_DP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                        new Pair<View, String>(Header_DP, "imageTransition"));
                startActivity(new Intent(MainActivity.this, ProfileActivity.class), options.toBundle());
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionStatus.setVisibility(View.GONE);
                init();
            }
        });
        ////////////////////////////////////////////////////
        teachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TeachersList.class);
                startActivity(intent);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UploadProfileActivity.class);
                startActivity(intent);
            }
        });
        remainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Startup.class);
                startActivity(intent);
            }
        });
        Notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowNotificationActivity.class);
                startActivity(intent);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
        attendance.setOnClickListener(new View.OnClickListener() { //Visit the Ilahia Website
            @Override
            public void onClick(View v) {
                WebViewActivity.setURL(ATTENDANCE_WEBSITE);
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.setURL(ILAHIA_WEBSITE);
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity.setLOCATION(ILAHIA_LOCATION, "Ilahia College of Arts & Science");
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
        locationService.init(this);
    }

    private void adaptUserInfo() {
        if (!TextUtils.equals(getUserInfo(SP_NICKNAME), DEFAULT)) {
            header_username.setText(String.format("%s (%s)", getUserInfo(SP_USERNAME).toUpperCase(),
                    getUserInfo(SP_NICKNAME)));
        } else {
            header_username.setText(getUserInfo(SP_USERNAME).toUpperCase());
        }
        email.setText(getUserInfo(SP_EMAIL));
    }

    private void init() {
        adaptUserInfo();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        initial.setText(getUserInfo(SP_CLASS_NAME));

        if (Supports.Connected(this)) {
            drawer.openDrawer(GravityCompat.START);
            profile_view.setVisibility(View.GONE);
            connectionStatus.setVisibility(View.VISIBLE);
        } else {
            profile_view.setVisibility(View.VISIBLE);
            progressbar.setVisibility(View.VISIBLE);
            connectionStatus.setVisibility(View.GONE);
            reference = FirebaseDatabase.getInstance().getReference("Users")
                    .child(Objects.requireNonNull(firebaseAuth.getUid()));
            reference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    progressbar.setVisibility(View.GONE);
                    assert user != null;
                    setUserInfo(user);
                    fetched = true;
                    myInfo = user;
                    ViewSupport.setThumbProfileImage(user, Header_DP);
                    locationService.init(MainActivity.this);
                    adaptUserInfo();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Could not Connect with Database", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String getUserInfo(String key) {
        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class)
                    .putExtra("ProfileURL", myInfo.getThumbnailURL()));
        } else if (id == R.id.action_help) {
            startActivity(new Intent(MainActivity.this, HelpActivity.class));
        } else if (id == R.id.action_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        } else if (id == R.id.action_Logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(MainActivity.this, signinActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_help) {
            startActivity(new Intent(MainActivity.this, HelpActivity.class));
        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(MainActivity.this, signinActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        } else if (id == R.id.nav_invite) {
            Toast.makeText(this, "Invite a Ilahianz", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_chat) {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setUserInfo(User user) {
        setUserInfo(SP_USERNAME, user.getUsername());
        setUserInfo(SP_PH_NUMBER, user.getPhoneNumber());
        setUserInfo(SP_GENDER, user.getGender());
        setUserInfo(SP_CLASS_NAME, user.getClassName());
        setUserInfo(SP_EMAIL, user.getEmail());
        setUserInfo(SP_BIRTH_DAY, user.getBirthday());
        setUserInfo(SP_BIRTH_YEAR, user.getBirthYear());
        setUserInfo(SP_BIRTH_MONTH, user.getBirthMonth());
        setUserInfo(SP_NICKNAME, user.getNickname());
        setUserInfo(SP_CATEGORY, user.getCategory());
        setUserInfo(SP_ABOUT, user.getDescription());
        setUserInfo(SP_LAST_SEEN, user.getLastSeenPrivacy());
        setUserInfo(SP_PROFILE_PRIVACY, user.getProfilePrivacy());
        setUserInfo(SP_ABOUT_PRIVACY, user.getAboutPrivacy());
        setUserInfo(SP_LOCATION_PRIVACY, user.getLocationPrivacy());
        setUserInfo(SP_EMAIL_PRIVACY, user.getEmailPrivacy());
        setUserInfo(SP_PHONE_PRIVACY, user.getPhonePrivacy());
        setUserInfo(SP_BIRTHDAY_PRIVACY, user.getBirthdayPrivacy());
        setUserInfo(SP_BIO, user.getBio());
        setUserInfo(SP_CITY, user.getCity());
        setUserInfo(SP_DISTRICT, user.getDistrict());
        setUserInfo(SP_DEPARTMENT, user.getDepartment());

    }

    private void setUserInfo(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void status(String status, String last_seen) {
        reference = FirebaseDatabase.getInstance()
                .getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        hashMap.put("lastSeen", last_seen);
        reference.updateChildren(hashMap);

    }

    private void setLocation(String latitude, String longitude) {
        reference = FirebaseDatabase.getInstance()
                .getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Latitude", latitude);
        hashMap.put("Longitude", longitude);
        reference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        if (!Supports.Connected(this) && fetched) {
            status("online", "active");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                    CurrentLocation = locationService.getLocation();
                    setLocation(String.valueOf(CurrentLocation.latitude),
                            String.valueOf(CurrentLocation.longitude));
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
                    setLocation(getResources().getString(R.string.not_provided),
                            getResources().getString(R.string.not_provided));
                }
            } else {
                locationService.init(this);
                CurrentLocation = locationService.getLocation();
                setLocation(String.valueOf(CurrentLocation.latitude),
                        String.valueOf(CurrentLocation.longitude));
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (!Supports.Connected(this) && fetched) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.US);
            String time = format.format(calendar.getTime());
            SimpleDateFormat format1 = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
            String date = format1.format(calendar.getTime());
            status(time, date);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationService.init(MainActivity.this);
                    CurrentLocation = locationService.getLocation();
                    if (CurrentLocation.longitude != 0.0 || CurrentLocation.latitude != 0.0)
                        setLocation(String.valueOf(CurrentLocation.latitude), String.valueOf(CurrentLocation.longitude));
                    Log.d("MainActivity", "Location Updated");
                } else {
                    setLocation(getResources().getString(R.string.not_provided),
                            getResources().getString(R.string.not_provided));
                }
            } else {
                locationService.init(MainActivity.this);
                CurrentLocation = locationService.getLocation();
                if (CurrentLocation.longitude != 0.0 || CurrentLocation.latitude != 0.0)
                    setLocation(String.valueOf(CurrentLocation.latitude), String.valueOf(CurrentLocation.longitude));
                Log.d("MainActivity", "Location Updated " + CurrentLocation.latitude + " , " + CurrentLocation.longitude);
            }
        }
        super.onPause();
    }


}
