package com.experiment.comp.application_house_rent_bd;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.experiment.comp.application_house_rent_bd.Others.CustomRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by comp on 7/7/2015.
 */
public class DataFetcher {
    private Context context;
    MyDBHandler myDBHandler;

    public DataFetcher(Context context)
    {
        //Log.d("Shout","OKay2");
        this.context = context;
        myDBHandler = new MyDBHandler(context,null,null,1);
        //Log.d("Shout","OKay3");
    }

    public void fetchData() throws JSONException {

        Map<String,String> params = new HashMap<String,String>();

        params.put("recent_id", String.valueOf(myDBHandler.latestID()));
        //Log.d("Shout","OKay4");
        params.put("old_id", String.valueOf(myDBHandler.lastID()));


        CustomRequest jsonRequest =  new CustomRequest(Request.Method.POST,CommonHelper.updateURL,params,
                this.customResponse(),this.customErrorListener()
        );


        Volley.newRequestQueue(context).add(jsonRequest);


    }

    public Response.Listener<JSONObject> customResponse()
    {
        Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                removeArchievedData(response);
                loadUpdatedData(response);
                //Intent i = new Intent(getApplicationContext(),RentListActivity.class);
                //startActivity(i);

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



    public void removeArchievedData(JSONObject response)
    {
        try
        {
            //Log.d("Shout","Flags ");
            if(response.getInt("success_old_item") == 1)
            {

                JSONArray jsonArray = response.getJSONArray("remove_items");
                int[] removeID = new int[jsonArray.length()];

                for(int i =0;i<jsonArray.length();i++)
                {
                    removeID[i] = Integer.parseInt(jsonArray.getString(i));
                }


                myDBHandler.showCurrentSQLiteDB();

                Log.d("Shout","ID to be removed ");
                for(int x:removeID)
                {
                    Log.d("Shout",String.valueOf(x));
                }
                myDBHandler.removeIDs(removeID);
                myDBHandler.showCurrentSQLiteDB();

            }
            else {
                Log.d("Shout","No content to be deleted.");
            }
        }
        catch (JSONException e) {
            Log.d("Shout","JSON exception 2 ");
            e.printStackTrace();
        }

    }

    public void loadUpdatedData(JSONObject response)
    {

        try {
            //Log.d("Shout","Got Response");
            if(response.getInt("success_new_item") == 1 )
            {

                ArrayList<RentItem> rentItems = new ArrayList<RentItem>();
                JSONArray jsonArray = response.getJSONArray("rents");
                Log.d("Shout","New items "+jsonArray.length());

                for(int i =0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    RentItem rentItem = new RentItem();
                    rentItem.set_rentId(Integer.parseInt(jsonObject.getString("id")));
                    rentItem.setRentTitle(jsonObject.getString("title"));
                    //Log.d("Shout","Flag");
                    Timestamp timestamp =  DateTimeHelper.StringToDate(jsonObject.getString("posttime"));
                    //Log.d("Shout","timestamp "+timestamp.toString());

                    rentItem.setRentPosttime(timestamp);
                    rentItem.setRentDescription(jsonObject.getString("description"));
                    rentItem.setRentContact(jsonObject.getString("contact"));
                    rentItem.setRentImageName(jsonObject.getString("imageurl"));
                    rentItem.setRentStatus(RentStatus.valueOf(jsonObject.getString("status")));
                    rentItem.setRentArea(jsonObject.getString("area"));
                    rentItem.setRentGoogleMapUrl(jsonObject.getString("googlemapurl"));
                    rentItem.showAtLog();

                    rentItems.add(rentItem);

                }
                Log.d("Shout","Arraylist Created for insertion. Size"+rentItems.size());

                myDBHandler.insertRentItem(rentItems);




            }
            else
            {
                Log.d("Shout","No updated Content");
            }




        } catch (JSONException e) {
            Log.d("Shout","JSON exception 1 ");
            e.printStackTrace();
        }



    }
}
