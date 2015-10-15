package com.experiment.comp.application_house_rent_bd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.experiment.comp.application_house_rent_bd.R;
import com.experiment.comp.application_house_rent_bd.RentItem;

import java.util.ArrayList;

/**
 * Created by comp on 7/3/2015.
 */
public class HouseListAdapter extends ArrayAdapter<RentItem> {
    public HouseListAdapter(Context context, ArrayList<RentItem> resource) {
        super(context, 0,resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        RentItem rentItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rent_list_item, parent, false);
        }
        // Lookup view for data population
        //ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        TextView textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
        TextView textViewArea = (TextView) convertView.findViewById(R.id.textViewArea);

        textViewTitle.setText(rentItem.getRentTitle());
        textViewArea.setText(rentItem.getRentArea());

        return convertView;


    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
