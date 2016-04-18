package com.nwoodthorpe.busstop;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.internal.zzf;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Integer> stops;
    private ArrayList<String> longStops;
    private Marker currentSelectedMarker;
    private String route;

    public void onBackClick(View v){
        finish();
    }

    public void onDoneClick(View v){
        if(currentSelectedMarker == null){
            Toast toast = Toast.makeText(getApplicationContext(), "Please select a bus stop.", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            Intent newIntent = new Intent(MapActivity.this, AddFinalDetailsActivity.class);
            newIntent.putExtra("stop", currentSelectedMarker.getTitle());
            newIntent.putExtra("route", route);
            MapActivity.this.startActivity(newIntent);
        }
    }

    public void setButtonListeners(){
        ImageView backButton = (ImageView)findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick(v);
            }
        });

        ImageView doneButton = (ImageView)findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               onDoneClick(v);
            }
        });
    }

    public void setMapMarkerListener(){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                System.out.println(marker.getTitle());
                currentSelectedMarker = marker;
                return false;
            }
        });

        mMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
            @Override
            public void onInfoWindowClose(Marker marker) {
                System.out.println("JUST CLOSED: " + marker.getTitle());
                currentSelectedMarker = null; ///Deselect current selected marker
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        Bundle extras = getIntent().getExtras();
        stops = (ArrayList<Integer>)extras.get("stops");
        longStops = (ArrayList<String>)extras.get("longstops");
        route = (String)extras.get("route");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
                                                                               ;
        setButtonListeners();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setMapMarkerListener();

        HashMap<Integer, LatLng> mappedGeoCoords = UserValues.getInstance().geo;
        LatLng anchor = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0; i<stops.size(); i++){

            int stopNum = stops.get(i);
            String stopDesc = longStops.get(i);
            LatLng coord = mappedGeoCoords.get(stopNum);
            if(coord == null){
                System.err.println("STOP " + stopNum + " NOT FOUND IN COORD LIST");
            }else {
                MarkerOptions coordMarker = new MarkerOptions();
                anchor = coord;
                coordMarker.position(coord);
                coordMarker.title(stopDesc);
                coordMarker.anchor(0.5f, 1f);
                coordMarker.draggable(false);
                mMap.addMarker(coordMarker);
                builder.include(coord);
            }
        }

        if(anchor!=null) {

            LatLngBounds bounds = builder.build();

            int offset = 100;
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels - 60; //Header
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, offset);

            mMap.moveCamera(cu);
        }
    }
}
