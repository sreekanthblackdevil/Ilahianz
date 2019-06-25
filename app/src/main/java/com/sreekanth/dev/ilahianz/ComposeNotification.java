package com.sreekanth.dev.ilahianz;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    FirebaseUser fuser;
    BottomSheetDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_notification);

        dialog = new BottomSheetDialog(this);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        bulb = findViewById(R.id.bulb);
        close = findViewById(R.id.close);
        send = findViewById(R.id.send_btn);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
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
                if (!TextUtils.equals(to.getText().toString(), "Choose here") &&
                        !TextUtils.isEmpty(message.getText().toString()))
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
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Heading", subject.getText().toString());
        hashMap.put("from", fuser.getUid());
        hashMap.put("Content", message.getText().toString());
        hashMap.put("Target", to.getText().toString());
        hashMap.put("Time", time);
        reference.child("Notice").push().setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isComplete()) {
                    Toast.makeText(ComposeNotification.this,
                            "Failed to create notification", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openSheet() {
        dialog.setContentView(R.layout.choose_list);
        Button select;
        select = dialog.findViewById(R.id.select);
        assert select != null;
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
