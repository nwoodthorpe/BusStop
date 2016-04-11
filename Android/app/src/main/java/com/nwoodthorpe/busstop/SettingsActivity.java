package com.nwoodthorpe.busstop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

public class SettingsActivity extends AppCompatActivity {

    public void showETAListener(CompoundButton buttonView, boolean isChecked){
        System.out.println("Toggled");
        //Fill later
    }

    public void setButtonListeners(){
        Switch showETA = (Switch) findViewById(R.id.showETAToggle);
        showETA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showETAListener(buttonView, isChecked);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setButtonListeners();
    }
}
