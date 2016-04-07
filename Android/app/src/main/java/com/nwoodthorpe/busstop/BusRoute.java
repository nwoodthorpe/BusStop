package com.nwoodthorpe.busstop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nathaniel on 3/27/2016.
 */
public class BusRoute {
    String shortID;
    String longID;
    ArrayList<String> stops;
    ArrayList<Integer> stopIDs;

    public BusRoute(JSONObject json) throws JSONException {
        shortID = json.getJSONObject("RouteDirection").getString("PublicIdentifier");
        longID = json.getString("Text");
        stops = new ArrayList<>();
        stopIDs = new ArrayList<>();

        JSONArray stopsArray = json.getJSONArray("Stops");
        for(int i = 0; i<stopsArray.length(); i++){
            String stopString = (String)stopsArray.get(i);
            //System.out.println(stopString);
            stops.add(stopString);
            stopIDs.add(Integer.parseInt(stopString.substring(0, 4))); //Take 4 number stop ID
        }
    }
}
