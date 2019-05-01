package com.sreekanth.dev.ilahianz;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sreekanth.dev.ilahianz.Fragments.ChatskFragment;
import com.sreekanth.dev.ilahianz.Fragments.TeachersFragment;
import com.sreekanth.dev.ilahianz.Fragments.UsersFragment;
import com.sreekanth.dev.ilahianz.Supports.Supports;
import com.sreekanth.dev.ilahianz.Supports.ViewSupport;
import com.sreekanth.dev.ilahianz.model.Chat;
import com.sreekanth.dev.ilahianz.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sreekanth.dev.ilahianz.UserProfile.REQUEST_CALL;

public class ChatActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    CircleImageView profile_image;
    TextView username;
    Dialog profile_view;
    Calendar calendar;
    RelativeLayout offline;
    TabLayout tabLayout;
    Button retry;
    public String PhoneNumber = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        profile_image = findViewById(R.id.profile_Image);
        username = findViewById(R.id.username);
        offline = findViewById(R.id.offline);
        tabLayout = findViewById(R.id.tab_layout);
        retry = findViewById(R.id.retry_btn);
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (!Supports.Connected(ChatActivity.this)) {
            setUserInfo();
        } else {
            tabLayout.setVisibility(View.GONE);
            offline.setVisibility(View.VISIBLE);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retry.setVisibility(View.GONE);
                    if (Supports.Connected(ChatActivity.this))
                        retry.setVisibility(View.VISIBLE);
                    else {
                        setUserInfo();
                    }
                }
            });
        }

    }

    private void setUserInfo() {
        tabLayout.setVisibility(View.VISIBLE);
        offline.setVisibility(View.GONE);
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());
                ViewSupport.setThumbProfileImage(user, profile_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilePopup(ChatActivity.this);
            }
        });

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilePopup(ChatActivity.this);
            }
        });
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.view_pager);

        tabLayout.setupWithViewPager(viewPager);

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                int unread = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;
                    if (TextUtils.equals(chat.getReceiver(), firebaseUser.getUid()) && !chat.isIsseen()) {
                        unread++;
                    }
                }
                if (unread == 0) {
                    viewPagerAdapter.addFragment(new ChatskFragment(), "Chats");
                } else {
                    viewPagerAdapter.addFragment(new ChatskFragment(), "(" + unread + ")" + "Chats");
                }

                viewPagerAdapter.addFragment(new UsersFragment(), "Students");
                viewPagerAdapter.addFragment(new TeachersFragment(), "Staff");

                viewPager.setAdapter(viewPagerAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.about:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ChatActivity.this, AboutActivity.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(ChatActivity.this, SettingsActivity.class));
                return true;
        }
        return false;
    }

    private void status(String status, String last_seen) {
        reference = FirebaseDatabase.getInstance()
                .getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        hashMap.put("lastSeen", last_seen);
        reference.updateChildren(hashMap);

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
        SimpleDateFormat format1 = new SimpleDateFormat("EEE, MMM d, yyyy");
        String date = format1.format(calendar.getTime());
        String time = format.format(calendar.getTime());
        status(time, date);

    }

    public void ProfilePopup(Context context) {
        profile_view = new Dialog(context);
        profile_view.setContentView(R.layout.profile_view);
        Objects.requireNonNull(profile_view.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final TextView popup_usename = profile_view.findViewById(R.id.username_profile);
        final ImageView proImage = profile_view.findViewById(R.id.profile_Image);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                popup_usename.setText(user.getUsername());
                ViewSupport.setProfileImage(user, proImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Button save_image, change_image;
        save_image = profile_view.findViewById(R.id.save_pic);
        change_image = profile_view.findViewById(R.id.pro_change);
        save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(ChatActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                BitmapDrawable draw = (BitmapDrawable) proImage.getDrawable();
                Bitmap bitmap = draw.getBitmap();

                FileOutputStream outStream;
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Ilahianz/" + popup_usename);
                if (!dir.exists())
                    dir.mkdirs();
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);
                try {
                    outStream = new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                    Toast.makeText(ChatActivity.this, "saved to the gallery", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, ProfileActivity.class));
            }
        });
        profile_view.show();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void BirthdayCheck() {

    }

    public void makePhoneCall(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            if (PhoneNumber != null && !TextUtils.equals(PhoneNumber, "Not Provided")) {
                String dial = "tel:" + PhoneNumber;
                context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } else {
                Toast.makeText(context, "Phone number not provided", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(ChatActivity.this);
            }
        }
    }
}
