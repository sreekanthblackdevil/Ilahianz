package com.sreekanth.dev.ilahianz.Supports;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.sreekanth.dev.ilahianz.R;
import com.sreekanth.dev.ilahianz.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileSupports {
    /**
     * This code
     * Created on May-01-2019
     * Author Sreekanth K R
     * Github https://github.com/sreekanthblackdevil/Ilahianz
     */


    public static void saveImage(Context context, ImageView imageView, User user) {
        BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = draw.getBitmap();
        FileOutputStream outStream;
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/Ilahianz/" + user.getUsername());
        if (!dir.exists())
            if (dir.mkdirs()) {
                String fileName = String.format(context.getString(R.string.image_file_formate), System.currentTimeMillis());
                File outFile = new File(dir, fileName);
                try {
                    outStream = new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                    Toast.makeText(context,
                            "saved to the gallery", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                Toast.makeText(context, "Failed to create directory", Toast.LENGTH_SHORT).show();
    }
}
