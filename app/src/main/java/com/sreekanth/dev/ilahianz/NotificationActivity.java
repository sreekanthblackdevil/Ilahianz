package com.sreekanth.dev.ilahianz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

public class NotificationActivity extends AppCompatActivity {

    FloatingActionButton fab_public, fab_local, fab_teachers_only;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        fab_local = findViewById(R.id.local_fab);
        fab_teachers_only = findViewById(R.id.teachers_only_fab);
        fab_public = findViewById(R.id.public_fab);

        fab_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NotificationActivity.this, "public", Toast.LENGTH_SHORT).show();
            }
        });
        fab_teachers_only.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NotificationActivity.this, "Teachers", Toast.LENGTH_SHORT).show();

            }
        });
        fab_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NotificationActivity.this, "local", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
