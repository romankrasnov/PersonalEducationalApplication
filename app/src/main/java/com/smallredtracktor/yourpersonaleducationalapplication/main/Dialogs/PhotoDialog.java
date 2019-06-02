package com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;


import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.CompressUtil;



public class PhotoDialog extends Dialog {

    private final SubsamplingScaleImageView imageView;
    private Bitmap bitmap;

    public PhotoDialog(Context context) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        imageView = new SubsamplingScaleImageView(context);
        this.setContentView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void show() {
        imageView.setImage(ImageSource.cachedBitmap(bitmap));
        super.show();
    }


    public void setDialogParams(String id, Bitmap bitmap, int type, boolean isQuestion) {
        this.bitmap = bitmap;
    }
}
