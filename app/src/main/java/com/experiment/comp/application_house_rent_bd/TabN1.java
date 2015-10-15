package com.experiment.comp.application_house_rent_bd;

/**
 * Created by comp on 7/7/2015.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.experiment.comp.application_house_rent_bd.Adapter.HouseListAdapter;
import com.experiment.comp.application_house_rent_bd.Others.CustomRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TabN1 extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;
    HouseListAdapter houseListAdapter;
    MyDBHandler myDBHandler;
    ArrayList<RentItem> rentItems;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab1, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        myDBHandler = new MyDBHandler(getActivity(),null,null,1);
        rentItems = myDBHandler.getListData();




        houseListAdapter  = new HouseListAdapter(getActivity(),rentItems);
        setListAdapter(houseListAdapter);

        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        RentItem itemAtPosition = (RentItem) l.getItemAtPosition(position);
        Intent intent1;
        intent1 = new Intent();
        intent1.setClass(getActivity(),ShowRentInfo.class);
        //Log.d("Shout", "Rent at position " + itemAtPosition.getRentImageName());
        //Log.d("Shout","Rent at position "+itemAtPosition.getRentGoogleMapUrl());

        intent1.putExtra("rentItem", itemAtPosition);

        // 4. start the activity
        startActivity(intent1);

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        try {
            fetchData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        swipeRefreshLayout.setRefreshing(false);


    }

    public void fetchData() throws JSONException {

        Map<String,String> params = new HashMap<>();

        params.put("recent_id", String.valueOf(myDBHandler.latestID()));
        //Log.d("Shout","OKay4");
        params.put("old_id", String.valueOf(myDBHandler.lastID()));


        CustomRequest jsonRequest =  new CustomRequest(Request.Method.POST,CommonHelper.updateURL,params,
                this.customResponse(),this.customErrorListener()
        );


        Volley.newRequestQueue(getActivity()).add(jsonRequest);


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
                rentItems.clear();
                rentItems.addAll(myDBHandler.getListData());
                //Log.d("Shout","rentItems in tab1 ");
                for(RentItem rentItem : rentItems)
                {
                    rentItem.showAtLog();
                }

                houseListAdapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);



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