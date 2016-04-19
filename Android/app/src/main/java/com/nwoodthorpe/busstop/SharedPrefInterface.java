package com.nwoodthorpe.busstop;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by Nathaniel on 4/18/2016.
 */
public class SharedPrefInterface {

    final static String favKey = "FAV_DATA";
    final static String freqKey = "UPDATE_FREQ";
    final static String firstKey = "FIRSTRUN";

    private static SharedPreferences getPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static ArrayList<FavRoute> getFavList(Context context){
        SharedPreferences preferences = getPreferences(context);
        String favString = preferences.getString(favKey, "");
        return Serialization.deserialize(favString);
    }

    public static void updateFavList(Context context, ArrayList<FavRoute> favorites){
        SharedPreferences preferences = getPreferences(context);
        String favString = Serialization.serialize(favorites);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(favKey, favString);
        editor.apply();
    }

    public static void updateFavTimes(Context context, ArrayList<FavRoute> favorites){
        SharedPreferences preferences = getPreferences(context);
        ArrayList<FavRoute> oldFavs = Serialization.deserialize(preferences.getString(favKey, ""));
        for(int i = 0; i<favorites.size(); i++){
            for(int j = 0; j<oldFavs.size(); j++){
                if(favorites.get(i).name.equals(oldFavs.get(j).name)){
                    oldFavs.get(j).seconds = favorites.get(i).seconds;
                    break;
                }
            }
        }
        String favString = Serialization.serialize(oldFavs);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(favKey, favString);
        editor.apply();
    }

    public static void toggleEnabled(Context context, String name, boolean enabled){
        SharedPreferences preferences = getPreferences(context);
        ArrayList<FavRoute> favs = Serialization.deserialize(preferences.getString(favKey, ""));
        for(int i = 0; i<favs.size(); i++){
            if(favs.get(i).name.equals(name)){
                favs.get(i).enabled = enabled?1:0;
            }
        }
        updateFavList(context, favs);
    }

    public static int getUpdateFrequency(Context context){
        SharedPreferences preferences = getPreferences(context);
        return preferences.getInt(freqKey, 15000);
    }

    public static void setUpdateFrequency(Context context, int frequency){
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(freqKey, frequency);
        editor.apply();
    }

    public static void setIsFirst(Context context, boolean first){
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(firstKey, first);
    }

    public static boolean isFirst(Context context){
        SharedPreferences preferences = getPreferences(context);
        return preferences.getBoolean(firstKey, true);
    }
}
