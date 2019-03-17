package com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.smallredtracktor.yourpersonaleducationalapplication.main.Networking.OcrApiServise.OcrHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ParseTextUtil {

    public static String getParsedResult(String mPath)
    {
        File imgFile = new  File(mPath);
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return  OcrHelper.getIntance().getParsedText(base64, "rus");
    }
}
