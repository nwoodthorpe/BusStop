package com.nwoodthorpe.busstop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class SplashActivity extends Activity {

    public String loadJSON(String file) {
        String json = null;
        try {
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ArrayList<BusRoute> stops = new ArrayList<>();
        HashMap<Integer, LatLng> map = new HashMap<Integer, LatLng>();

        try {
            JSONObject stopObject = new JSONObject(loadJSON("stops.json"));
            JSONArray stopArray = stopObject.getJSONArray("data");
            //Iterate over JSON array
            for(int i = 0; i<stopArray.length(); i++){
                JSONObject localobj = (JSONObject)stopArray.get(i);
                if(localobj.has("Stops"))
                    stops.add(new BusRoute(localobj));
            }

            JSONObject geoObject = new JSONObject(loadJSON("latlong.json"));
            JSONArray geoArray = geoObject.getJSONArray("data");

            for(int i = 0; i<geoArray.length(); i++){
                JSONObject localobj = (JSONObject)geoArray.get(i);
                map.put((Integer)localobj.get("stop"), new LatLng((Double)localobj.get("lat"), (Double)localobj.get("long")));
                System.out.println("PUTTING COORD " + i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        UserValues.getInstance().stops = stops;
        UserValues.getInstance().geo = map;



        //Check if app has been run before
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean first = preferences.getBoolean("FIRSTRUN", true);

        if(first){
            System.out.println("FIRST");
            //It's our first time running
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("FIRSTRUN",false);
            editor.apply();

            System.out.println("FIRST RUN!");
            Intent newIntent = new Intent(SplashActivity.this, IntroActivity.class);
            SplashActivity.this.startActivity(newIntent);
            System.out.println("OPENED INTRO");
        }else{
            System.out.println("NOT FIRST");
            SharedPreferences.Editor editor = preferences.edit();
            //Bus Home|1368|1368 - COWAN / WALACE|60|60 - NORTHVIEW ACRES|52.1222222|14.2221
            editor.putString("FAV_DATA","Bus to School|1368|1368-COWAN / WALACE|60|60 - NORTHVIEW ACRES|43.3841743|-80.29449|1000+Bus Home|1123|1123-U|200|200 - iXpress (To Ainslie)|43.47273|-80.54123|1000");
            editor.apply();

            Intent newIntent = new Intent(SplashActivity.this, MenuActivity.class);
            SplashActivity.this.startActivity(newIntent);
            System.out.println("OPENED MENU");
        }
    }
}
