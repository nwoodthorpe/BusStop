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

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nathaniel on 12/13/2015.
 */
// Adapter that links our data to the ListView on Transactions Activity
public class MenuListAdapter extends ArrayAdapter<FavRoute> {
    private ArrayList<FavRoute> list;
    final int listLayoutXML = R.layout.menu_list_item;
    public MenuListAdapter(Context context, int textViewResourceId, ArrayList<FavRoute> list) {
        super(context, textViewResourceId, list);
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FavRoute user = list.get(position);
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
                if(user.seconds == -1){
                    time.setText("Retrieving...");
                }else if(user.seconds == -2) {
                    time.setText("Network error retrieving times...");
                }else if(user.seconds == -3) {
                    time.setText("No bus data found!");
                }else{
                    //time.seconds is seconds since midnight
                    Calendar c = Calendar.getInstance();
                    int hours = c.get(Calendar.HOUR_OF_DAY);
                    int minutes = c.get(Calendar.MINUTE);
                    int seconds = c.get(Calendar.SECOND);
                    int mili = c.get(Calendar.MILLISECOND);

                    long curSecondsFromMidnight = seconds + (minutes * 60) + (hours * 3600 );

                    long secondsETA = user.seconds - curSecondsFromMidnight;
                    if(secondsETA < 10){
                        time.setText("Due!");
                    }else if(secondsETA < 60){
                        time.setText(secondsETA + "seconds");
                    }else {
                        time.setText((secondsETA / 60) + " minutes");
                    }
                }
            }

            if(smallName!=null){
                smallName.setText(user.shortRoute);
            }
        }
        return v;
    }
}
