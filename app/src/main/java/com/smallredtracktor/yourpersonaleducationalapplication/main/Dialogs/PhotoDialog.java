package com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs;


import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;


import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.CompressUtil;



public class PhotoDialog extends Dialog {

    private final CompressUtil util;
    private String content;
    private final SubsamplingScaleImageView imageView;


    public PhotoDialog(Context context, CompressUtil util) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.util = util;
        imageView = new SubsamplingScaleImageView(context);
    }

    @Override
    public void show() {
                util.getBitmap(content)
                    .doOnSuccess(bitmap -> imageView.setImage(ImageSource.cachedBitmap(bitmap)))
                    .subscribe();

        this.setContentView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        super.show();
    }


    public void setDialogParams(String id, String content, int type, boolean isQuestion) {
        this.content = content;
    }
}
