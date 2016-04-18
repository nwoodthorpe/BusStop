package com.nwoodthorpe.busstop;

/**
 * Created by Nathaniel on 4/1/2016.
 */
public class FavRoute {
    double lat;
    double lng;
    String name;
    String longRoute;
    String shortRoute;
    String longStop;
    String shortStop;
    int enabled;
    int seconds;
    //Stores when next bus comes in seconds since midnight.

    public FavRoute(double lat, double lng, String name, String longRoute,
                    String shortRoute, String longStop, String shortStop, int enabled, int seconds){
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.longRoute = longRoute;
        this.shortRoute = shortRoute;
        this.longStop = longStop;
        this.shortStop = shortStop;
        this.enabled = enabled;
        this.seconds = seconds;
    }
}
