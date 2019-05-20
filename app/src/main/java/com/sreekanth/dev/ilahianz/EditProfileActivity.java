/*
 * Copyright (c)
 * This Code
 * Author Sreekanth K R
 * Name Ilahianz
 * Github  https://github.com/sreekanthblackdevil/Ilahianz
 */

package com.sreekanth.dev.ilahianz;

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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import static com.sreekanth.dev.ilahianz.model.Literals.CAMERA_REQUEST;
import static com.sreekanth.dev.ilahianz.model.Literals.DEFAULT;
import static com.sreekanth.dev.ilahianz.model.Literals.IMAGE_REQUEST;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_ABOUT;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIO;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_DAY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_MONTH;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_YEAR;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_CATEGORY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_CITY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_CLASS_NAME;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_DISTRICT;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_EMAIL;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_NICKNAME;
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
    ImageButton close;
    CircleImageView profile_image;
    ProgressDialog progressDialog;
    String imageURL, thumbURL;
    private File croppedImage;
    private File compressedImage;
    private File thumbnail;
    private File actualImage;
    StorageTask uploadTask;
    FirebaseStorage mStorage;
    StorageReference storageReference;
    String username_txt;
    TextView change_Profile_image;
    EditText city;
    EditText bio;
    EditText district;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        dialog = new Dialog(this);
        imageURL = null;
        thumbURL = null;
        bio = findViewById(R.id.bio);
        progressDialog = new ProgressDialog(this);
        about = findViewById(R.id.description);
        username = findViewById(R.id.username);
        nickname = findViewById(R.id.nickname);
        class_name = findViewById(R.id.class_name);
        birthday = findViewById(R.id.birthday);
        city = findViewById(R.id.location);
        district = findViewById(R.id.district);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        mStorage = FirebaseStorage.getInstance();
        save = findViewById(R.id.ok);
        profile_image = findViewById(R.id.profile_Image);
        close = findViewById(R.id.close);
        phone = findViewById(R.id.phone);
        change_Profile_image = findViewById(R.id.change_image);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        init();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myInfo = dataSnapshot.getValue(User.class);
                assert myInfo != null;
                ViewSupport.setThumbProfileImage(myInfo, profile_image);
                imageURL = myInfo.getImageURL();
                thumbURL = myInfo.getThumbnailURL();
                fetched = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        change_Profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSheet();
            }
        });
        progressDialog.setMessage("Updating...");
        progressDialog.setCancelable(false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Supports.Connected(EditProfileActivity.this)) {
                    if (fetched) {
                        if (TextUtils.isEmpty(username.getText()))
                            username.setError("Empty");
                        else if (TextUtils.getTrimmedLength(username.getText()) > 20)
                            username.setError("Too much Characters");
                        else if (TextUtils.getTrimmedLength(phone.getText().toString()) < 10)
                            phone.setError("Must be 10 Digits");
                        else if (TextUtils.isEmpty(about.getText().toString()))
                            about.setError("is Empty");
                        else if (TextUtils.getTrimmedLength(about.getText().toString()) > 35)
                            about.setError("Too much characters");
                        else if (TextUtils.isEmpty(nickname.getText()))
                            nickname.setError("is Empty !");
                        else if (TextUtils.getTrimmedLength(nickname.getText()) >= 15)
                            nickname.setError("Too much Characters");
                        else {
                            progressDialog.show();
                            final String description;
                            if (TextUtils.equals(about.getText().toString(), "Not Provided") ||
                                    TextUtils.isEmpty(about.getText().toString()))
                                description = DEFAULT;
                            else description = about.getText().toString();
                            reference = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(fuser.getUid());
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("username", username.getText().toString());
                            hashMap.put("Description", description);
                            hashMap.put("PhoneNumber", phone.getText().toString());
                            hashMap.put("Nickname", nickname.getText().toString());
                            hashMap.put("className", class_name.getText().toString());
                            hashMap.put("city", city.getText().toString());
                            hashMap.put("bio", bio.getText().toString());
                            hashMap.put("district", district.getText().toString());
                            hashMap.put("search", username.getText().toString().toLowerCase());
                            reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()) {
                                        setUserInfo(SP_NICKNAME, nickname.getText().toString());
                                        setUserInfo(SP_ABOUT, description);
                                        setUserInfo(SP_USERNAME, username.getText().toString());
                                        setUserInfo(SP_PH_NUMBER, phone.getText().toString());
                                        setUserInfo(SP_CLASS_NAME, class_name.getText().toString());
                                        setUserInfo(SP_BIO, bio.getText().toString());
                                        setUserInfo(SP_DISTRICT, district.getText().toString());
                                        setUserInfo(SP_CITY, city.getText().toString());
                                        Toast.makeText(EditProfileActivity.this,
                                                "Changes applied", Toast.LENGTH_SHORT).show();
                                        progressDialog.cancel();
                                    } else {
                                        progressDialog.cancel();
                                        Toast.makeText(EditProfileActivity.this,
                                                "Failed to apply changes", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    } else
                        Toast.makeText(EditProfileActivity.this,
                                "Loading information...", Toast.LENGTH_SHORT).show();
                } else
                    Snackbar.make(v, "No Internet", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
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
                            Toast.makeText(EditProfileActivity.this,
                                    "Date of birth changed", Toast.LENGTH_SHORT).show();
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.DAY_OF_MONTH, birth_day);
                            calendar.set(Calendar.MONTH, birth_month);
                            calendar.set(Calendar.YEAR, birth_year);
                            SimpleDateFormat format = new SimpleDateFormat(getString(R.string.simple_date_formate), Locale.US);
                            String birthDay = format.format(calendar.getTime());
                            birthday.setText(birthDay);
                        } else
                            Toast.makeText(EditProfileActivity.this,
                                    "Failed to apply changes", Toast.LENGTH_SHORT).show();
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
                        Button ok = dialog.findViewById(R.id.ok_btn);

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
                        Toast.makeText(EditProfileActivity.this,
                                "Loading Information...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(v, "No Internet !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private void init() {
        district.setText(getUserInfo(SP_DISTRICT));
        city.setText(getUserInfo(SP_CITY));
        bio.setText(getUserInfo(SP_BIO));
        username.setText(getUserInfo(SP_USERNAME));
        about.setText(getUserInfo(SP_ABOUT));
        if (TextUtils.equals(getUserInfo(SP_CATEGORY), "Teacher"))
            class_name.setVisibility(View.GONE);
        else
            class_name.setText(getUserInfo(SP_CLASS_NAME));

        if (!TextUtils.equals(getUserInfo(SP_NICKNAME), DEFAULT))
            nickname.setText(getUserInfo(SP_NICKNAME));
        else nickname.setText(R.string.not_provided);
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

    public String getUserInfo(String key) {
        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

    private void setUserInfo(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    /// Image processing part

    private void openSheet() {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(this).inflate(R.layout.profile_bottom_sheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        if (!Supports.Connected(this)) {
            if (fetched) {
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
            final ProgressDialog pd = new ProgressDialog(EditProfileActivity.this);
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
                final ProgressDialog pd = new ProgressDialog(EditProfileActivity.this);
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
                                Toast.makeText(EditProfileActivity.this, "Image Deleted", Toast.LENGTH_SHORT).show();
                                deleteThumbnail(deleteURL2);
                            } else {
                                reportBug();
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
                final ProgressDialog pd = new ProgressDialog(EditProfileActivity.this);
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
                                Toast.makeText(EditProfileActivity.this, "Thumbnail deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                reportBug();
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
            final ProgressDialog pd = new ProgressDialog(EditProfileActivity.this);
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
                            Toast.makeText(EditProfileActivity.this,
                                    "Failed to upload.!", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        final ProgressDialog pd = new ProgressDialog(EditProfileActivity.this);
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
                        Toast.makeText(EditProfileActivity.this, "Failed to upload thumbnail.!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Button ok = dialog.findViewById(R.id.ok);
        Button cancel = dialog.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage(imageURL, thumbURL);
                dialog.dismiss();
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

    private void reportBug() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.report_window);
        Button report = dialog.findViewById(R.id.report_btn);
        Button cancel = dialog.findViewById(R.id.cancel_btn);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Reports");
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("Problem", "Image Could not delete");
                hashMap.put("UserId", fuser.getUid());
                hashMap.put("Email", getUserInfo(SP_EMAIL));
                hashMap.put("Name", getUserInfo(SP_USERNAME));
                reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Toast.makeText(EditProfileActivity.this,
                                    "Thank you for the feedback", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Error detected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
