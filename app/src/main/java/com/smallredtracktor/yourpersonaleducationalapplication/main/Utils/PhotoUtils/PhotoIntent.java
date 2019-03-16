package com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;

import java.io.File;
import java.io.IOException;

public class PhotoIntent {

    private static Intent intent;
    public static Uri photoURI;

    public static Intent getInstance(Context context) {
        if (intent == null) {
             intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

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
                // Continue only if the File was successfully created
                if (photoFile != null) {
                     photoURI = FileProvider.getUriForFile(context,
                            "com.smallredtracktor.yourpersonaleducationalapplication.android.fileprovider",
                            photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }
                return intent;
            }
        }
        return intent;
    }
}


