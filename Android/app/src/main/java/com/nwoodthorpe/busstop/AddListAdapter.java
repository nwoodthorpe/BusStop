package com.nwoodthorpe.busstop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nathaniel on 3/27/2016.
 */
public class AddListAdapter extends ArrayAdapter<BusRoute> {
    private ArrayList<BusRoute> list;
    final int listLayoutXML = R.layout.add_list_item;
    public AddListAdapter(Context context, int textViewResourceId, ArrayList<BusRoute> list) {
        super(context, textViewResourceId, list);
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BusRoute user = list.get(position);
        View v = null;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int layout = listLayoutXML;
            v = vi.inflate(layout, parent, false);
        }

        if (user != null) {
            TextView route = (TextView) v.findViewById(R.id.smallname);
            TextView longName = (TextView) v.findViewById(R.id.longname);

            if (route != null) {
                route.setText(user.shortID);
            }
            if (longName != null) {
                longName.setText(user.longID);
            }
        }
        return v;
    }
}
