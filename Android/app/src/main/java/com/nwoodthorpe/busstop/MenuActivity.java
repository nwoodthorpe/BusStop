package com.nwoodthorpe.busstop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ArrayList<BusRow> rowArray = new ArrayList<>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String favData = preferences.getString("FAV_DATA", null);

        if(favData != null){
            System.out.println("NOT NULL");
            System.out.println(favData);
            String[] favorites = favData.split("%");
            System.out.println(favorites.length);
            for(int i = 0; i<favorites.length; i++){
                System.out.println("ONE ITERATION");

                String[] splitFavorites = favorites[i].split("-");
                System.out.println(splitFavorites[0] + " " + splitFavorites[1]);
                rowArray.add(new BusRow(splitFavorites[0], splitFavorites[1], splitFavorites[2]));
            }
        }

        ArrayAdapter adapter = new MenuListAdapter(this, 0, rowArray);

        // Link the data and our listview using the adapter.
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        setButtonListeners();
    }
}
