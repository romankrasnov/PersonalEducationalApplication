package com.smallredtracktor.yourpersonaleducationalapplication.main.Utils.PhotoUtils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PolygonCropUtil {

    public Single<Bitmap> getCroppedBitmap(Bitmap src, List<PointF> sPoints, Bitmap mutableBitmap)
        {
            Single<Bitmap> s = Single.create(emitter -> {
                int size = sPoints.size();
                if(size > 3)
                {
                    Bitmap dest = Bitmap.createBitmap(
                            src.getWidth(), src.getHeight(), src.getConfig());
                    float[] listX = new float[size];
                    float[] listY = new float[size];
                    PointF pointF;
                    for (int i = 0; i<size; i++)
                    {
                        pointF = sPoints.get(i);
                        listX[i] = pointF.x;
                        listY[i] = pointF.y;
                    }
                    //precomputed Rect to draw
                    Arrays.sort(listX);
                    Arrays.sort(listY);
                    int minX = (int) listX[0];
                    int minY = (int) listY[0];
                    int maxX = (int) listX[size - 1];
                    int maxY = (int) listY[size - 1];
                    for (int x = minX; x < maxX; x++) {
                        for (int y = minY; y < maxY; y++) {
                            if (isContains(sPoints, x, y)) {
                                int pixelColor = mutableBitmap.getPixel(x, y);
                                int pixelAlpha = Color.alpha(pixelColor);
                                int pixelRed = Color.red(pixelColor);
                                int pixelGreen = Color.green(pixelColor);
                                int pixelBlue = Color.blue(pixelColor);
                                int newPixel = Color.argb(
                                        pixelAlpha, pixelBlue, pixelRed, pixelGreen);
                                dest.setPixel(x, y, newPixel);
                            }
                        }
                    }
                    emitter.onSuccess(dest);
                } else 
                    {
                        emitter.onSuccess(mutableBitmap);
                    }
                
            });
                    return s.subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread());
        }


        private boolean isContains(List<PointF> poly, int x, int y) {
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = poly.size() - 1; i < poly.size(); j = i++) {
            if ((poly.get(i).y > y) != (poly.get(j).y > y) &&
                    (x < (poly.get(j).x - poly.get(i).x) * (y - poly.get(i).y) /
                            (poly.get(j).y - poly.get(i).y) + poly.get(i).x)) {
                result = !result;
            }
        }
        return result;
    }
}
