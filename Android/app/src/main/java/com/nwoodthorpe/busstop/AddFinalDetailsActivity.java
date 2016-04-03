package com.nwoodthorpe.busstop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class AddFinalDetailsActivity extends AppCompatActivity {

    String route;
    String stop;

    public void onBackClick(View v){
        finish();
    }

    public int validateForm(String name, String notifRadius){
        try {
            if (name == null || notifRadius == null)
                return 1;
            if (name.length() > 20)
                return 2;

            if (name.length() == 0)
                return 3;

            if(notifRadius.length() == 0)
                return 18;

            if(name.contains("|") || name.contains("+")){
                return 20;
            }

            int stopnum = Integer.parseInt(stop.substring(0, 4));
            LatLng stopPos = UserValues.getInstance().geo.get(stopnum);
            if(stopPos == null)
                return 99;

            int num;

            try {
                num = Integer.parseInt(notifRadius);
            } catch (Exception e) {
                return 4;
            }

            if (num > 99999)
                return 5;

            if (num < 0)
                return 6;

            if(num == 0)
                return 7;


            return 0;
        }catch(Exception e){
            return 100;
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
            case 1:
                //No name or radius was entered

                name.setError("Enter a name!");
                break;

            case 2:
                //Name too long

                name.setError("Name must be shorter than 20 characters.");
                break;
            case 3:
                 //Name wasn't entered
                name.setError("Please enter a name!");
                break;

            case 18:
                //notif distance wasn't entered

                notif.setError("Please enter a distance!");
                break;

            case 4:
                //Invalid number

                notif.setError("Please enter a valid distance!");
                break;

            case 5:
                 //Num too large

                 notif.setError("Notification distance must be < 99999m. Set 0 for always active.");
                 break;

            case 6:
                //Negative number

                notif.setError("A negative distance makes no sense!");
                break;

            case 7:
                //0

                notif.setError("Notification distance cannot be 0. Set 0 for always active.");
                break;

            case 20:
                //+ in name

                name.setError("Your name cannot have a '+' or '|' in it!");
                break;

            case 99:
                //An error ocurred while fetching geo data

                notif.setError("An internal error has occured fetching geographical data for this stop.");
                break;

            case 100:
                //An unexpected error ocurred.

                notif.setError("An unexpected error occured. :(");
                break;
        }
    }

    public void onDoneClick(View v){
        String name = ((EditText)findViewById(R.id.nameText)).getText().toString();
        String notifRadius = ((EditText)findViewById(R.id.distanceText)).getText().toString();
        int error = validateForm(name, notifRadius);
        if(error != 0){
            handleError(error);
        }else{
            //Everything's good, we can add to active
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
