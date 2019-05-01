package com.sreekanth.dev.ilahianz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.yalantis.ucrop.UCrop;

import java.io.File;


public class CropActivity extends AppCompatActivity {


    ImageView img;

    private final int CODE_IMAGE_GALLERRY = 1;
    private final String SAMPLE_CROP_IMAGE_NAME = "samplecrop";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        img = findViewById(R.id.image);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent()
                        .setAction(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"), CODE_IMAGE_GALLERRY);
            }
        });
    }

    private void startCrop(Uri uri) {
        String destination = SAMPLE_CROP_IMAGE_NAME;
        destination += ".jpeg";
        UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destination)))
                .withAspectRatio(1, 1)
                .withOptions(getOption())
                .withMaxResultSize(1280, 1280)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_IMAGE_GALLERRY && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            startCrop(imageUri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri uri = UCrop.getOutput(data);
            if (uri != null) {
                img.setImageURI(uri);
            }
        }
    }

    private UCrop.Options getOption() {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarTitle("Profile Image");
        options.setCompressionQuality(100);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        return options;
    }
}
