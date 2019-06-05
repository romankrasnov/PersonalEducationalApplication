package com.smallredtracktor.yourpersonaleducationalapplication.main.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.davemorrissey.labs.subscaleview.ImageSource;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.smallredtracktor.yourpersonaleducationalapplication.R;
import com.smallredtracktor.yourpersonaleducationalapplication.main.MVPproviders.ICreateTestFragmentMVPprovider;
import com.smallredtracktor.yourpersonaleducationalapplication.main.UIcomponents.CustomSubsamplingScaleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@SuppressLint("ValidFragment")
public class OcrDrawingDialog extends Dialog {
    private String filePath;
    private String id;
    private final OcrDrawingDialogListener listener;
    private CustomDrawerImageView ocrDialogImageView;
    private boolean mode;
    private Bitmap src;
    private Bitmap mutableBitmap;
    private List<PointF> sPoints = new ArrayList<>();
    private boolean isQuestion;

    public OcrDrawingDialog(Context context, ICreateTestFragmentMVPprovider.IPresenter presenter) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.listener = (OcrDrawingDialogListener) presenter;
        ocrDialogImageView = new CustomDrawerImageView(context);
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
                    listener.onOcrDrawingDialogUndo();
                    break;
                }
                case R.id.fab_redo: {
                    listener.onOcrDrawingDialogRedo();
                    break;
                }

                case R.id.fab_edit: {
                    listener.onOcrDrawingDialogEdit();
                    break;
                }

                case R.id.fab_touch: {
                    listener.onOcrDrawingDialogTouchModeFabOn();
                    break;
                }

                case R.id.fab_done: {
                    listener.onOcrDrawingDialogDone(src, sPoints, mutableBitmap, id, filePath, isQuestion);
                    break;
                }
            }
            return false;
        });
        CoordinatorLayout rootLayout = new CoordinatorLayout(context);
        rootLayout.addView(ocrDialogImageView, new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setAnchorId(View.NO_ID);
        lp.gravity = Gravity.BOTTOM | Gravity.END;
        speedDial.setLayoutParams(lp);
        rootLayout.addView(speedDial);
        this.setContentView(rootLayout);
    }

    @Override
    public void show() {
        ocrDialogImageView.setImage(ImageSource.cachedBitmap(src));
        super.show();
    }

    @Override
    public void dismiss() {
        ocrDialogImageView.clearPath();
        super.dismiss();
    }

    public void setDialogParams(String id, Bitmap bitmap, String path, boolean isQuestion) {
        this.mutableBitmap = bitmap;
        this.src = bitmap;
        this.id = id;
        this.filePath = path;
        this.isQuestion = isQuestion;
    }

    public void setDrawingMode(boolean mode) {
        this.mode = mode;
    }


    public void addPointToDrawerView(float x, float y) {
        ocrDialogImageView.addPolygonPoint(x, y);
    }

    public void recomputeLastDrawerViewPoint(float x, float y) {
        ocrDialogImageView.recomputeLastPolygonPoint(x, y);
    }


    public void undoDrawingDialogViewPoint() {
        ocrDialogImageView.removeLastPoint();
    }
    public void redoDrawingDialogViewPoint() {
        ocrDialogImageView.restoreLastPoint();
    }



    public interface OcrDrawingDialogListener {
        void onOcrDrawingDialogUndo();
        void onOcrDrawingDialogRedo();
        void onOcrDrawingDialogEdit();
        void onOcrDrawingDialogTouchModeFabOn();
        void onOcrDrawingDialogDone(Bitmap src, List<PointF> polygon, Bitmap mutable, String id, String path, boolean isQuestion);
        void onOcrDrawingDialogPolygonClosed();
        void onDrawerViewActionDown(MotionEvent event);
        void onDrawerViewActionMove(MotionEvent event);
        void onDrawerViewActionUp(MotionEvent event);
    }

    private class CustomDrawerImageView extends CustomSubsamplingScaleImageView implements View.OnTouchListener {
        private final Paint paint = new Paint();
        private Path vPath = new Path();
        private PointF vPoint = new PointF();
        private PointF vPrev = new PointF();
        private int strokeWidth;
        private PointF sCurrentF;
        private PointF sCurrent;
        private List<PointF> sPointsCache = new ArrayList<>();


        public CustomDrawerImageView(Context context, AttributeSet attr) {
            super(context, attr);
            initialise();
        }

        public CustomDrawerImageView(Context context) {
            super(context);
            initialise();
        }

        private void initialise() {
            setOnTouchListener(this);
            float density = getResources().getDisplayMetrics().densityDpi;
            strokeWidth = (int) (density / 120f);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(strokeWidth * 2);
            paint.setColor(Color.argb(255, 51, 181, 229));
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(@NonNull MotionEvent event) {
            if (!mode) {
                return super.onTouchEvent(event);
            } else {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        listener.onDrawerViewActionDown(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                            listener.onDrawerViewActionMove(event);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        listener.onDrawerViewActionUp(event);
                }
                return true;
            }
        }

        public void addPolygonPoint(float x, float y) {
            sCurrentF = viewToSourceCoord(x, y);
            sCurrent = new PointF(Objects.requireNonNull(sCurrentF).x, sCurrentF.y);
            sPoints.add(sCurrent);
            sPointsCache.add(sCurrent);
            invalidate();
        }

        public void recomputeLastPolygonPoint(float x, float y) {
            sCurrentF = viewToSourceCoord(x, y);
            sCurrent = new PointF(Objects.requireNonNull(sCurrentF).x, sCurrentF.y);
            sPoints.set(sPoints.size() - 1, sCurrent);
            invalidate();
        }


        public void removeLastPoint()
        {
            if(sPoints.size() > 0)
            {
                sPointsCache.add(sPoints.get(sPoints.size() - 1));
                sPointsCache.add(sPoints.get(sPoints.size() - 2));
                sPoints.remove(sPoints.size() - 1);
                sPoints.remove(sPoints.size() - 1);
                invalidate();
            }
        }

        public void restoreLastPoint()
        {
            if(sPointsCache.size() > 0)
            {
                sPoints.add(sPointsCache.get(sPointsCache.size() - 1));
                sPoints.add(sPointsCache.get(sPointsCache.size() - 2));
                sPointsCache.remove(sPointsCache.size() - 1);
                sPointsCache.remove(sPointsCache.size() - 1);
                invalidate();
            }
        }


        private void drawPathWithPaint(Canvas canvas)
        {
            canvas.drawPath(vPath, paint);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // Don't draw anything before image is ready.
            if (!isReady()) {
                return;
            }
            if (sPoints.size() >= 1) {
                vPath.reset();
                sourceToViewCoord(sPoints.get(0).x, sPoints.get(0).y, vPrev);
                vPath.moveTo(vPrev.x, vPrev.y);
                for (int i = 1; i < sPoints.size(); i++) {
                    sourceToViewCoord(sPoints.get(i).x, sPoints.get(i).y, vPoint);
                    vPath.quadTo(vPrev.x, vPrev.y, (vPoint.x + vPrev.x) / 2, (vPoint.y + vPrev.y) / 2);
                    vPrev= vPoint;
                }
                float dx = Math.abs(sPoints.get(0).x - sPoints.get(sPoints.size() - 1).x);
                float dy = Math.abs(sPoints.get(0).y - sPoints.get(sPoints.size() - 1).y);
                float dl = (float) Math.sqrt(dx * dx + dy * dy);
                if (dl < 20 * strokeWidth)
                {
                    if (sPoints.size() > 2)
                    {
                        vPath.close();
                        drawPathWithPaint(canvas);
                        listener.onOcrDrawingDialogPolygonClosed();
                    }
                } else
                {
                    drawPathWithPaint(canvas);
                }
            }
        }

        public void clearPath() {
            sPoints = new ArrayList<>();
            sPointsCache = new ArrayList<>();
            vPath.reset();
        }
    }
}

