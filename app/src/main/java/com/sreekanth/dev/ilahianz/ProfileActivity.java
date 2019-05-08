package com.sreekanth.dev.ilahianz;
/**
 * This code {ProfileActivity}
 * Created on May-01-2019
 * Author Sreekanth K R
 * name Ilahianz
 * Github https://github.com/sreekanthblackdevil/Ilahianz
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.sreekanth.dev.ilahianz.Supports.Supports;
import com.sreekanth.dev.ilahianz.Supports.ViewSupport;
import com.sreekanth.dev.ilahianz.model.User;
import com.sreekanth.dev.ilahianz.utils.FileUtil;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.sreekanth.dev.ilahianz.Supports.FileSupports.saveImage;
import static com.sreekanth.dev.ilahianz.model.Literals.CAMERA_REQUEST;
import static com.sreekanth.dev.ilahianz.model.Literals.DEFAULT;
import static com.sreekanth.dev.ilahianz.model.Literals.IMAGE_REQUEST;
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
import static com.sreekanth.dev.ilahianz.model.Literals.SP_ID;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_LAST_SEEN;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_LOCATION_PRIVACY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_NICKNAME;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_PHONE_PRIVACY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_PH_NUMBER;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_PROFILE_PRIVACY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_USERNAME;


public class ProfileActivity extends AppCompatActivity {

    private CircleImageView pro_image;
    private TextView username, about, birthday;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Dialog change_username,
            change_description,
            changeNickname,
            edit_class;
    private DatabaseReference reference;
    private FirebaseUser fuser;
    private ImageView change_profile;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private int birth_day, birth_month, birth_year;
    private Dialog profile_view;
    private ImageView changeName, editNickname;
    private LinearLayout chaneDescription, ChangeBirthday;
    private String username_txt;
    private String imageURL;
    private String thumbURL;
    private User user1 = new User();
    private BottomSheetDialog dialog;
    private FirebaseStorage mStorage;
    private File croppedImage;
    private File compressedImage;
    private File thumbnail;
    private File actualImage;
    private ProgressBar progress;
    private TextView phone;
    private TextView email;
    private TextView class_name;
    private TextView nickname;
    private CardView nickname_carry;
    boolean fetched;
    private LinearLayout edit_class_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_view = new Dialog(this);
        change_description = new Dialog(this);
        change_username = new Dialog(this);
        changeNickname = new Dialog(this);
        edit_class = new Dialog(this);

        pro_image = findViewById(R.id.profile_Image);
        birthday = findViewById(R.id.birthday);
        changeName = findViewById(R.id.edit_username);
        ChangeBirthday = findViewById(R.id.editBirthday);
        chaneDescription = findViewById(R.id.changeDescription);
        about = findViewById(R.id.description);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone_number);
        class_name = findViewById(R.id.class_name);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        change_profile = findViewById(R.id.btn_profile_change);
        mStorage = FirebaseStorage.getInstance();
        progress = findViewById(R.id.progressBar);
        nickname = findViewById(R.id.nickname);
        edit_class_str = findViewById(R.id.edit_class);
        editNickname = findViewById(R.id.edit_nickname);
        nickname_carry = findViewById(R.id.card6);
        init();
        fetched = false;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                fetched = true;
                imageURL = user.getImageURL();
                thumbURL = user.getThumbnailURL();
                user1 = user;
                setUserInfo(user);
                progress.setVisibility(View.GONE);
                ViewSupport.setProfileImage(user, pro_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pro_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_view.setContentView(R.layout.profile_view);
                Objects.requireNonNull(profile_view.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        profile_view.setContentView(R.layout.profile_view);
                        final User user = dataSnapshot.getValue(User.class);
                        final ImageView imageView = profile_view.findViewById(R.id.profile_Image);
                        final TextView textView = profile_view.findViewById(R.id.username_profile);
                        Button proChange = profile_view.findViewById(R.id.pro_change);
                        Button save = profile_view.findViewById(R.id.save_pic);
                        assert user != null;
                        textView.setText(user.getUsername());
                        ViewSupport.setProfileImage(user, imageView);
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(ProfileActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                saveImage(ProfileActivity.this, imageView, user);

                            }
                        });
                        proChange.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openSheet();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ProfileActivity.this, "Failed to load Image.", Toast.LENGTH_SHORT).show();
                    }
                });
                profile_view.show();
            }
        });

        changes();
    }

    private void init() {
        username.setText(getUserInfo(SP_USERNAME));
        username_txt = getUserInfo(SP_USERNAME);
        about.setText(getUserInfo(SP_ABOUT));
        birth_day = Integer.parseInt(getUserInfo(SP_BIRTH_DAY));
        birth_month = Integer.parseInt(getUserInfo(SP_BIRTH_MONTH));
        birth_year = Integer.parseInt(getUserInfo(SP_BIRTH_YEAR));
        email.setText(getUserInfo(SP_EMAIL));
        phone.setText(getUserInfo(SP_PH_NUMBER));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, birth_day);
        calendar.set(Calendar.MONTH, birth_month);
        calendar.set(Calendar.YEAR, birth_year);
        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.simple_date_formate), Locale.US);
        String birthDay = format.format(calendar.getTime());
        birthday.setText(birthDay);

        if (!TextUtils.equals(getUserInfo(SP_CLASS_NAME), "Other"))
            class_name.setText(getUserInfo(SP_CLASS_NAME));
        else class_name.setVisibility(View.GONE);

        if (!TextUtils.equals(getUserInfo(SP_NICKNAME), DEFAULT)) {
            nickname.setText(getUserInfo(SP_NICKNAME));
            nickname_carry.setVisibility(View.VISIBLE);
        } else if (TextUtils.equals(getUserInfo(SP_CATEGORY), "Student")) {
            nickname.setText(getString(R.string.nick_name_not_provided));
            nickname_carry.setVisibility(View.VISIBLE);
        } else if (TextUtils.equals(getUserInfo(SP_CATEGORY), "Teacher"))
            nickname_carry.setVisibility(View.GONE);
    }

    private void changes() {

        edit_class_str.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Supports.Connected(ProfileActivity.this)) {
                    if (fetched) {

                        edit_class.setContentView(R.layout.edit_class);
                        Objects.requireNonNull(edit_class.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ImageView cancel = edit_class.findViewById(R.id.cancel_btn);
                        ImageView ok = edit_class.findViewById(R.id.ok_btn);

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                edit_class.dismiss();
                            }
                        });
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                int divisionID;
                                RadioButton txt_class, txt_division;
                                RadioGroup division = edit_class.findViewById(R.id.division_rg);
                                RadioGroup classname = edit_class.findViewById(R.id.class_rg);
                                divisionID = division.getCheckedRadioButtonId();
                                int classID = classname.getCheckedRadioButtonId();
                                txt_class = edit_class.findViewById(classID);
                                txt_division = edit_class.findViewById(divisionID);

                                if (txt_class == null) {
                                    Toast.makeText(ProfileActivity.this,
                                            "Specify your class", Toast.LENGTH_SHORT).show();
                                } else if (txt_division == null) {
                                    Toast.makeText(ProfileActivity.this,
                                            "Specify your Division", Toast.LENGTH_SHORT).show();
                                } else {
                                    String className = txt_class.getText().toString();
                                    className += txt_division.getText().toString();
                                    updateInfo("className", SP_CLASS_NAME, className);
                                    edit_class.dismiss();
                                }
                            }
                        });

                        edit_class.show();

                    } else {
                        Toast.makeText(ProfileActivity.this, "Loading Information...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(v, "No Internet !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Supports.Connected(ProfileActivity.this)) {
                    if (fetched) {
                        final Dialog dialog = new Dialog(ProfileActivity.this);
                        dialog.setContentView(R.layout.edit_phone_number);
                        ImageView ok = dialog.findViewById(R.id.ok);
                        ImageView cancel = dialog.findViewById(R.id.cancel);
                        final EditText number = dialog.findViewById(R.id.phone_number);
                        number.setText(getUserInfo(SP_PH_NUMBER));
                        number.setSelection(0, getUserInfo(SP_PH_NUMBER).length());
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                if (!TextUtils.isEmpty(number.getText().toString())) {
                                    if (TextUtils.getTrimmedLength(number.getText().toString()) >= 10) {
                                        updateInfo("PhoneNumber", SP_PH_NUMBER, number.getText().toString());
                                        dialog.dismiss();
                                    } else {
                                        number.setError("Must be 10 Digits");
                                    }
                                }
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Loading information...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(v, "No Internet", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        });
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Supports.Connected(ProfileActivity.this)) {
                    if (fetched) {
                        change_username.setContentView(R.layout.username_cahnge);
                        Objects.requireNonNull(change_username.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        CircleImageView proImage = change_username.findViewById(R.id.profile_Image);
                        ViewSupport.setThumbProfileImage(user1, proImage);

                        TextView username_txt = change_username.findViewById(R.id.username);
                        final EditText username = change_username.findViewById(R.id.edit_username_txt);

                        username_txt.setText(getUserInfo(SP_USERNAME));
                        username.setText(getUserInfo(SP_USERNAME));
                        username.setSelection(0, getUserInfo(SP_USERNAME).length());
                        username.setSelection(getUserInfo(SP_USERNAME).length());

                        ImageView ok = change_username.findViewById(R.id.ok_btn);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                if (!TextUtils.isEmpty(username.getText().toString())) {
                                    updateInfo("username", SP_USERNAME, username.getText().toString());
                                    updateInfo("search", SP_USERNAME, username.getText().toString());
                                } else {
                                    username.setError("Required");
                                }
                                change_username.dismiss();
                                init();
                            }
                        });

                        ImageView cancel = change_username.findViewById(R.id.cancel_btn);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                change_username.dismiss();
                            }
                        });

                        change_username.show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Loading information...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(v, "No Internet", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        });

        chaneDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Supports.Connected(ProfileActivity.this)) {
                    if (fetched) {
                        change_description.setContentView(R.layout.discription_change);
                        Objects.requireNonNull(change_description.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        final EditText description = change_description.findViewById(R.id.description_txt_edit);
                        ImageView cancel = change_description.findViewById(R.id.cancel_btn);
                        ImageView ok = change_description.findViewById(R.id.ok_btn);

                        description.setText(getUserInfo("description"));
                        description.setSelection(getUserInfo("description").length());
                        description.setSelection(0, getUserInfo("description").length());
                        description.setCursorVisible(true);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                change_description.dismiss();
                            }
                        });
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                if (!TextUtils.isEmpty(description.getText())) {
                                    updateInfo("Description", SP_ABOUT, description.getText().toString());
                                } else description.setError("Field is Empty !");

                                change_description.dismiss();
                            }
                        });

                        change_description.show();
                    } else
                        Toast.makeText(ProfileActivity.this, "Loading Information", Toast.LENGTH_SHORT).show();
                } else
                    Snackbar.make(v, "No Internet !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
            }
        });

        editNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Supports.Connected(ProfileActivity.this)) {
                    if (fetched) {
                        changeNickname.setContentView(R.layout.edit_nickname);
                        Objects.requireNonNull(changeNickname.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        final EditText nickname = changeNickname.findViewById(R.id.nickname);
                        ImageView cancel = changeNickname.findViewById(R.id.cancel);
                        ImageView ok = changeNickname.findViewById(R.id.ok);

                        nickname.setText(getUserInfo("nickname"));
                        nickname.setSelection(getUserInfo("nickname").length());
                        nickname.setSelection(0, getUserInfo("nickname").length());
                        nickname.setCursorVisible(true);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeNickname.dismiss();
                            }
                        });
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                if (!TextUtils.isEmpty(nickname.getText())) {
                                    updateInfo("Nickname", SP_NICKNAME, nickname.getText().toString());
                                }

                                changeNickname.dismiss();
                            }
                        });

                        changeNickname.show();
                    } else
                        Toast.makeText(ProfileActivity.this, "Loading Information", Toast.LENGTH_SHORT).show();
                } else
                    Snackbar.make(v, "No Internet !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
            }
        });

        change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSheet();
            }
        });

        ChangeBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!Supports.Connected(ProfileActivity.this)) {
                    if (fetched) {
                        DatePickerDialog Dialog = new DatePickerDialog(
                                ProfileActivity.this, android.R.style.Theme_Material_Light_Dialog,
                                dateSetListener, birth_year, birth_month, birth_day);
                        Dialog.show();
                    } else
                        Toast.makeText(ProfileActivity.this, "Loading information...", Toast.LENGTH_SHORT).show();
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
                /*setUserInfo("Birthday", String.valueOf(birth_day));
                setUserInfo("BirthMonth", String.valueOf(birth_month));
                setUserInfo("BirthYear", String.valueOf(birth_year));*/
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
                            Toast.makeText(ProfileActivity.this, "Changes applied", Toast.LENGTH_SHORT).show();
                            init();
                        } else
                            Toast.makeText(ProfileActivity.this, "Failed to apply changes", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };


    }

    private void openSheet() {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(this).inflate(R.layout.profile_bottom_sheet, null);
        if (!Supports.Connected(this)) {
            if (fetched) {
                dialog = new BottomSheetDialog(this);
                dialog.setContentView(view);
                {/////////////////////////////
                    LinearLayout camera, gallery, delete;
                    camera = view.findViewById(R.id.camera);
                    gallery = view.findViewById(R.id.gallery);
                    delete = view.findViewById(R.id.delete);
                    camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.equals(imageURL, "default"))
                                popupMSG();
                            else
                                openCamera();
                            dialog.dismiss();
                        }
                    });
                    gallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.equals(imageURL, "default"))
                                popupMSG();
                            else
                                openImage();
                            dialog.dismiss();
                        }
                    });
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteImage(imageURL, thumbURL);
                            dialog.dismiss();
                        }
                    });
                }//////////////////////////////////////////////////
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            } else Toast.makeText(this, "Loading Information...", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
    }

    private void openImage() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_REQUEST);
            } else ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_REQUEST);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, IMAGE_REQUEST);
        }
    }

    private void openCamera() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST);
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, CAMERA_REQUEST);
            } else
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        } else {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imageUri != null) {
                try {
                    if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
                        if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            actualImage = FileUtil.from(this, imageUri);
                        } else {
                            Toast.makeText(this, "Storage permission not granted", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        actualImage = FileUtil.from(this, imageUri);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                compressImage();
            }
        }
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imageUri != null) {
                try {
                    if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
                        if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            actualImage = FileUtil.from(this, imageUri);
                        }
                    } else {
                        actualImage = FileUtil.from(this, imageUri);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                compressImage();
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri uri = UCrop.getOutput(data);
            if (uri != null) {
                try {
                    croppedImage = FileUtil.from(this, uri);
                    thumbnailCompress();
                    if (uploadTask != null && uploadTask.isInProgress()) {
                        Toast.makeText(this, "Upload in Progress..", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadImage();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @SuppressLint("CheckResult")
    public void compressImage() {
        if (actualImage != null) {
            final ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
            pd.setMessage("Compressing...");
            pd.show();
            // Compress image in main thread
            //compressedImage = new Compressor(this).compressToFile(actualImage);
            //setCompressedImage();

            // Compress image to bitmap in main thread
            //compressedImageView.setImageBitmap(new Compressor(this).compressToBitmap(actualImage));

            // Compress image using RxJava in background thread
            /*new Compressor(this)
                    .compressToFileAsFlowable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            compressedImage = file;
                            startCrop(Uri.fromFile(compressedImage));
                            pd.dismiss();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            pd.dismiss();
                        }
                    });*/
            // Compress image using RxJava in background thread with custom Compressor
            //noinspection ResultOfMethodCallIgnored
            new Compressor(this)
                    .setQuality(50)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFileAsFlowable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            pd.dismiss();
                            compressedImage = file;
                            startCrop(Uri.fromFile(compressedImage));
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            pd.dismiss();
                        }
                    });

        }
    }

    private void startCrop(Uri uri) {
        String destination = username_txt;
        destination += ".jpeg";
        UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destination)))
                .withAspectRatio(1, 1)
                .withMaxResultSize(640, 640)
                .withOptions(getOption())
                .start(this);
    }

    private UCrop.Options getOption() {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarTitle("Profile Image");
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(50);
        options.setHideBottomControls(true);
        options.setActiveWidgetColor(Color.WHITE);
        options.setStatusBarColor(Color.WHITE);
        return options;
    }

    private void thumbnailCompress() {
        // if (croppedImage != null)
            /*// Compress image in main thread using custom Compressor
            try {
                thumbnail = new Compressor(this)
                        .setMaxWidth(140)
                        .setMaxHeight(140)
                        .setQuality(50)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(croppedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }*/
        // Compress image using RxJava in background thread with custom Compressor
        if (croppedImage != null) //noinspection ResultOfMethodCallIgnored
            new Compressor(this)
                    .setMaxWidth(140)
                    .setMaxHeight(140)
                    .setQuality(50)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFileAsFlowable(croppedImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            thumbnail = file;
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();

                        }
                    });
        else thumbnail = null;
    }

    private void deleteImage(String deleteURL, final String deleteURL2) {
        if (!Supports.Connected(this)) {
            if (fetched) {
                final ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
                pd.setMessage("Removing Photo...");
                pd.show();
                if (!TextUtils.equals(deleteURL, "default") && deleteURL != null) {
                    mStorage = FirebaseStorage.getInstance();
                    final StorageReference imageRef = mStorage.getReferenceFromUrl(deleteURL);
                    imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                UpdateInfo("imageURL", "default");
                                pd.dismiss();
                                Toast.makeText(ProfileActivity.this, "Image Delated", Toast.LENGTH_SHORT).show();
                                deleteThumbnail(deleteURL2);
                            } else {
                                Toast.makeText(ProfileActivity.this, "Could not delete image", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        }
                    });
                } else {
                    pd.dismiss();
                }
            } else {
                Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteThumbnail(String deleteURL) {
        if (!Supports.Connected(this)) {
            if (fetched) {
                final ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
                pd.setMessage("Removing Thumbnail...");
                pd.show();
                if (!TextUtils.equals(deleteURL, "default") && deleteURL != null) {
                    mStorage = FirebaseStorage.getInstance();
                    final StorageReference thumbnailRef = mStorage.getReferenceFromUrl(deleteURL);
                    thumbnailRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                UpdateInfo("thumbnailURL", "default");
                                pd.dismiss();
                                Toast.makeText(ProfileActivity.this, "Thumbnail deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Could not delete thumbnail", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        }
                    });
                } else {
                    pd.dismiss();
                }
            } else {
                Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void UpdateInfo(String key, String value) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(key, value);
        reference.updateChildren(hashMap);
    }

    private void uploadImage() {
        if (!Supports.Connected(this)) {
            final ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
            pd.setMessage("Uploading...");
            pd.show();
            if (croppedImage != null) {
                final StorageReference fileReference = storageReference.child("profile")
                        .child(fuser.getUid() + ".jpg");
                uploadTask = fileReference.putFile(Uri.fromFile(croppedImage));

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }
                        return fileReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = (Uri) task.getResult();
                            assert downloadUri != null;
                            String mUri = downloadUri.toString();
                            UpdateInfo("imageURL", mUri);
                            pd.dismiss();
                            uploadThumbnail();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to upload.!", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });
            } else {
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        } else {
            Toast.makeText(this, "Connect to the Internet !", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadThumbnail() {
        final ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
        pd.setMessage("Creating Thumbnail...");
        pd.show();
        if (thumbnail != null) {
            final StorageReference fileReference = storageReference.child("profile").child("thumbnails")
                    .child(fuser.getUid() + ".jpg");
            uploadTask = fileReference.putFile(Uri.fromFile(thumbnail));

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = (Uri) task.getResult();
                        assert downloadUri != null;
                        String mUri = downloadUri.toString();
                        UpdateInfo("thumbnailURL", mUri);
                        pd.dismiss();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to upload thumbnail.!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            pd.dismiss();
        }
    }

    private void popupMSG() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_dialog);
        ImageView ok = dialog.findViewById(R.id.ok);
        ImageView cancel = dialog.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteImage(imageURL, thumbURL);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void setUserInfo(User user) {
        setUserInfo(SP_ID, user.getId());
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

    private void updateInfo(String key1, final String key2, final String value) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(key1, value);
        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    setUserInfo(key2, value);
                    Toast.makeText(ProfileActivity.this, "Changes applied", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to apply changes", Toast.LENGTH_SHORT).show();
                }
                init();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}