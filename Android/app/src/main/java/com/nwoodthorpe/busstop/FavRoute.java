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
    int notifRadius;

    public FavRoute(double lat, double lng, String name, String longRoute,
                    String shortRoute, String longStop, String shortStop, int notifRadius){
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.longRoute = longRoute;
        this.shortRoute = shortRoute;
        this.longStop = longStop;
        this.shortStop = shortStop;
        this.notifRadius = notifRadius;
    }
}
