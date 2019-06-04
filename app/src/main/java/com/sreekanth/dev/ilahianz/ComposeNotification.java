package com.sreekanth.dev.ilahianz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private String getUserInfo(String key) {
        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

    private void sendNotice() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.US);
        String time = format.format(calendar.getTime());
        SimpleDateFormat format1 = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        String date = format1.format(calendar.getTime());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("subject", subject.getText().toString());
        hashMap.put("from", from.getText().toString());
        hashMap.put("message", message.getText().toString());
        hashMap.put("time", time);
        hashMap.put("date", date);
        reference.child("Notifications").push().setValue(hashMap);
    }

}
