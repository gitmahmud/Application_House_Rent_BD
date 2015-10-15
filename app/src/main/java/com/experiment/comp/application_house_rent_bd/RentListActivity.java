package com.experiment.comp.application_house_rent_bd;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.experiment.comp.application_house_rent_bd.Adapter.HouseListAdapter;

import java.util.ArrayList;

/**
 * Created by comp on 7/3/2015.
 */
public class RentListActivity extends ListActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all);
        listView = (ListView) findViewById(android.R.id.list);
        MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
        final ArrayList<RentItem> rentItems = myDBHandler.getListData();
        HouseListAdapter houseListAdapter  = new HouseListAdapter(this,rentItems);
        listView.setAdapter(houseListAdapter);      //listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RentItem itemAtPosition = (RentItem) parent.getItemAtPosition(position);
                Intent intent1;
                intent1 = new Intent();
                intent1.setClass(getApplicationContext(),ShowRentInfo.class);
                Log.d("Shout","Rent at position "+itemAtPosition.getRentImageName());
                Log.d("Shout","Rent at position "+itemAtPosition.getRentGoogleMapUrl());

                intent1.putExtra("rentItem", itemAtPosition);

                // 4. start the activity
                startActivity(intent1);



            }
        });




    }

}
