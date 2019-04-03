package com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;

import java.io.File;
import java.io.IOException;

public class PhotoIntent {

    public static String mPath;

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    File storageDir = (context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                    photoFile = File.createTempFile(
                            UniqueDigit.getUnique(),  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */
                    );

                } catch (IOException ex) {
                    // Error occurred while creating the File
                }

                if (photoFile != null) {
                    mPath = photoFile.getAbsolutePath();
                    Uri photoURI = FileProvider.getUriForFile(context,
                            "com.smallredtracktor.youreducationalapplication.android.fileprovider",
                            photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }
            }
        return intent;
    }
}


