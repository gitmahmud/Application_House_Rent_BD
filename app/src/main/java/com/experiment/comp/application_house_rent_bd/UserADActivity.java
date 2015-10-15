package com.experiment.comp.application_house_rent_bd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cloudinary.Cloudinary;
import com.dd.processbutton.iml.ActionProcessButton;
import com.dd.processbutton.iml.SubmitProcessButton;
import com.experiment.comp.application_house_rent_bd.Others.CustomRequest;
import com.experiment.comp.application_house_rent_bd.Others.MyTaskParams;
import com.experiment.comp.application_house_rent_bd.Others.ProgressGenerator;
import com.experiment.comp.application_house_rent_bd.Others.SessionManager;
import com.experiment.comp.application_house_rent_bd.Others.UploadCloudinaryImageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by comp on 7/13/2015.
 */
public class UserADActivity extends ActionBarActivity implements ProgressGenerator.OnCompleteListener{
    private int PICK_IMAGE_REQUEST = 1;
    EditText editTextTitle,editTextDescription,editTextArea,editTextContact;
    ImageView imageView;
    Bitmap bitmap;
    ImageButton buttonAttach;
    Typeface tf;
    SubmitProcessButton buttonAdd ;
    ProgressGenerator progressGenerator;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        tf = Typeface.createFromAsset(getAssets(),
                "font/solaimanlipinormal.ttf");

        editTextTitle = (EditText) findViewById(R.id.editText1);
        editTextDescription = (EditText) findViewById(R.id.editText2);
        editTextArea = (EditText) findViewById(R.id.editText3);
        editTextContact = (EditText) findViewById(R.id.editText4);
        imageView = (ImageView) findViewById(R.id.imageView);
        buttonAttach = (ImageButton) findViewById(R.id.button2);
        buttonAdd = (SubmitProcessButton) findViewById(R.id.button);
        progressGenerator = new ProgressGenerator(this);


        typeFaceInitilisation();







    }
    private void typeFaceInitilisation()
    {
       editTextTitle.setTypeface(tf);
       editTextDescription.setTypeface(tf);
       editTextContact.setTypeface(tf);
       editTextArea.setTypeface(tf);
    }


    public void pickImage(View view )
    {

        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                buttonAttach.setVisibility(View.INVISIBLE);
                imageView.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void addAD(View view) throws IOException {
        progressGenerator.start(buttonAdd);

        String imageName=null;
        if(bitmap != null) {

            imageName = String.valueOf(System.currentTimeMillis());
            Log.d("Shout","Flag cloud");

            //MyTaskParams myTaskParams = new MyTaskParams(bitmap,imageName);
            //new UploadCloudinaryImageTask().execute(myTaskParams);


            Map config = new HashMap();
            config.put("cloud_name", "dhoswljp0");
            config.put("api_key", "347834125497797");
            config.put("api_secret", "R6BqHkg0ZCzg5vcKQAEr95IbheI");
            final Cloudinary cloudinary = new Cloudinary(config);
            final Map cMap = new HashMap();
            cMap.put("public_id",imageName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
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





        }




        Map<String,String> params = new HashMap<>();
        SessionManager sessionManager = new SessionManager(getApplicationContext());

        params.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        params.put("title", editTextTitle.getText().toString());
        //Log.d("Shout","OKay4");
        params.put("description", editTextDescription.getText().toString());
        params.put("area", editTextArea.getText().toString());
        params.put("contact", editTextContact.getText().toString());
        if(imageName != null) {
            params.put("image", imageName.toString());
        }
        Log.d(getClass().getName(), "image name " + imageName);
        params.put("author", sessionManager.getUID());
        for (Map.Entry entry : params.entrySet()) {
            Log.d("Shout",entry.getKey() + ", " + entry.getValue());
        }

        CustomRequest jsonRequest =  new CustomRequest(Request.Method.POST,CommonHelper.insertURL,params,
                customResponse(),customErrorListener()
        );
        //jsonRequest


        Volley.newRequestQueue(this).add(jsonRequest);




    }

    public Response.Listener<JSONObject> customResponse()
    {
        Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Shout","response "+response.toString());

                try {
                    if(response.getInt("success") == 1)
                    {

                        buttonAdd.setEnabled(false);

                        Toast.makeText(getApplicationContext(),"Successfully added.",Toast.LENGTH_LONG).show();


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Operation unsuccessful.",Toast.LENGTH_LONG).show();

                    }
                    //Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    //startActivity(intent);
                    finish();
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

    public void finish() {
        Intent data = new Intent();

        setResult(RESULT_OK, data);
        super.finish();
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

    @Override
    public void onComplete() {

    }
}
