package com.nwoodthorpe.busstop;

import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    protected void onPause() {
        super.onPause();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this);
        if(!preferences.getBoolean("showETA", false)) {
            Intent service = new Intent(MenuActivity.this, ServerSyncService.class);
            stopService(service);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent service = new Intent(MenuActivity.this, ServerSyncService.class);
        startService(service);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this);
        if(!preferences.getBoolean("showETA", false)) {
            Intent service = new Intent(MenuActivity.this, ServerSyncService.class);
            stopService(service);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("STARTING SERVICE:");
        //Intent intent = new Intent(this, ServerSyncService.class);
        //startService(intent);
        //bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        setContentView(R.layout.activity_menu);

        ArrayList<BusRow> rowArray = new ArrayList<>();

        final ArrayAdapter adapter = new MenuListAdapter(this, 0, UserValues.getInstance().favorites);

        // Link the data and our listview using the adapter.
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("SENDING SERVICE REQUEST");
                //Intent intent = new Intent(MenuActivity.this, ServerSyncService.class);
                //bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                System.out.println("LONG TOUGH ON POS: " + position);
                final UserValues prefs = UserValues.getInstance();
                System.out.println(prefs.favorites.get(position).name);
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(25);

                new AlertDialog.Builder(MenuActivity.this)
                        .setTitle("Delete Favorite")
                        .setMessage("Do you want to delete '" + prefs.favorites.get(position).name + "'")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                System.out.println("REMOVING ENTRANCE: " + position);
                                ArrayList<FavRoute> favs = prefs.favorites;
                                favs.remove(position);
                                prefs.favorites = favs;

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("FAV_DATA", Serialization.serialize(prefs.favorites));
                                System.out.println("PUTTING NEW DATA: ");
                                System.out.println(Serialization.serialize(prefs.favorites));
                                editor.apply();

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
        final int delay = 1000; //Every second
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
