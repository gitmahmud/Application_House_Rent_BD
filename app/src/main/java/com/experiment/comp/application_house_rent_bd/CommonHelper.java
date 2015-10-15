package com.experiment.comp.application_house_rent_bd;

import android.util.Log;

import com.android.volley.VolleyError;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by comp on 7/6/2015.
 */
public class CommonHelper {
    public static String updateURL = "http://housedb.freeiz.com/update_all.php";
    public static String insertURL = "http://housedb.freeiz.com/insert_item.php";
    public static String markURL = "http://housedb.freeiz.com/update_mark.php";


    public static String imageURL = "http://res.cloudinary.com/dhoswljp0/image/upload/";

    public static String URL_LOGIN = "http://housedb.freeiz.com/index.php";

    // Server user register url
    public static String URL_REGISTER = "http://housedb.freeiz.com/index.php";


    public static void printVolleyErrorAtLog(Exception error)
    {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        error.printStackTrace(printWriter);
        String s = writer.toString();

        Log.d("Shout", s);

    }



}









   /*JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,updateURL,object,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("Shout","Flag");
                        removeArchievedData(response);
                        loadUpdatedData(response);
                        Intent i = new Intent(getApplicationContext(),RentListActivity.class);
                        startActivity(i);
                    }

                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Shout","JSON response error");
                        Writer writer = new StringWriter();
                        PrintWriter printWriter = new PrintWriter(writer);
                        error.printStackTrace(printWriter);
                        String s = writer.toString();
                        Log.d("Shout", s);
                    }
                }

        ) {
           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                Log.d("Shout","Latest ID "+String.valueOf(latestId)+" Last ID "+String.valueOf(lastID));
                params.put("recent_id",String.valueOf(latestId));
                params.put("old_id",String.valueOf(lastID));
                return params;

            }



            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                   Log.d("Shout","Json String "+jsonString);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return super.parseNetworkResponse(response);
            }
        };
        */

