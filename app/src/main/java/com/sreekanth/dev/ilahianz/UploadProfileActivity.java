package com.sreekanth.dev.ilahianz;
/**
 * This Code
 * Created in 2019
 * Author Sreekanth K R
 * Name Ilahianz
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
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.sreekanth.dev.ilahianz.Supports.Supports;
import com.sreekanth.dev.ilahianz.Supports.ViewSupport;
import com.sreekanth.dev.ilahianz.model.User;
import com.sreekanth.dev.ilahianz.utils.FileUtil;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.sreekanth.dev.ilahianz.model.Literals.CAMERA_REQUEST;
import static com.sreekanth.dev.ilahianz.model.Literals.IMAGE_REQUEST;
import static com.sreekanth.dev.ilahianz.model.Literals.SP_USERNAME;

public class UploadProfileActivity extends AppCompatActivity {


    CircleImageView profileImage;
    Button upload_btn, skip_btn;
    TextView username;
    DatabaseReference reference;
    FirebaseUser fuser;
    boolean fetched = false;
    Dialog dialog;
    File croppedImage, compressedImage, thumbnail, actualImage;
    StorageReference storageReference;
    StorageTask uploadTask;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile);
        profileImage = findViewById(R.id.profile_Image);
        upload_btn = findViewById(R.id.upload_btn);
        skip_btn = findViewById(R.id.skip_btn);
        username = findViewById(R.id.username);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        init();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals(upload_btn.getText(), "UPLOAD"))
                    openSheet();
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals(upload_btn.getText(), "UPLOAD"))
                    openSheet();
                else startActivity(new Intent(UploadProfileActivity.this, Startup.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadProfileActivity.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

    public String getUserInfo(String key) {
        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        upload_btn.setText("UPLOAD");
        username.setText(getUserInfo(SP_USERNAME));
        reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                fetched = true;
                ViewSupport.setProfileImage(user, profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                            openCamera();
                            dialog.dismiss();

                        }
                    });
                    gallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openImage();
                            dialog.dismiss();
                        }
                    });
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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
            final ProgressDialog pd = new ProgressDialog(UploadProfileActivity.this);
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
        String destination = getUserInfo(SP_USERNAME);
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

    private void UpdateInfo(String key, String value) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(key, value);
        reference.updateChildren(hashMap);
    }

    private void uploadImage() {
        if (!Supports.Connected(this)) {
            final ProgressDialog pd = new ProgressDialog(UploadProfileActivity.this);
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
                            Toast.makeText(UploadProfileActivity.this, "Failed to upload.!", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        final ProgressDialog pd = new ProgressDialog(UploadProfileActivity.this);
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
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = (Uri) task.getResult();
                        assert downloadUri != null;
                        String mUri = downloadUri.toString();
                        UpdateInfo("thumbnailURL", mUri);
                        upload_btn.setText("NEXT");
                        pd.dismiss();
                    } else {
                        Toast.makeText(UploadProfileActivity.this, "Failed to upload thumbnail.!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            pd.dismiss();
        }
    }
}
