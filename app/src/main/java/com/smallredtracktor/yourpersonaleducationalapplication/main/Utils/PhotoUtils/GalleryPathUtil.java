package com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GalleryPathUtil {


    public static String save(Uri contentURI, FragmentActivity activity) {
        Bitmap bitmap;
        ByteArrayOutputStream bytes = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), contentURI);
            bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File photoFile = null;
        try {
            File storageDir = (activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            photoFile = File.createTempFile(
                    UniqueDigit.getUnique(),  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (photoFile != null) {
            try {
                FileOutputStream fo = new FileOutputStream(photoFile);
                try {
                    assert bytes != null;
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        assert photoFile != null;
        return photoFile.getAbsolutePath();
    }
}
