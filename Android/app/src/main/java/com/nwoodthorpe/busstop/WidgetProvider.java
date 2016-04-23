package com.nwoodthorpe.busstop;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context)
    {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context)
    {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if("refresh".equals(intent.getAction())) {
            System.out.println("RECIEVED SOMETHING:" + intent.getAction());
            AppWidgetManager appWidgetMgr = AppWidgetManager.getInstance(context);

            Bundle extras = intent.getExtras();
            int[] ids = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);

            System.out.println("SENDING AN UPDATE");
            onUpdate(context, appWidgetMgr, ids);
        }

        super.onReceive(context, intent);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action, int[] ids) {
        Intent intent = new Intent(context, getClass());
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        System.out.println("PUTTING ARRAY OF LEN: " + ids.length);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds)
    {
        for(int i=0;i<appWidgetIds.length;i++)
        {
            System.out.println("CHECK");
            RemoteViews rv = new RemoteViews(context.getPackageName(),
                    R.layout.app_widget);

            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            rv.setRemoteAdapter(R.id.list, intent);

            rv.setOnClickPendingIntent(R.id.refresh, getPendingSelfIntent(context,
                    "refresh", appWidgetIds));

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.list);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}

