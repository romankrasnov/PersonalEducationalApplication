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

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PhotoIntentUtil {

    private static final String FILE_PROVIDER = "com.smallredtracktor.youreducationalapplication.android.fileprovider";
    private String path;
    private Context context;

    public PhotoIntentUtil(Context context) {
        this.context = context;
    }

    public Single<PhotoTakingSet> getPhotoTakingSet() {
        Single<PhotoTakingSet> s = Single.create(emitter -> {
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
                Log.d("thread", Thread.currentThread().getName() + "PHOTO INTENT UTIL");
                if (photoFile != null) {
                    path = photoFile.getAbsolutePath();
                    Uri photoURI = FileProvider.getUriForFile(context,
                            FILE_PROVIDER,
                            photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }
            }

            PhotoTakingSet result = new PhotoTakingSet();
            result.setIntent(intent);
            result.setPath(path);
            emitter.onSuccess(result);
        });

        return s.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public class PhotoTakingSet {

        private Intent intent;
        private String path;

        public Intent getIntent() {
            return intent;
        }

        public String getPath() {
            return path;
        }


        void setIntent(Intent intent) {
            this.intent = intent;
        }

        void setPath(String path) {
            this.path = path;
        }
    }
}


