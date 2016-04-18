package com.nwoodthorpe.busstop;

import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nathaniel on 3/27/2016.
 */
public class UserValues {
    private static UserValues mInstance= null;

    protected UserValues(){}

    ArrayList<BusRoute> stops;
    HashMap<Integer, LatLng> geo;

    //CONSTANTS
    final int ADD_RESULT_KEY = 2;

    public static synchronized UserValues getInstance(){
        if(null == mInstance){
            mInstance = new UserValues();
        }
        return mInstance;
    }
}