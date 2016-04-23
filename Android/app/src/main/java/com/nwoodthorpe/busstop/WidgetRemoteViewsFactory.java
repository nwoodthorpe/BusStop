package com.nwoodthorpe.busstop;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Nathaniel on 4/22/2016.
 */
public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context = null;
    private int appWidgetId;

    private ArrayList<FavRoute> favs = new ArrayList<>();

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        System.out.println("CREATED");
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    private void updateWidgetListView() {
        favs = SharedPrefInterface.getFavList(context);
    }

    @Override
    public int getCount() {
        return favs.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d("WidgetCreatingView", "WidgetCreatingView");
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.widget_menu_list_item);

        String time;

        FavRoute user = favs.get(position);

        if (user.seconds == -1) {
            time = "Retrieving...";
        } else if (user.seconds == -2) {
            time = "Network error retrieving times...";
        } else if (user.seconds == -3) {
            time = "No bus data found!";
        } else {
            //time.seconds is seconds since midnight
            Calendar c = Calendar.getInstance();
            int hours = c.get(Calendar.HOUR_OF_DAY);
            int minutes = c.get(Calendar.MINUTE);
            int seconds = c.get(Calendar.SECOND);
            int mili = c.get(Calendar.MILLISECOND);

            long curSecondsFromMidnight = seconds + (minutes * 60) + (hours * 3600);

            long secondsETA = user.seconds - curSecondsFromMidnight;
            if (secondsETA < 10) {
                time = "Due!";
            } else if (secondsETA < 60) {
                time = "Less than a minute!";
            } else {
                time = (secondsETA / 60) + " minutes";
            }
        }

        remoteView.setTextViewText(R.id.name, user.name);
        remoteView.setTextViewText(R.id.smallName, user.shortRoute);
        remoteView.setTextViewText(R.id.time, time);

        return remoteView;
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        updateWidgetListView();
    }

    @Override
    public void onDataSetChanged() {
        // TODO Auto-generated method stub
        updateWidgetListView();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
    }
}
