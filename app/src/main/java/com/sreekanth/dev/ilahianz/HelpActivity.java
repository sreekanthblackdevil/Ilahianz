package com.sreekanth.dev.ilahianz;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class HelpActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 11;
    private final String DEVELOPER_PH = "+918156906701";
    private final String POLICE_PH = "04852832304";
    private final String WOMEN_HELPLINE = "1091";
    private final String WOMEN_HELPLINE_IDUKKI = "04862236600";
    private final String WOMEN_HELPLINE_KOCHI = "04842356044";
    private final String WOMEN_HELPLINE_WEBSITE = "http://keralawomen.gov.in/index.php/helpline";
    private final LatLng MVP_POLICE_STATION_LOCATION = new LatLng(9.980718, 76.5818285);
    LinearLayout policeHelp, teachersHelp, libraryHelp, ladiesHelp, developerHelp;
    Button close_dev;
    Dialog dev_contact_popup, police_contact_popup, women_help;
    String PhoneNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        dev_contact_popup = new Dialog(this);
        police_contact_popup = new Dialog(this);
        women_help = new Dialog(this);
        women_help.setContentView(R.layout.ladies_helpline);
        police_contact_popup.setContentView(R.layout.police_help);
        dev_contact_popup.setContentView(R.layout.help_dev);
        policeHelp = findViewById(R.id.police_help_btn);
        teachersHelp = findViewById(R.id.teachers_help_btn);
        libraryHelp = findViewById(R.id.library_help_btn);
        ladiesHelp = findViewById(R.id.ladies_help_btn);
        developerHelp = findViewById(R.id.help_developer_btn);

        policeHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView police_call = police_contact_popup.findViewById(R.id.call_btn_police_help);
                ImageView police_locate = police_contact_popup.findViewById(R.id.locate_btn_police);
                Button close = police_contact_popup.findViewById(R.id.police_help_close_btn);
                Objects.requireNonNull(police_contact_popup.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                police_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhoneNumber = POLICE_PH;
                        makePhoneCall();
                    }
                });
                police_locate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MapsActivity.setLOCATION(MVP_POLICE_STATION_LOCATION, "Muvattupuzha Police Station");
                        startActivity(new Intent(HelpActivity.this, MapsActivity.class));
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        police_contact_popup.cancel();
                    }
                });
                police_contact_popup.show();

            }
        });
        teachersHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        libraryHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        ladiesHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(women_help.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageView call = women_help.findViewById(R.id.call_btn_helpline);
                ImageView web_visit = women_help.findViewById(R.id.visit_web_helpline);
                ImageView call_idukki = women_help.findViewById(R.id.idukki_call);
                ImageView call_kochi = women_help.findViewById(R.id.kochi_call);
                Button close = women_help.findViewById(R.id.ladies_help_close_btn);

                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhoneNumber = WOMEN_HELPLINE;
                        makePhoneCall();
                    }
                });
                web_visit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebViewActivity.setURL(WOMEN_HELPLINE_WEBSITE);
                        startActivity(new Intent(HelpActivity.this, WebViewActivity.class));

                    }
                });
                call_idukki.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhoneNumber = WOMEN_HELPLINE_IDUKKI;
                        makePhoneCall();
                    }
                });
                call_kochi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhoneNumber = WOMEN_HELPLINE_KOCHI;
                        makePhoneCall();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        women_help.cancel();
                    }
                });
                women_help.show();
            }
        });
        developerHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(dev_contact_popup.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageView contact_dev = dev_contact_popup.findViewById(R.id.contact_developer_btn);
                close_dev = dev_contact_popup.findViewById(R.id.close_dev_help);
                contact_dev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhoneNumber = DEVELOPER_PH;
                        makePhoneCall();
                    }
                });
                dev_contact_popup.show();
                close_dev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dev_contact_popup.cancel();
                    }
                });
            }
        });
    }

    public void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + PhoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            }
        }
    }
}
