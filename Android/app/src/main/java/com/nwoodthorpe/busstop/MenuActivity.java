package com.nwoodthorpe.busstop;

import android.app.AlertDialog;
import android.app.Application;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    ServerSyncService mService;
    boolean mBound = false;

    public void onPlusClick(View v){
        Intent newIntent = new Intent(MenuActivity.this, AddActivity.class);
        MenuActivity.this.startActivity(newIntent);
    }

    public void onSettingsClicked(View v){
        Intent newIntent = new Intent(MenuActivity.this, SettingsActivity.class);
        MenuActivity.this.startActivity(newIntent);
    }

    public void setButtonListeners() {
        ImageView addButton = (ImageView) findViewById(R.id.plus);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlusClick(v);
            }
        });

        ImageView settingsButton = (ImageView) findViewById(R.id.settings);
        settingsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSettingsClicked(v);
                    }
                }
        );
    }



    @Override
    public void onContentChanged() {
        super.onContentChanged();

        View empty = findViewById(R.id.empty);
        ListView list = (ListView) findViewById(R.id.list);
        list.setEmptyView(empty);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent service = new Intent(MenuActivity.this, ServerSyncService.class);
        startService(service);
        ListView listView = (ListView) findViewById(R.id.list);
        ArrayAdapter adapter = (ArrayAdapter)listView.getAdapter();
        if(adapter != null)
            adapter.notifyDataSetChanged();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(SharedPrefInterface.isAllDisabled(this)){
            Intent service = new Intent(MenuActivity.this, ServerSyncService.class);
            stopService(service);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        final ArrayAdapter adapter = new MenuListAdapter(this, 0, SharedPrefInterface.getFavList(this));

        // Link the data and our listview using the adapter.
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("WTF");
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final UserValues prefs = UserValues.getInstance();
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(25);

                final ArrayList<FavRoute> favorites = SharedPrefInterface.getFavList(MenuActivity.this);

                new AlertDialog.Builder(MenuActivity.this)
                        .setTitle("Delete Favorite")
                        .setMessage("Do you want to delete '" + favorites.get(position).name + "'")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                System.out.println("REMOVING ENTRANCE: " + position);
                                int hash = favorites.get(position).name.hashCode();
                                favorites.remove(position);

                                SharedPrefInterface.updateFavList(MenuActivity.this, favorites);

                                NotificationManager mNotificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.cancel(hash);

                                adapter.notifyDataSetChanged();
                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        }).show();

                return false;
            }
        });
        listView.setAdapter(adapter);

        setButtonListeners();

        final Handler h = new Handler();
        final int delay = 2000; //Every 2 seconds
        h.postDelayed(new Runnable(){
            public void run(){
                adapter.notifyDataSetChanged();

                h.postDelayed(this, delay);
            }
        }, delay);
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ServerSyncService.LocalBinder binder = (ServerSyncService.LocalBinder) service;
            mService = binder.getService();
            mService.switchBounds();
            unbindService(this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
