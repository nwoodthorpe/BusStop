package com.nwoodthorpe.busstop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class AddFinalDetailsActivity extends AppCompatActivity {

    String route;
    String stop;

    //CONSTANTS
    static final int ADD_ALL_GOOD = 0;
    static final int ADD_NO_NAME_OR_RAD = 1;
    static final int ADD_NAME_TOO_LONG = 2;
    static final int ADD_NO_NAME = 3;
    static final int ADD_NO_RAD = 4;
    static final int ADD_INVALID_DISTANCE = 5;
    static final int ADD_NUM_TOO_LARGE = 6;
    static final int ADD_NUM_NEGATIVE = 7;
    static final int ADD_iNVALID_CHAR_IN_NAME = 8;
    static final int ADD_NAME_EXISTS = 9;
    static final int ADD_GEO_ERROR = 10;
    static final int ADD_UNEXPECTED_ERROR = 11;
    static final int ADD_UNEXPECTED_ERROR2 = 12;

    public void onBackClick(View v){
        finish();
    }

    public int validateForm(String name, String notifRadius){
        try {
            if (name == null || notifRadius == null)
                return ADD_NO_NAME_OR_RAD;
            if (name.length() > 20)
                return ADD_NAME_TOO_LONG;

            if (name.length() == 0)
                return ADD_NO_NAME;

            if(notifRadius.length() == 0)
                return ADD_NO_RAD;

            if(name.contains("|") || name.contains("+")){
                return ADD_iNVALID_CHAR_IN_NAME;
            }

            int stopnum = Integer.parseInt(stop.substring(0, 4));
            LatLng stopPos = UserValues.getInstance().geo.get(stopnum);
            if(stopPos == null)
                return ADD_GEO_ERROR;

            int num;

            try {
                num = Integer.parseInt(notifRadius);
            } catch (Exception e) {
                return ADD_INVALID_DISTANCE;
            }

            if (num > 99999)
                return ADD_NUM_TOO_LARGE;

            if (num < 0)
                return ADD_NUM_NEGATIVE;

            UserValues prefs = UserValues.getInstance();
            ArrayList<FavRoute> favs = prefs.favorites;

            for(int i = 0; i<favs.size(); i++){
                if(name.hashCode() == favs.get(i).name.hashCode()){
                    return ADD_NAME_EXISTS;
                }
            }

            return ADD_ALL_GOOD;
        }catch(Exception e){
            return ADD_UNEXPECTED_ERROR;
        }
    }

    public void handleError(int error){
        EditText name = (EditText)findViewById(R.id.nameText);
        EditText notif = (EditText)findViewById(R.id.distanceText);
        EditText stop = (EditText)findViewById(R.id.stopText);
        EditText route = (EditText)findViewById(R.id.routeText);
        assert name != null;
        assert notif != null;
        assert stop != null;
        assert route != null;

        switch (error) {
            case ADD_NO_NAME_OR_RAD:
                //No name or radius was entered

                name.setError("Enter a name!");
                break;

            case ADD_NAME_TOO_LONG:
                //Name too long

                name.setError("Name must be shorter than 20 characters.");
                break;
            case ADD_NO_NAME:
                 //Name wasn't entered
                name.setError("Please enter a name!");
                break;

            case ADD_NO_RAD:
                //notif distance wasn't entered

                notif.setError("Please enter a distance!");
                break;

            case ADD_INVALID_DISTANCE:
                //Invalid number

                notif.setError("Please enter a valid distance!");
                break;

            case ADD_NUM_TOO_LARGE:
                 //Num too large

                 notif.setError("Notification distance must be < 99999m. Set 0 for always active.");
                 break;

            case ADD_NUM_NEGATIVE:
                //Negative number

                notif.setError("A negative distance makes no sense!");
                break;

            case ADD_iNVALID_CHAR_IN_NAME:
                //+ in name

                name.setError("Your name cannot have a '+' or '|' in it!");
                break;

            case ADD_NAME_EXISTS:
                //Name exists

                name.setError("Please choose a unique favorite name!");
                break;

            case ADD_GEO_ERROR:
                //An error ocurred while fetching geo data

                notif.setError("An internal error has occured fetching geographical data for this stop.");
                break;

            case ADD_UNEXPECTED_ERROR:
                //An unexpected error ocurred.

                notif.setError("An unexpected error occured. :(");
                break;

            case ADD_UNEXPECTED_ERROR2:
                //An unexpected error ocurred while adding.

                notif.setError("An unexpected error occured. :/");
        }
    }

    public void onDoneClick(View v) {
        String name = ((EditText) findViewById(R.id.nameText)).getText().toString();
        String notifRadius = ((EditText) findViewById(R.id.distanceText)).getText().toString();
        int error = validateForm(name, notifRadius);
        if (error != ADD_ALL_GOOD) {
            handleError(error);
        } else {
            try {
                UserValues prefs = UserValues.getInstance();

                //Everything's good, we can add to active
                //Create new FavRoute and add it to sharedpreferences
                //Also push update to main menu custom listview adapter
                // public FavRoute(double lat, double lng, String name, String longRoute,
                //String shortRoute, String longStop, String shortStop, int notifRadius){
                String shortStop = stop.substring(0, 4);
                String shortRoute = route.replaceAll("\\D+", ""); //Replace non numerics with numbers

                int stopnum = Integer.parseInt(shortStop);
                LatLng coords = prefs.geo.get(stopnum);

                int distance = Integer.parseInt(notifRadius);
                FavRoute newRoute = new FavRoute(coords.latitude, coords.longitude, name, route, shortRoute, stop, shortStop, distance);

                prefs.favorites.add(newRoute);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("FAV_DATA", Serialization.serialize(prefs.favorites));
                System.out.println("PUTTING NEW DATA: ");
                System.out.println(Serialization.serialize(prefs.favorites));
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } catch (Exception e) {
                handleError(ADD_UNEXPECTED_ERROR2);
            }
        }
    }

    public void setButtonListeners(){
        ImageView backButton = (ImageView)findViewById(R.id.back);
        assert backButton!=null;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick(v);
            }
        });

        ImageView doneButton = (ImageView)findViewById(R.id.done);
        assert doneButton!=null;
        doneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onDoneClick(v);
            }
        });
    }

    private void setFields(){
        EditText routeField = (EditText)findViewById(R.id.routeText);
        assert routeField!=null;
        routeField.setText(route);

        EditText stopField = (EditText)findViewById(R.id.stopText);
        assert stopField!=null;
        stopField.setText(stop);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_final_details);

        Bundle extras = getIntent().getExtras();
        route = (String)extras.get("route");
        stop = (String)extras.get("stop");

        setFields();

        setButtonListeners();
    }
}
