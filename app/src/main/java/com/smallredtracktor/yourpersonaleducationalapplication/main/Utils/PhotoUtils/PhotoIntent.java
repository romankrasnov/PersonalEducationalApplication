package com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;

import java.io.File;
import java.io.IOException;

public class PhotoIntent {

    private static final String FILE_PROVIDER = "com.smallredtracktor.youreducationalapplication.android.fileprovider";
    private static String path;

    public static Intent newInstance(Context context) {
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
                    path = photoFile.getAbsolutePath();
                    Uri photoURI = FileProvider.getUriForFile(context,
                            FILE_PROVIDER,
                            photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }
            }
        return intent;
    }

    public static String getPath() {
        return path;
    }

}


