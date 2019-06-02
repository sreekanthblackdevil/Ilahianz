package com.sreekanth.dev.ilahianz;
/*
 * This code {ProfileActivity}
 * Created on May-01-2019
 * Author Sreekanth K R
 * name Ilahianz
 * Github https://github.com/sreekanthblackdevil/Ilahianz
 */

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sreekanth.dev.ilahianz.Fragments.MyNotificationFragment;
import com.sreekanth.dev.ilahianz.Fragments.StoryFragment;
import com.sreekanth.dev.ilahianz.Supports.Supports;
import com.sreekanth.dev.ilahianz.Supports.ViewSupport;
import com.sreekanth.dev.ilahianz.model.User;
import com.sreekanth.dev.ilahianz.utils.FileUtil;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIO;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTHDAY_PRIVACY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_DAY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_MONTH;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_BIRTH_YEAR;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_CATEGORY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_CITY;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_CLASS_NAME;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_DEPARTMENT;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_DISTRICT;
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
    private DatabaseReference reference;
    private FirebaseUser fuser;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private Dialog profile_view;
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
    private TextView phone;
    private TextView email;
    private TextView class_name;
    private TextView nickname;
    private TextView location;
    private TextView bio;
    boolean fetched;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_view = new Dialog(this);
        pro_image = findViewById(R.id.profile_Image);
        birthday = findViewById(R.id.birthday);
        about = findViewById(R.id.description);
        location = findViewById(R.id.location);
        username = findViewById(R.id.username);
        nickname = findViewById(R.id.nickname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone_number);
        class_name = findViewById(R.id.class_name);
        bio = findViewById(R.id.bio);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mStorage = FirebaseStorage.getInstance();

        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        LinearLayout container = findViewById(R.id.container);
        ImageView back = findViewById(R.id.back_btn);
        Button edit_btn = findViewById(R.id.edit_profile_btn);

        init();

        // full screen
        container.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        container.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        ////
        //tab layout
        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new StoryFragment(), "Events");
        viewPagerAdapter.addFragment(new MyNotificationFragment(), "Notifications");
        viewPager.setAdapter(viewPagerAdapter);
        ////

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
                ViewSupport.setThumbProfileImage(user, pro_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    }

    private void init() {
        bio.setText(getUserInfo(SP_BIO));
        username.setText(getUserInfo(SP_USERNAME));
        username_txt = getUserInfo(SP_USERNAME);
        about.setText(getUserInfo(SP_ABOUT));
        location.setText(String.format("%s, %s",
                getUserInfo(SP_CITY), getUserInfo(SP_DISTRICT)));
        int birth_day = Integer.parseInt(getUserInfo(SP_BIRTH_DAY));
        int birth_month = Integer.parseInt(getUserInfo(SP_BIRTH_MONTH));
        int birth_year = Integer.parseInt(getUserInfo(SP_BIRTH_YEAR));
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
            class_name.setText(String.format("%s, %s",
                    getUserInfo(SP_DEPARTMENT), getUserInfo(SP_CLASS_NAME)));
        else class_name.setVisibility(View.GONE);

        if (!TextUtils.equals(getUserInfo(SP_NICKNAME), DEFAULT)) {
            nickname.setText(getUserInfo(SP_NICKNAME));
        } else {
            nickname.setText(getString(R.string.nick_name_not_provided));
        }

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
        Button ok = dialog.findViewById(R.id.ok);
        Button cancel = dialog.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (!Supports.Connected(ProfileActivity.this))
                    if (fetched)
                        deleteImage(imageURL, thumbURL);
                    else
                        Toast.makeText(ProfileActivity.this, "Collecting information...", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ProfileActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
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
        setUserInfo(SP_BIO, user.getBio());
        setUserInfo(SP_CITY, user.getCity());
        setUserInfo(SP_DISTRICT, user.getDistrict());
        setUserInfo(SP_DEPARTMENT, user.getDepartment());
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

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

    }

}