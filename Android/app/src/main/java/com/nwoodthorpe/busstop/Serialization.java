package com.nwoodthorpe.busstop;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by Nathaniel on 4/1/2016.
 */
public class Serialization {
    //Class provides methods to serialize between swting format
    //of favorite data and object format.

    //SERIALIZATION FORMAT:
    /*
    NAME|SHORTSTOP|LONGSTOP|SHORTROUTE|LONGROUTE|LAT|LNG|DISTANCE+NAME|SHORTSTOP|...
    E.g.
    Bus Home|1368|1368 - COWAN / WALACE|60|60 - NORTHVIEW ACRES|52.1222222|14.2221+SOMETHING ELSE|...
     */

    //Returns null on error.
    public static ArrayList<FavRoute> deserialize(String input){
        if(input == null || input.length() == 0)
            return null;
        String[] splitEntries = input.split("\\+");

        int len = splitEntries.length;
        ArrayList<FavRoute> routes = new ArrayList<>();

        for(int i = 0; i<len; i++){
            String[] splitRoute = splitEntries[i].split("\\|");
            if(splitRoute.length != 8) {
                System.out.println("THE GOD DAMN LENGTH IS: " + splitRoute.length);
                return null;
            }

            String name = splitRoute[0];
            String shortStop = splitRoute[1];
            String longStop = splitRoute[2];
            String shortRoute = splitRoute[3];
            String longRoute = splitRoute[4];
            double lat;
            double lng;
            int distance;
            try {
                lat = Double.parseDouble(splitRoute[5]);
                lng = Double.parseDouble(splitRoute[6]);
                distance = Integer.parseInt(splitRoute[7]);
            }catch(Exception e){
                return null;
            }

            routes.add(new FavRoute(lat, lng, name, longRoute, shortRoute, longStop, shortStop, distance));
        }
        return routes;
    }

    public static String serialize(ArrayList<FavRoute> routes){
        if(routes == null && routes.size() == 0)
            return null;

        int len = routes.size();
        String[] routesStrings = new String[len];

        for(int i = 0; i<len; i++){
            FavRoute route = routes.get(i);
            routesStrings[i] = "" + route.lat + "|" + route.lng + "|" + route.name + "|"
                    + route.longRoute + "|" + route.shortRoute + "|" + route.longStop +
                    "|" + route.shortStop + "|" + route.notifRadius;
        }
        return TextUtils.join("+", routesStrings);
    }
}
