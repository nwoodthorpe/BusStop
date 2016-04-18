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
        if(input == null || input.length() == 0) {
            ArrayList<FavRoute> x = new ArrayList<>();
            return x;
        }
        String[] splitEntries = input.split("\\+");

        int len = splitEntries.length;
        System.out.println("NUMBER OF ENTRIES: " + len);
        ArrayList<FavRoute> routes = new ArrayList<>();

        for(int i = 0; i<len; i++){
            String[] splitRoute = splitEntries[i].split("\\|");
            if(splitRoute.length != 8 && splitRoute.length != 9) {
                return null;
            }

            String name = splitRoute[0];
            String shortStop = splitRoute[1];
            String longStop = splitRoute[2];
            String shortRoute = splitRoute[3];
            String longRoute = splitRoute[4];
            double lat;
            double lng;
            int enabled;
            int seconds;
            try {
                lat = Double.parseDouble(splitRoute[5]);
                lng = Double.parseDouble(splitRoute[6]);
                enabled = Integer.parseInt(splitRoute[7]);
                if(splitRoute.length == 8)
                    seconds = -1;
                else
                    seconds = Integer.parseInt(splitRoute[8]);
            }catch(Exception e){
                System.out.println("EXCEPTION TRYING TO PARSE!");
                e.printStackTrace();
                return null;
            }

            routes.add(new FavRoute(lat, lng, name, longRoute, shortRoute, longStop, shortStop, enabled, seconds));
        }
        System.out.println("RETURNING NEW ROUTE SIZE: " + routes.size());
        return routes;
    }

    public static String serialize(ArrayList<FavRoute> routes){
        if(routes == null && routes.size() == 0)
            return null;

        int len = routes.size();
        String[] routesStrings = new String[len];

        for(int i = 0; i<len; i++){
            FavRoute route = routes.get(i);
            routesStrings[i] = "" + route.name + "|" + route.shortStop + "|" + route.longStop + "|"
                    + route.shortRoute + "|" + route.longRoute + "|" + route.lat +
                    "|" + route.lng + "|" + route.enabled + "|" + route.seconds;
        }
        return TextUtils.join("+", routesStrings);
    }

    //editor.putString("FAV_DATA","Bus to School|1368|1368-COWAN / WALACE|60|60 - NORTHVIEW ACRES|43.3841743|-80.29449|1000+Bus Home|1123|1123-U|200|200 - iXpress (To Ainslie)|43.47273|-80.54123|1000");

}
