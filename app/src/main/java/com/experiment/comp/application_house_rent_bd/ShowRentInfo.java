package com.experiment.comp.application_house_rent_bd;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.dd.processbutton.iml.ActionProcessButton;
import com.experiment.comp.application_house_rent_bd.Others.CustomImageView;
import com.experiment.comp.application_house_rent_bd.Others.CustomRequest;
import com.experiment.comp.application_house_rent_bd.Others.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by comp on 7/6/2015.
 */
public class ShowRentInfo extends ActionBarActivity {
    TextView textViewTitle,textViewDesc,textViewContact,textViewArea;
    CustomImageView imageView;
    RentItem rentItem;
    Typeface tf;
    ActionProcessButton buttonFalse,buttonBooked;
// no progress




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_item);
        Intent intent= getIntent();
        rentItem = (RentItem) intent.getSerializableExtra("rentItem");
        Log.d("Shout","Show Rent Info");
        rentItem.showAtLog();
        tf = Typeface.createFromAsset(getAssets(),
                "font/solaimanlipinormal.ttf");


        textViewTitle = (TextView) findViewById(R.id.textView7);
        textViewDesc = (TextView) findViewById(R.id.textView8);
        textViewContact = (TextView) findViewById(R.id.textView9);
        textViewArea = (TextView) findViewById(R.id.textView10);
        imageView = (CustomImageView) findViewById(R.id.imageView2);


        initializeActionButton();


        textViewTitle.setText(rentItem.getRentTitle());

        textViewDesc.setText(rentItem.getRentDescription());
        //Log.d("Shout",rentItem.getRentDescription());


        textViewContact.setText(rentItem.getRentContact());
        textViewArea.setText(rentItem.getRentArea());

        if(rentItem.getRentImageName() !=null)
        {
            Log.d("Shout",CommonHelper.imageURL+rentItem.getRentImageName());

            ImageRequest imageRequest = new ImageRequest(CommonHelper.imageURL+rentItem.getRentImageName(),
                    this.onImageResponse(),0,0,
                    CustomImageView.ScaleType.FIT_XY,Bitmap.Config.ARGB_8888, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Shout","Image Error");
                    Log.d("Shout",CommonHelper.imageURL+rentItem.getRentImageName());
                    CommonHelper.printVolleyErrorAtLog(error);
                    error.printStackTrace();
                    showDefaultImage();
                }
            });

            Volley.newRequestQueue(this).add(imageRequest);
        }
        else {
            showDefaultImage();

        }


        typeFaceInitilisation();


    }
    private void typeFaceInitilisation()
    {
        textViewTitle.setTypeface(tf);
        textViewDesc.setTypeface(tf);
        textViewContact.setTypeface(tf);
        textViewArea.setTypeface(tf);

    }
    private void initializeActionButton()
    {
        buttonBooked = (ActionProcessButton) findViewById(R.id.button10);
        buttonBooked.setMode(ActionProcessButton.Mode.PROGRESS);
        buttonBooked.setProgress(0);



        buttonFalse = (ActionProcessButton) findViewById(R.id.button11);
        buttonFalse.setMode(ActionProcessButton.Mode.PROGRESS);
        buttonFalse.setProgress(0);


    }


    public void showDefaultImage() {

        AssetManager assetManager = getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open("no_image.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Shout","Default image shown");
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        imageView.setImageBitmap(bitmap);
    }


    private Response.Listener<Bitmap> onImageResponse()
    {
        Response.Listener<Bitmap> bitmapListener = new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap response) {
                Log.d("Shout","Got image response");
                imageView.setImageBitmap(response);

            }
        };


        return bitmapListener;

    }



    public void handleUpdate(View view )
    {
        Map<String,String> params = new HashMap<>();
        params.put("itemid",String.valueOf(rentItem.get_rentId()));

        if(view.getId() == buttonBooked.getId()){
            buttonBooked.setProgress(50);
            params.put("value",String.valueOf(1));
            Log.d("Shout","Value booked");

            buttonBooked.setProgress(75);

        }


        if(view.getId() == buttonFalse.getId()){
            // progressDrawable cover 50% of button width, progressText is shown
            buttonFalse.setProgress(50);

            params.put("value",String.valueOf(2));
            Log.d("Shout","Value false");

            // progressDrawable cover 75% of button width, progressText is shown
            buttonFalse.setProgress(75);

        }




        //Log.d(getClass().getName(), "image name " + imageName);
        //params.put("author", sessionManager.getUID());
        for (Map.Entry entry : params.entrySet()) {
            Log.d("Shout",entry.getKey() + ", " + entry.getValue());
        }

        CustomRequest jsonRequest =  new CustomRequest(Request.Method.POST,CommonHelper.markURL,params,
                customResponse(),customErrorListener()
        );


        Volley.newRequestQueue(this).add(jsonRequest);


    }

    public Response.Listener<JSONObject> customResponse()
    {
        Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response.getInt("success") == 1)
                    {
                        // completeColor & completeText is shown
                        buttonBooked.setProgress(100);
                        buttonBooked.setEnabled(false);




                    }
                    if(response.getInt("success") == 2)
                    {

                        buttonFalse.setProgress(100);
                        buttonFalse.setEnabled(false);



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        return jsonObjectListener;


    }

    public Response.ErrorListener customErrorListener()
    {
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Shout", "JSON response error");
                CommonHelper.printVolleyErrorAtLog(error);

            }
        };
        return errorListener;

    }












    public void startMap(View view)
    {
        Uri webpage = Uri.parse(rentItem.getRentGoogleMapUrl());
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(webIntent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if(isIntentSafe) {
            startActivity(webIntent);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
