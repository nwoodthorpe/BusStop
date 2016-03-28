package com.nwoodthorpe.busstop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
            editor.putString("FAV_DATA","Home 60-3-60%School 200-7-200%School 7-4-7A");
            editor.apply();

            Intent newIntent = new Intent(SplashActivity.this, MenuActivity.class);
            SplashActivity.this.startActivity(newIntent);
            System.out.println("OPENED MENU");
        }
    }
}
