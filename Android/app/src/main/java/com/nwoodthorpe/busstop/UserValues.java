package com.nwoodthorpe.busstop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Nathaniel on 3/27/2016.
 */
public class UserValues {
    private static UserValues mInstance= null;



    protected UserValues(){}

    ArrayList<BusRoute> stops;

    public static synchronized UserValues getInstance(){
        if(null == mInstance){
            mInstance = new UserValues();
        }
        return mInstance;
    }
}
