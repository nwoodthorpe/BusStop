package com.nwoodthorpe.busstop;

/**
 * Created by Nathaniel on 3/23/2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nathaniel on 12/13/2015.
 */
// Adapter that links our data to the ListView on Transactions Activity
public class MenuListAdapter extends ArrayAdapter<BusRow> {
    private ArrayList<BusRow> list;
    final int listLayoutXML = R.layout.menu_list_item;
    public MenuListAdapter(Context context, int textViewResourceId, ArrayList<BusRow> list) {
        super(context, textViewResourceId, list);
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BusRow user = list.get(position);
        View v = null;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int layout = listLayoutXML;
            v = vi.inflate(layout, parent, false);
        }

        if (user != null) {
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView time = (TextView) v.findViewById(R.id.time);
            TextView smallName = (TextView)v.findViewById(R.id.smallName);

            if (name != null) {
                name.setText(user.name);
            }
            if (time != null) {
                time.setText(user.time + " minutes");
            }
            if(smallName!=null){
                smallName.setText(user.smallName);
            }
        }
        return v;
    }
}
