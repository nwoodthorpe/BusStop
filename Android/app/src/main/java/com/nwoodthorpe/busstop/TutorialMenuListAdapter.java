package com.nwoodthorpe.busstop;

/**
 * Created by Nathaniel on 3/23/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nathaniel on 12/13/2015.
 */
// Adapter that links our data to the ListView on Transactions Activity
public class TutorialMenuListAdapter extends ArrayAdapter<FavRoute> {
    private ArrayList<FavRoute> list;
    int listLayoutXML = R.layout.menu_list_item;
    public TutorialMenuListAdapter(Context context, int textViewResourceId, ArrayList<FavRoute> list) {
        super(context, textViewResourceId, list);
        this.list = list;

    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FavRoute user = list.get(position);
        View v = null;

        LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layout = listLayoutXML;
        v = vi.inflate(layout, parent, false);


        if (user != null) {
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView time = (TextView) v.findViewById(R.id.time);
            Switch enabled = null;
            TextView smallName = (TextView)v.findViewById(R.id.smallName);

            if (name != null) {
                name.setText(user.name);
            }
            if (time != null) {
                int secondsETA = user.seconds;
                if(secondsETA < 10){
                    time.setText("Due!");
                }else if(secondsETA < 60){
                    time.setText("Less than a minute!");
                }else {
                    time.setText((secondsETA / 60) + " minutes");
                }
            }

            if(enabled != null){
                enabled.setChecked(user.enabled==1);

            }

            if(smallName!=null){
                smallName.setText(user.shortRoute);
            }
        }
        return v;
    }
}
