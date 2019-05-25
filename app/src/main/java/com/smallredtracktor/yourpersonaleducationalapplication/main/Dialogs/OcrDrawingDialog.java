package com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;


import com.davemorrissey.labs.subscaleview.ImageSource;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.CompressUtil;
import com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils.CustomSubsamplingScaleImageView;


@SuppressLint("ValidFragment")
public class OcrDrawingDialog extends Dialog {
    private String content;
    private String id;
    private final CompressUtil util;
    private final OcrDrawingDialogListener listener;
    private CustomSubsamplingScaleImageView ocrDialogImageView;
    private CoordinatorLayout rootLayout;

    public OcrDrawingDialog(Context context, CompressUtil util, ICreateTestFragmentMVPprovider.IPresenter presenter) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.util = util;
        this.listener = (OcrDrawingDialogListener) presenter;
        ocrDialogImageView = new CustomSubsamplingScaleImageView(context);
        SpeedDialView speedDial = new SpeedDialView(context);
        speedDial.getMainFab().setImageResource(R.drawable.ic_toolbox);
        speedDial.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_redo, R.drawable.ic_arrow_right).create());
        speedDial.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_undo, R.drawable.ic_arrow_left).create());
        speedDial.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_edit, R.drawable.ic_polygon).create());
        speedDial.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_touch, R.drawable.ic_touch).create());
        speedDial.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_done, R.drawable.ic_done).create());
        speedDial.setOnActionSelectedListener(speedDialActionItem -> {
            switch (speedDialActionItem.getId()) {
                case R.id.fab_undo: {
                    listener.onOcrDrawingDialogUndo(id, null);
                    break;
                }
                case R.id.fab_redo: {
                    listener.onOcrDrawingDialogRedo(id, null);
                    break;
                }

                case R.id.fab_edit: {
                    listener.onOcrDrawingDialogEdit(id, null);
                    break;
                }

                case R.id.fab_touch: {
                    listener.onOcrDrawingDialogTouchModeFabOn(id, null);
                    break;
                }

                case R.id.fab_done: {
                    listener.onOcrDrawingDialogDone(id, null);
                    break;
                }
            }
            return false;
        });
        rootLayout = new CoordinatorLayout(context);
        rootLayout.addView(ocrDialogImageView,new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setAnchorId(View.NO_ID);
        lp.gravity = Gravity.BOTTOM | Gravity.END;
        speedDial.setLayoutParams(lp);
        rootLayout.addView(speedDial);
    }

    @Override
    public void show() {
        util.getBitmap(content)
                .doOnSuccess(bitmap -> ocrDialogImageView.setImage(ImageSource.cachedBitmap(bitmap)))
                .subscribe();
        this.setContentView(rootLayout);
        super.show();
    }


    public void setDialogParams(String id, String content) {
        this.content = content;
        this.id = id;
    }

    public interface OcrDrawingDialogListener {
        void onOcrDrawingDialogUndo(String id, Bitmap takenImage);
        void onOcrDrawingDialogRedo(String id, Bitmap selectedBitmapRegion);
        void onOcrDrawingDialogEdit(String id, Object o);
        void onOcrDrawingDialogTouchModeFabOn(String id, Object o);
        void onOcrDrawingDialogDone(String id, Object o);
    }
}
