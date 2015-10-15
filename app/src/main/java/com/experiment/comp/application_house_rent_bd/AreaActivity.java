package com.experiment.comp.application_house_rent_bd;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.experiment.comp.application_house_rent_bd.Adapter.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by comp on 7/6/2015.
 */
public class AreaActivity extends ExpandableListActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<RentItem>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_area);

        // get the listview
        expListView = (ExpandableListView) findViewById(android.R.id.list);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                /* Toast.makeText(getApplicationContext(),
                 "Group Clicked " + listDataHeader.get(groupPosition),
                 Toast.LENGTH_SHORT).show();
                 */
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
               /* Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
                        */
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
               /* Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

                        */

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

                Log.d("Shout","CHild selected . ");
                RentItem rentItem = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),ShowRentInfo.class);
                intent.putExtra("rentItem",rentItem);
                startActivity(intent);

                return false;
            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);

        listDataHeader = new ArrayList<String>(myDBHandler.getDistinctAreas());
        Log.d("Nout","DataHeadders");
        for(String s : listDataHeader)
        {
            Log.d("Nout",s);
        }

        listDataChild = new HashMap<String, List<RentItem>>();

        for(String area : listDataHeader)
        {
            List<RentItem> group = new ArrayList<RentItem>(myDBHandler.getAreaItems(area)) ;

            listDataChild.put(area,group);

        }

    }
}
