package com.experiment.comp.application_house_rent_bd.Others;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.experiment.comp.application_house_rent_bd.CommonHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by comp on 7/13/2015.
 */
public class UploadCloudinaryImageTask extends AsyncTask<MyTaskParams,Void,Boolean> {

    Exception exception;

    @Override
    protected Boolean doInBackground(MyTaskParams... params) {
        MyTaskParams myFirstParam = params[0];

        Map config = new HashMap();
        config.put("cloud_name", "dhoswljp0");
        config.put("api_key", "347834125497797");
        config.put("api_secret", "R6BqHkg0ZCzg5vcKQAEr95IbheI");
        final Cloudinary cloudinary = new Cloudinary(config);
        final Map cMap = new HashMap();
        cMap.put("public_id",myFirstParam.getImageName());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        myFirstParam.getBitmap().compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        final ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);



        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    cloudinary.uploader().upload(bs, cMap);
                } catch (IOException e) {
                    //TODO: better error handling when image uploading fails
                    //e.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();

        return true;

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        CommonHelper.printVolleyErrorAtLog(exception);
    }
}
