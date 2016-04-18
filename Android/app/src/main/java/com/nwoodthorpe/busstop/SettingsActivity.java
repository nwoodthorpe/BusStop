package com.nwoodthorpe.busstop;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences preferences;

    public void etaUpdateListener(){


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("How often should we ping the server for an ETA? (seconds)");
        final EditText input = new EditText(this);
        input.setPadding(60, 0, 0, 30);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint(Integer.toString(preferences.getInt("UPDATE_FREQ", 15000)));
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                System.out.println("ENTERED :" + input.getText());
                try{
                    int num = Integer.parseInt(input.getText().toString());
                    if(num > 5 && num<120){
                        SharedPreferences.Editor prefEdit = preferences.edit();
                        prefEdit.putInt("UPDATE_FREQ", num*1000); //convert to ms
                        prefEdit.commit();

                        Toast.makeText(SettingsActivity.this, "Frequency successfully updated!",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SettingsActivity.this, "Frequency should be between 5 and 120 seconds.",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Toast.makeText(SettingsActivity.this, "Error setting new frequency.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Put actions for CANCEL button here, or leave in blank
            }
        });
        alert.show();
    }

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
            ArrayList<FavRoute> favs = Serialization.deserialize(preferences.getString("FAV_DATA", ""));
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

        RelativeLayout etaUpdateLayout = (RelativeLayout) findViewById(R.id.etaupdatelayout);
        etaUpdateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etaUpdateListener();
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
        preferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
        boolean showETA = preferences.getBoolean("showETA", false);

        Switch showETASwitch = (Switch) findViewById(R.id.showETAToggle);
        showETASwitch.setChecked(showETA);

        setButtonListeners();
    }
}
