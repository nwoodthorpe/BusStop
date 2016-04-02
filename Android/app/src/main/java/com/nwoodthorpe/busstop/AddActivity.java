package com.nwoodthorpe.busstop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AddActivity extends AppCompatActivity {

    public void onBackClick(View v){
        finish();
    }

    public void setButtonListeners(){
        ImageView backButton = (ImageView)findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackClick(v);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final ArrayList<BusRoute> routes = UserValues.getInstance().stops;

        ArrayAdapter adapter = new AddListAdapter(this, 0, routes);

        // Link the data and our listview using the adapter.
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        setButtonListeners();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent newIntent = new Intent(AddActivity.this, MapActivity.class);
                newIntent.putExtra("stops", routes.get(position).stopIDs);
                newIntent.putExtra("longstops", routes.get(position).stops);
                newIntent.putExtra("route", routes.get(position).longID);
                AddActivity.this.startActivity(newIntent);
            }
        });
    }
}
