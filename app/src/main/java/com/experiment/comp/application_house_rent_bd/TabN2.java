package com.experiment.comp.application_house_rent_bd;

/**
 * Created by comp on 7/7/2015.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.experiment.comp.application_house_rent_bd.Adapter.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TabN2 extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<RentItem>> listDataChild;
    private int lastExpandedPosition = -1;

    public TabN2() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_list_area, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get the listview
        expListView = (ExpandableListView)getView().findViewById(android.R.id.list);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
               /* Toast.makeText(getActivity(),
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
                /*Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();

                        */
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;


            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*Toast.makeText(getActivity(),
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

                RentItem rentItem = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                Intent intent = new Intent();
                intent.setClass(getActivity(),ShowRentInfo.class);
                intent.putExtra("rentItem",rentItem);
                startActivity(intent);


                /*Toast.makeText(
                        getActivity(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();

                        */
                return false;
            }
        });




    }




    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser)
        {

            prepareListData2();
            Log.d("Shout","Visible Tab 2");
            listAdapter.notifyDataSetChanged();

        }



    }



    private void prepareListData() {
        MyDBHandler myDBHandler = new MyDBHandler(getActivity(),null,null,1);

        listDataHeader = new ArrayList<String>(myDBHandler.getDistinctAreas());
        Collections.sort(listDataHeader, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        Log.d("Nout", "DataHeadders");
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

    private void prepareListData2() {
        listDataHeader.clear();
        listDataChild.clear();
        MyDBHandler myDBHandler = new MyDBHandler(getActivity(),null,null,1);

        listDataHeader.addAll(myDBHandler.getDistinctAreas());
        Log.d("Nout", "DataHeadders");
        for(String s : listDataHeader)
        {
            Log.d("Nout",s);
        }



        for(String area : listDataHeader)
        {
            List<RentItem> group = new ArrayList<RentItem>(myDBHandler.getAreaItems(area)) ;

            listDataChild.put(area,group);

        }


    }



}