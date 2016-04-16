package com.nwoodthorpe.busstop;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    public void showETAListener(CompoundButton buttonView, boolean isChecked){
        System.out.println("Toggled");

        //Set toggle state in application preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putBoolean("showETA", isChecked);
        prefEdit.apply();

        Intent intent = new Intent(this, ServerSyncService.class);
        if(isChecked){
            startService(intent);
        }else{
            stopService(intent);

            //Also need to delete the notifications.
            ArrayList<FavRoute> favs = UserValues.getInstance().favorites;
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            for(int i = 0; i<favs.size(); i++){
                mNotificationManager.cancel(favs.get(i).name.hashCode());
            }
        }
    }

    public void onBackClicked(){
        finish();
    }

    public void setButtonListeners(){
        Switch showETA = (Switch) findViewById(R.id.showETAToggle);
        showETA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showETAListener(buttonView, isChecked);
            }
        });

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClicked();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Set switch state to current setting determined by application preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        boolean showETA = preferences.getBoolean("showETA", false);

        Switch showETASwitch = (Switch) findViewById(R.id.showETAToggle);
        showETASwitch.setChecked(showETA);

        setButtonListeners();
    }
}
