/*
 * Copyright (c)
 * This Code
 * Author Sreekanth K R
 * Name Ilahianz
 * Github  https://github.com/sreekanthblackdevil/Ilahianz
 */

package com.sreekanth.dev.ilahianz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;
import com.sreekanth.dev.ilahianz.Supports.Supports;
import com.sreekanth.dev.ilahianz.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static com.sreekanth.dev.ilahianz.model.Literals.SP_ABOUT;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_DAY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_MONTH;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_YEAR;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_PH_NUMBER;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_USERNAME;

public class EditProfileActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener dateSetListener;
    EditText username,
            nickname, class_name, birthday,
            about, phone;
    private FirebaseUser fuser;
    private boolean fetched = false;
    int birth_year, birth_month, birth_day;
    DatabaseReference reference;
    User myInfo = new User();
    Dialog dialog;
    ImageButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dialog = new Dialog(this);
        username = findViewById(R.id.username);
        nickname = findViewById(R.id.nickname);
        class_name = findViewById(R.id.class_name);
        save = findViewById(R.id.ok);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        init();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetched = true;
                myInfo = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Supports.Connected(EditProfileActivity.this)) {
                    if (fetched) {

                        if (!TextUtils.isEmpty(phone.getText().toString())) {
                            if (TextUtils.getTrimmedLength(phone.getText().toString()) >= 10) {
                                updateInfo("PhoneNumber", SP_PH_NUMBER, phone.getText().toString());
                                dialog.dismiss();
                            } else {
                                phone.setError("Must be 10 Digits");
                            }
                        }

                        if (!TextUtils.isEmpty(about.getText().toString())) {
                            if (TextUtils.getTrimmedLength(about.getText().toString()) >= 35) {
                                updateInfo("Description", SP_ABOUT, about.getText().toString());
                            } else {
                                about.setError("Too much characters");
                            }
                        } else about.setError("field is empty");


                    } else {
                        Toast.makeText(EditProfileActivity.this, "Loading information...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(v, "No Internet", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        });

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!Supports.Connected(EditProfileActivity.this)) {
                    if (fetched) {
                        DatePickerDialog Dialog = new DatePickerDialog(
                                EditProfileActivity.this, android.R.style.Theme_Material_Light_Dialog,
                                dateSetListener, birth_year, birth_month, birth_day);
                        Dialog.show();
                    } else
                        Toast.makeText(EditProfileActivity.this, "Loading information...", Toast.LENGTH_SHORT).show();
                } else
                    Snackbar.make(v, "No Internet !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, int year, int month, int dayOfMonth) {
                reference = FirebaseDatabase.getInstance()
                        .getReference("Users").child(fuser.getUid());
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("Birthday", String.valueOf(dayOfMonth));
                hashMap.put("BirthMonth", String.valueOf(month));
                hashMap.put("BirthYear", String.valueOf(year));
                birth_day = dayOfMonth;
                birth_month = month;
                birth_year = year;
                reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            setUserInfo("Birthday", String.valueOf(birth_day));
                            setUserInfo("BirthMonth", String.valueOf(birth_month));
                            setUserInfo("BirthYear", String.valueOf(birth_year));
                            Toast.makeText(EditProfileActivity.this, "Date of birth changed", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(EditProfileActivity.this, "Failed to apply changes", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };


        class_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Supports.Connected(EditProfileActivity.this)) {
                    if (fetched) {
                        dialog.setContentView(R.layout.edit_class);
                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ImageView cancel = dialog.findViewById(R.id.cancel_btn);
                        ImageView ok = dialog.findViewById(R.id.ok_btn);

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                int divisionID;
                                RadioButton txt_class, txt_division;
                                RadioGroup division = dialog.findViewById(R.id.division_rg);
                                RadioGroup classname = dialog.findViewById(R.id.class_rg);
                                divisionID = division.getCheckedRadioButtonId();
                                int classID = classname.getCheckedRadioButtonId();
                                txt_class = dialog.findViewById(classID);
                                txt_division = dialog.findViewById(divisionID);

                                if (txt_class == null) {
                                    Toast.makeText(EditProfileActivity.this,
                                            "Specify your class", Toast.LENGTH_SHORT).show();
                                } else if (txt_division == null) {
                                    Toast.makeText(EditProfileActivity.this,
                                            "Specify your Division", Toast.LENGTH_SHORT).show();
                                } else {
                                    String className = txt_class.getText().toString();
                                    className += txt_division.getText().toString();
                                    class_name.setText(className);
                                    dialog.dismiss();
                                }
                            }
                        });

                        dialog.show();

                    } else {
                        Toast.makeText(EditProfileActivity.this, "Loading Information...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(v, "No Internet !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private void init() {
        username.setText(getUserInfo(SP_USERNAME));
        about.setText(getUserInfo(SP_ABOUT));
        int birth_day = Integer.parseInt(getUserInfo(SP_BIRTH_DAY));
        int birth_month = Integer.parseInt(getUserInfo(SP_BIRTH_MONTH));
        int birth_year = Integer.parseInt(getUserInfo(SP_BIRTH_YEAR));
        phone.setText(getUserInfo(SP_PH_NUMBER));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, birth_day);
        calendar.set(Calendar.MONTH, birth_month);
        calendar.set(Calendar.YEAR, birth_year);
        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.simple_date_formate), Locale.US);
        String birthDay = format.format(calendar.getTime());
        birthday.setText(birthDay);
    }

    private void updateInfo(String key1, final String key2, final String value) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(key1, value);
        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    setUserInfo(key2, value);
                    Toast.makeText(EditProfileActivity.this, "Changes applied", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to apply changes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getUserInfo(String key) {
        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

    private void setUserInfo(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }
}
