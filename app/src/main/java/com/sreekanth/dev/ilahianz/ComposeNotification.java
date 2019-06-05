package com.sreekanth.dev.ilahianz;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static com.sreekanth.dev.ilahianz.model.Literals.SP_CLASS_NAME;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_USERNAME;

public class ComposeNotification extends AppCompatActivity {

    TextView from, to;
    EditText subject, message;
    ImageButton bulb, close, send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_notification);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        bulb = findViewById(R.id.bulb);
        close = findViewById(R.id.close);
        send = findViewById(R.id.send_btn);
        from.setText(String.format("%s %s", getUserInfo(SP_USERNAME), getUserInfo(SP_CLASS_NAME)));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSheet();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.equals(to.getText().toString(), "Choose here"))
                    if (!TextUtils.isEmpty(message.getText().toString()))
                        sendNotice();
            }
        });
    }

    private String getUserInfo(String key) {
        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

    private void sendNotice() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
        String time = format.format(calendar.getTime());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("subject", subject.getText().toString());
        hashMap.put("from", from.getText().toString());
        hashMap.put("message", message.getText().toString());
        hashMap.put("Uid", message.getText().toString());
        hashMap.put("timeStamp", time);
        reference.child("Notifications").push().setValue(hashMap);
    }

    private void openSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.choose_list);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog.show();
    }
}
