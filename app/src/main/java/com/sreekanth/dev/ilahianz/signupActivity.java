package com.sreekanth.dev.ilahianz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import static com.sreekanth.dev.ilahianz.model.Literals.SP_ABOUT;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_ABOUT_PRIVACY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTHDAY_PRIVACY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_DAY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_MONTH;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_YEAR;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_CATEGORY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_CLASS_NAME;
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

public class signupActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private DatabaseReference reference;
    private Dialog Progressbar;
    private FirebaseAuth auth;
    private start start = new start();
    private EditText username, password, email, passwordConfirm;
    private RadioGroup gender, category, classname, division;
    private Dialog formPopupStud, formPopupStaff;
    private ImageView character;
    private Button btn_finish;
    private EditText nickname;
    private String txt_nickname;
    private int divisionID, classID;
    private String txt_password;
    private String txt_Username;
    private String txt_email;
    private RadioButton btn_gender, btn_category;
    private int birthDay;
    private int birthYear;
    private int birthMonth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        formPopupStud = new Dialog(signupActivity.this);
        formPopupStud.setContentView(R.layout.activity_form_);
        formPopupStaff = new Dialog(signupActivity.this);
        formPopupStaff.setContentView(R.layout.staff_form);
        Button signUp_btn = findViewById(R.id.signup_btn);
        auth = FirebaseAuth.getInstance();
        character = findViewById(R.id.character);
        final Button birthday = findViewById(R.id.birthday);
        password = findViewById(R.id.signup_passwordField);
        username = findViewById(R.id.signup_username_field);
        email = findViewById(R.id.signup_EmailField);
        division = formPopupStud.findViewById(R.id.division_rg);
        classname = formPopupStud.findViewById(R.id.class_rg);
        btn_finish = formPopupStud.findViewById(R.id.finish_btn);
        nickname = formPopupStud.findViewById(R.id.nickname);
        passwordConfirm = findViewById(R.id.signup_passwordConfirm);
        gender = findViewById(R.id.gender_rg);
        category = findViewById(R.id.category_rg);
        Progressbar = new Dialog(signupActivity.this);
        auth = FirebaseAuth.getInstance();
        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.sreekanth.dev.ilahianz.start.newUser = true;
                txt_password = password.getText().toString();
                txt_Username = username.getText().toString();
                txt_email = email.getText().toString();
                int genderId = gender.getCheckedRadioButtonId();
                int catId = category.getCheckedRadioButtonId();
                btn_gender = findViewById(genderId);
                btn_category = findViewById(catId);
                String txt_passsConfirm = passwordConfirm.getText().toString();
                if (start.Connected(signupActivity.this)) {
                    start.popupNoInternet(signupActivity.this);
                } else if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) ||
                        TextUtils.isEmpty(txt_passsConfirm) || TextUtils.isEmpty(txt_Username)) {
                    Toast.makeText(signupActivity.this, "All Fields are Required !", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 8) {
                    Toast.makeText(signupActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                } else if (!txt_passsConfirm.equals(txt_password)) {
                    Toast.makeText(signupActivity.this, "Password isn't match", Toast.LENGTH_SHORT).show();
                } else if (btn_gender == null) {
                    Toast.makeText(signupActivity.this, "Specify your gender", Toast.LENGTH_SHORT).show();
                } else if (btn_category == null) {
                    Toast.makeText(signupActivity.this, "Specify your Category", Toast.LENGTH_SHORT).show();
                } else if ((birthDay + birthMonth + birthYear) == 0) {
                    Toast.makeText(signupActivity.this, "Specify your Birthday", Toast.LENGTH_SHORT).show();
                } else {
                    if (btn_category.getText().equals("Student")) {
                        studentForm();
                    } else {
                        staff_Form();
                    }
                }
            }
        });
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDay = 0;
                birthYear = 0;
                birthMonth = 0;
                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog Dialog = new DatePickerDialog(
                        signupActivity.this, android.R.style.Theme_Material_Light_Dialog,
                        dateSetListener, 2000, month, day);
                Dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthDay = dayOfMonth;
                birthMonth = month;
                birthYear = year;
            }
        };
    }


    private void registerStudents(final String username, final String email, String password,
                                  final String gender, final String classname,
                                  final String nickname, final String phone) { //Firebase Registry
        Progressbar.setContentView(R.layout.progressbar);
        Objects.requireNonNull(Progressbar.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Progressbar.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("search", username.toLowerCase());
                            hashMap.put("imageURL", "default");
                            hashMap.put("LastSeenPrivacy", "Everyone");
                            hashMap.put("ProfilePrivacy", "Everyone");
                            hashMap.put("AboutPrivacy", "Everyone");
                            hashMap.put("LocationPrivacy", "Everyone");
                            hashMap.put("EmailPrivacy", "Everyone");
                            hashMap.put("PhonePrivacy", "Everyone");
                            hashMap.put("BirthdayPrivacy", "Everyone");
                            hashMap.put("PhoneNumber", phone);
                            hashMap.put("gender", gender);
                            hashMap.put("className", classname);
                            hashMap.put("Nickname", nickname);
                            hashMap.put("Category", "Student");
                            hashMap.put("Latitude", "null");
                            hashMap.put("Longitude", "null");
                            hashMap.put("thumbnailURL", "default");
                            hashMap.put("email", email);
                            hashMap.put("Description", "Hey Ilahianz");
                            hashMap.put("Birthday", String.valueOf(birthDay));
                            hashMap.put("BirthYear", String.valueOf(birthYear));
                            hashMap.put("BirthMonth", String.valueOf(birthMonth));
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    setUserInfo(SP_USERNAME, username);
                                    setUserInfo(SP_PH_NUMBER, phone);
                                    setUserInfo(SP_GENDER, gender);
                                    setUserInfo(SP_CLASS_NAME, classname);
                                    setUserInfo(SP_EMAIL, email);
                                    setUserInfo(SP_BIRTH_DAY, String.valueOf(birthDay));
                                    setUserInfo(SP_BIRTH_YEAR, String.valueOf(birthYear));
                                    setUserInfo(SP_BIRTH_MONTH, String.valueOf(birthMonth));
                                    setUserInfo(SP_NICKNAME, nickname);
                                    setUserInfo(SP_CATEGORY, "Student");
                                    setUserInfo(SP_ABOUT, "Hey Ilahianz");
                                    setUserInfo(SP_LAST_SEEN, "Everyone");
                                    setUserInfo(SP_PROFILE_PRIVACY, "Everyone");
                                    setUserInfo(SP_ABOUT_PRIVACY, "Everyone");
                                    setUserInfo(SP_LOCATION_PRIVACY, "Everyone");
                                    setUserInfo(SP_EMAIL_PRIVACY, "Everyone");
                                    setUserInfo(SP_PHONE_PRIVACY, "Everyone");
                                    setUserInfo(SP_BIRTHDAY_PRIVACY, "Everyone");
                                    formPopupStud.cancel();
                                    Progressbar.cancel();
                                    Intent intent = new Intent(signupActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        } else {
                            Toast.makeText(signupActivity.this, "Authentication Failed !", Toast.LENGTH_SHORT).show();
                            Progressbar.cancel();
                            formPopupStud.cancel();
                        }
                    }
                });

    }

    public void gender_Male(View view) {
        int catId = category.getCheckedRadioButtonId();
        btn_category = findViewById(catId);
        if (btn_category != null) {
            switch (btn_category.getText().toString()) {
                case "Student":
                    character.setImageResource(R.mipmap.men1);
                    Toast.makeText(this, "Hai " + username.getText() + " Dude", Toast.LENGTH_SHORT).show();
                    break;
                case "Teacher":
                    character.setImageResource(R.mipmap.sir);
                    Toast.makeText(this, "Hai " + username.getText() + " Sir", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        } else {
            character.setImageResource(R.mipmap.men1);
        }
    }

    public void gender_Female(View view) {
        int catId = category.getCheckedRadioButtonId();
        btn_category = findViewById(catId);
        if (btn_category != null) {
            switch (btn_category.getText().toString()) {
                case "Student":
                    character.setImageResource(R.mipmap.wumen4);
                    Toast.makeText(this, "Hai " + username.getText(), Toast.LENGTH_SHORT).show();
                    break;
                case "Teacher":
                    character.setImageResource(R.mipmap.wumen2);
                    Toast.makeText(this, "Hai " + username.getText() + " Teacher", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        } else {
            character.setImageResource(R.mipmap.wumen4);
        }
    }

    public void gender_bysex(View view) {
        character.setImageResource(R.mipmap.bysex);
    }

    public void cat_teacherClick(View view) {
        int genderId = gender.getCheckedRadioButtonId();
        btn_gender = findViewById(genderId);
        character.setImageResource(R.mipmap.wumen2);
        if (btn_gender == null) {
            Toast.makeText(this, "Specify your Gender", Toast.LENGTH_SHORT).show();
        } else {
            switch (btn_gender.getText().toString()) {
                case "Male":
                    character.setImageResource(R.mipmap.sir);
                    break;
                case "Female":
                    character.setImageResource(R.mipmap.wumen2);
                    break;
                default:
                    character.setImageResource(R.mipmap.bysex);
                    break;
            }
            Toast.makeText(this, "Hai " + username.getText() + " Teacher", Toast.LENGTH_SHORT).show();
        }

    }

    public void cat_studentClick(View view) {
        int genderId = gender.getCheckedRadioButtonId();
        btn_gender = findViewById(genderId);
        if (btn_gender == null) {
            Toast.makeText(this, "Specify your Gender", Toast.LENGTH_SHORT).show();
        } else {
            switch (btn_gender.getText().toString()) {
                case "Male":
                    character.setImageResource(R.mipmap.men1);
                    break;
                case "Female":
                    character.setImageResource(R.mipmap.wumen4);
                    break;
                default:
                    character.setImageResource(R.mipmap.bysex);
                    break;
            }
            Toast.makeText(this, "Hai " + username.getText(), Toast.LENGTH_SHORT).show();
        }

    }

    ////////////////////////////////////
    public void studentForm() {
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton txt_class, txt_division;
                divisionID = division.getCheckedRadioButtonId();
                classID = classname.getCheckedRadioButtonId();
                txt_class = formPopupStud.findViewById(classID);
                txt_division = formPopupStud.findViewById(divisionID);
                txt_nickname = nickname.getText().toString();
                final EditText phoneNo = formPopupStud.findViewById(R.id.phone_number);
                final String PhoneNo = TextUtils.isEmpty(phoneNo.getText()) ? "Not Provided" : phoneNo.getText().toString();
                String full_className;
                if (txt_class == null) {
                    Toast.makeText(signupActivity.this, "Specify your class", Toast.LENGTH_SHORT).show();
                } else if (txt_division == null) {
                    Toast.makeText(signupActivity.this, "Specify your Division", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(nickname.getText().toString())) {
                        txt_nickname = "default";
                    }
                    full_className = txt_class.getText().toString();
                    full_className += (txt_division.getText().toString()).equals("non") ? "" : txt_division.getText().toString();
                    Toast.makeText(signupActivity.this, full_className, Toast.LENGTH_SHORT).show();
                    registerStudents(txt_Username, txt_email, txt_password,
                            btn_gender.getText().toString(),
                            full_className, txt_nickname, PhoneNo);
                }
            }
        });
        Objects.requireNonNull(formPopupStud.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        formPopupStud.show();
    }

    public void registerTeachers(final String username, final String email,
                                 String password, final String gender,
                                 final String position, final String phone) {
        Progressbar.setContentView(R.layout.progressbar);
        Objects.requireNonNull(Progressbar.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Progressbar.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("LastSeenPrivacy", "Everyone");
                            hashMap.put("ProfilePrivacy", "Everyone");
                            hashMap.put("AboutPrivacy", "Everyone");
                            hashMap.put("LocationPrivacy", "Everyone");
                            hashMap.put("EmailPrivacy", "Everyone");
                            hashMap.put("PhonePrivacy", "Everyone");
                            hashMap.put("BirthdayPrivacy", "Everyone");
                            hashMap.put("PhoneNumber", phone);
                            hashMap.put("imageURL", "default");
                            hashMap.put("gender", gender);
                            hashMap.put("Category", "Teacher");
                            hashMap.put("search", username.toLowerCase());
                            hashMap.put("className", position);
                            hashMap.put("Nickname", "default");
                            hashMap.put("Latitude", "null");
                            hashMap.put("Longitude", "null");
                            hashMap.put("thumbnailURL", "default");
                            hashMap.put("email", email);
                            hashMap.put("Description", "Hey Ilahianz");
                            hashMap.put("Birthday", String.valueOf(birthDay));
                            hashMap.put("BirthYear", String.valueOf(birthYear));
                            hashMap.put("BirthMonth", String.valueOf(birthMonth));
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()) {
                                        setUserInfo(SP_USERNAME, username);
                                        setUserInfo(SP_PH_NUMBER, phone);
                                        setUserInfo(SP_GENDER, gender);
                                        setUserInfo(SP_CLASS_NAME, position);
                                        setUserInfo(SP_EMAIL, email);
                                        setUserInfo(SP_BIRTH_DAY, String.valueOf(birthDay));
                                        setUserInfo(SP_BIRTH_YEAR, String.valueOf(birthYear));
                                        setUserInfo(SP_BIRTH_MONTH, String.valueOf(birthMonth));
                                        setUserInfo(SP_NICKNAME, "default");
                                        setUserInfo(SP_CATEGORY, "Teacher");
                                        setUserInfo(SP_ABOUT, "Hey Ilahianz");
                                        setUserInfo(SP_LAST_SEEN, "Everyone");
                                        setUserInfo(SP_PROFILE_PRIVACY, "Everyone");
                                        setUserInfo(SP_ABOUT_PRIVACY, "Everyone");
                                        setUserInfo(SP_LOCATION_PRIVACY, "Everyone");
                                        setUserInfo(SP_EMAIL_PRIVACY, "Everyone");
                                        setUserInfo(SP_PHONE_PRIVACY, "Everyone");
                                        setUserInfo(SP_BIRTHDAY_PRIVACY, "Everyone");
                                        formPopupStaff.cancel();
                                        Progressbar.cancel();
                                        Intent intent = new Intent(signupActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(signupActivity.this, "Authentication Failed !", Toast.LENGTH_SHORT).show();
                            Progressbar.cancel();
                            formPopupStaff.cancel();
                        }
                    }
                });
    }

    private void setUserInfo(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    /////////////////////////////////////////////////
    private void staff_Form() {
        Objects.requireNonNull(formPopupStaff.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button final_btn = formPopupStaff.findViewById(R.id.finish_btn_staff);
        final RadioGroup position = formPopupStaff.findViewById(R.id.position_rg);
        final EditText phoneNo = formPopupStaff.findViewById(R.id.phone_number);
        final String PhoneNo = TextUtils.isEmpty(phoneNo.getText()) ? "Not Provided" : phoneNo.getText().toString();
        final_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posId = position.getCheckedRadioButtonId();
                final RadioButton btn_position = formPopupStaff.findViewById(posId);
                if (btn_position != null) {
                    registerTeachers(txt_Username, txt_email, txt_password,
                            btn_gender.getText().toString(),
                            btn_position.getText().toString(), PhoneNo);
                } else {
                    Toast.makeText(signupActivity.this, "Specify your position", Toast.LENGTH_SHORT).show();
                }
            }
        });
        formPopupStaff.show();
    }

}
