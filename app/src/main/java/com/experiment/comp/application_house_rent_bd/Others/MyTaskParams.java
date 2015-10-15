package com.experiment.comp.application_house_rent_bd.Others;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by comp on 7/13/2015.
 */
public class MyTaskParams {
    Bitmap bitmap;
    String imageName;

    public MyTaskParams(Bitmap bitmap, String imageName) {

        this.bitmap = bitmap;
        this.imageName = imageName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
