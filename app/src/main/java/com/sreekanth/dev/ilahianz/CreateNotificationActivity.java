

package com.sreekanth.dev.ilahianz;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sreekanth.dev.ilahianz.Fragments.APIService;
import com.sreekanth.dev.ilahianz.Notifications.Client;
import com.sreekanth.dev.ilahianz.Notifications.Data;
import com.sreekanth.dev.ilahianz.Notifications.MyResponse;
import com.sreekanth.dev.ilahianz.Notifications.Sender;
import com.sreekanth.dev.ilahianz.Notifications.Token;
import com.sreekanth.dev.ilahianz.Supports.Graphics;
import com.sreekanth.dev.ilahianz.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sreekanth.dev.ilahianz.model.Literals.SP_CATEGORY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_USERNAME;


public class CreateNotificationActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    FirebaseUser firebaseUser;
    EditText heading, message;
    ImageButton templates;
    FloatingActionButton sendBtn;
    String colorRGB;
    Dialog dialog;
    boolean notify = false;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);

        Button green, blue, red, dark, color_btn;
        green = findViewById(R.id.color_green);
        blue = findViewById(R.id.color_blue);
        red = findViewById(R.id.color_red);
        dark = findViewById(R.id.color_dark);
        color_btn = findViewById(R.id.color_btn);
        frameLayout = findViewById(R.id.frame);
        templates = findViewById(R.id.templates);
        heading = findViewById(R.id.heading);
        sendBtn = findViewById(R.id.send_btn);
        message = findViewById(R.id.fullscreen_content);
        apiService = Client.getClient("http://fcm.googleapis.com").create(APIService.class);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.create_notification);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        String color = preferences.getString("notificationColor", "none");
        if (!TextUtils.equals(color, "none")) {
            colorRGB = color;
            assert color != null;
            frameLayout.setBackgroundColor(Integer.parseInt(color));
        } else {
            colorRGB = String.valueOf(getResources().getColor(R.color.dark));
        }


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(heading.getText().toString())
                        && !TextUtils.isEmpty(message.getText().toString())) {
                    createNotification(message.getText().toString(),
                            heading.getText().toString(), "C4A", "Public", colorRGB);
                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("notificationColor", colorRGB);
                    editor.apply();
                    finish();
                }
            }
        });

        templates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTemplates();
            }
        });

        message.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        message.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setBackgroundColor(getResources().getColor(R.color.green));
                colorRGB = String.valueOf(getResources().getColor(R.color.green));
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setBackgroundColor(getResources().getColor(R.color.blue));
                colorRGB = String.valueOf(getResources().getColor(R.color.blue));
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setBackgroundColor(getResources().getColor(R.color.red));
                colorRGB = String.valueOf(getResources().getColor(R.color.red));
            }
        });
        dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setBackgroundColor(getResources().getColor(R.color.dark));
                colorRGB = String.valueOf(getResources().getColor(R.color.dark));
            }
        });
        color_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = Graphics.getRandomColorMaterial();
                colorRGB = String.valueOf(color);
                frameLayout.setBackgroundColor(color);
            }
        });
    }

    private String getUserInfo(String key) {
        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

    private void createNotification(String Content, String Heading, String Target, String Type, String Color) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
        String time = format.format(calendar.getTime());
        SimpleDateFormat format1 = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        String date = format1.format(calendar.getTime());
        String username = getUserInfo(SP_USERNAME);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Heading", Heading);
        hashMap.put("Content", Content);
        hashMap.put("Target", Target);
        hashMap.put("Type", Type);
        hashMap.put("Color", Color);
        hashMap.put("Time", time);
        hashMap.put("Category", getUserInfo(SP_CATEGORY));
        hashMap.put("From", username);
        hashMap.put("Date", date);
        hashMap.put("UId", firebaseUser.getUid());
        reference.child("Notifications").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(CreateNotificationActivity.this,
                            "Notification delivered", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateNotificationActivity.this,
                            "Failed to deliver", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                if (notify) {
                    sendNotification(firebaseUser.getUid(), user.getUsername(), heading.getText().toString());
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(final String receiver, final String username, final String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = reference.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.mipmap.owl, username + " : " +
                            heading.getText().toString(), "New Message", firebaseUser.getUid());

                    assert token != null;
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender).
                            enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        assert response.body() != null;
                                        if (response.body().success == 1) {
                                            Toast.makeText(CreateNotificationActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openTemplates() {
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageButton cancel = dialog.findViewById(R.id.close);
        final RadioGroup templates = dialog.findViewById(R.id.templates_rg);
        Button create = dialog.findViewById(R.id.crete_btn);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = templates.getCheckedRadioButtonId();
                RadioButton data = dialog.findViewById(id);
                if (data != null) {
                    heading.setText(data.getText().toString());
                }
                dialog.dismiss();
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
}
