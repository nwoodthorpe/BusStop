package com.nwoodthorpe.busstop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

    public void onPlusClick(View v){
        Intent newIntent = new Intent(MenuActivity.this, AddActivity.class);
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
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        View empty = findViewById(R.id.empty);
        ListView list = (ListView) findViewById(R.id.list);
        list.setEmptyView(empty);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ArrayList<BusRow> rowArray = new ArrayList<>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String favData = preferences.getString("FAV_DATA", null);

        if(favData != null){
            System.out.println("NOT NULL");
            System.out.println("TESTING");
            System.out.println(favData);
            UserValues.getInstance().favorites = Serialization.deserialize(favData);
        }

        System.out.println("LENGTH: " + UserValues.getInstance().favorites.size());

        final ArrayAdapter adapter = new MenuListAdapter(this, 0, UserValues.getInstance().favorites);

        // Link the data and our listview using the adapter.
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //REFRESH ETA
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
    }
}
