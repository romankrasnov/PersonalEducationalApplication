package com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.UniqueUtils.UniqueDigit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.CompressUtil.LOCAL_PHOTO_QUALITY;

public class GalleryPathUtil {

    private final Context context;

    public GalleryPathUtil(Context context) {
        this.context = context;
    }

    public Single<String> save(Uri contentURI) {
        Single<String> path = Single.create(emitter -> {
            Bitmap bitmap;
            ByteArrayOutputStream bytes = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
                bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, LOCAL_PHOTO_QUALITY, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File photoFile = null;
            try {
                File storageDir = (context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                photoFile = File.createTempFile(
                        UniqueDigit.getUnique(),  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Log.d("thread", Thread.currentThread().getName() + "GALLERY PATH UTIL");
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
            emitter.onSuccess(photoFile.getAbsolutePath());
        });

        return path.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
