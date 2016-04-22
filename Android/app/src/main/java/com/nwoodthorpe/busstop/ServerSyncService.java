package com.nwoodthorpe.busstop;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class ServerSyncService extends Service {
    SparseArray<NotificationCompat.Builder> notificationBuilders = new SparseArray<>();

    int bound = 50;
    private boolean isRunning = false;
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    final Handler networkHandler = new Handler();

    public class LocalBinder extends Binder {

        ServerSyncService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServerSyncService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        //Handle disabling on notifcation button press
        Bundle extras = intent.getExtras();
        String disable = extras==null?null:extras.getString("DISABLE");
        if(disable != null && !disable.equals("")){
            ArrayList<FavRoute> favorites = SharedPrefInterface.getFavList(this);
            for(int i = 0; i<favorites.size(); i++){
                if(favorites.get(i).name.equals(disable)){
                    //Disable this notifcation and set it to disabled.
                    SharedPrefInterface.toggleEnabled(this, disable, false);

                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.cancel(disable.hashCode());
                }
            }
        }

        updateNotifications();

        getNewTimes(true);

        if(!isRunning) {
            //System.out.println("CHECK");
            isRunning = true;

            getNewTimes(false);

            final Handler h = new Handler();
            final int delay = 15000; //milliseconds, 30 seconds
            h.postDelayed(new Runnable() {
                public void run() {
                    updateNotifications();
                    if(isRunning)
                        h.postDelayed(this, delay);
                }
            }, 3000);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //System.out.println("AGH WE'VE BEEN DESTROYED NOOOOOOOOOO");
        isRunning = false;
        super.onDestroy();
    }

    public void switchBounds(){
        if(bound==50)
            bound = 1000;
        else
            bound=50;
    }

    public void updateNotifications(){
        //System.out.println("UPDATING NOTIFICATIONS");

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        ArrayList<FavRoute> favs = SharedPrefInterface.getFavList(this);

        for(int i = 0; i<favs.size(); i++){

            String time = "";

            FavRoute user = favs.get(i);
            if(user.enabled == 0)
                mNotificationManager.cancel(user.name.hashCode());
            else {

                if (user.seconds == -1) {
                    time = "Retrieving...";
                } else if (user.seconds == -2) {
                    time = "Network error retrieving times...";
                } else if (user.seconds == -3) {
                    time = "No bus data found!";
                } else {
                    //time.seconds is seconds since midnight
                    Calendar c = Calendar.getInstance();
                    int hours = c.get(Calendar.HOUR_OF_DAY);
                    int minutes = c.get(Calendar.MINUTE);
                    int seconds = c.get(Calendar.SECOND);
                    int mili = c.get(Calendar.MILLISECOND);

                    long curSecondsFromMidnight = seconds + (minutes * 60) + (hours * 3600);

                    long secondsETA = user.seconds - curSecondsFromMidnight;
                    if (secondsETA < 10) {
                        time = "Due!";
                    } else if (secondsETA < 60) {
                        time = "Less than a minute!";
                    } else {
                        time = (secondsETA / 60) + " minutes";
                    }
                }

                NotificationCompat.Builder mBuilder;

                Intent intent = new Intent(ServerSyncService.this, ServerSyncService.class);
                intent.putExtra("DISABLE", favs.get(i).name);
                PendingIntent cancelIntent  = PendingIntent.getService(this, favs.get(i).name.hashCode(), intent, 0);

                if (notificationBuilders.indexOfKey(user.name.hashCode()) < 0) {
                    mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.bus)
                            .setContentTitle(favs.get(i).shortRoute + " - " + favs.get(i).name)
                            .setContentText(time)
                            .setVisibility(Notification.VISIBILITY_PUBLIC)
                            .setDeleteIntent(cancelIntent)
                            .setOnlyAlertOnce(true);
                    notificationBuilders.append(user.name.hashCode(), mBuilder);
                } else {
                    mBuilder = notificationBuilders.get(user.name.hashCode());
                    mBuilder.setContentText(time);
                }


                Intent notificationIntent = new Intent(ServerSyncService.this, SplashActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(ServerSyncService.this, 0,
                        notificationIntent, 0);

                mBuilder.setContentIntent(pendingIntent);

// mId allows you to update the notification later on.
                mNotificationManager.notify(favs.get(i).name.hashCode(), mBuilder.build());
            }
        }
        //Hash names to int, use int for notification
        //Also remove any notifications that aren't nearby.

    }

    /** method for clients */
    public void getNewTimes(final boolean justOnce) {
        //System.out.println("BEGINNING GENERATION CYCLE");
        final int delay = SharedPrefInterface.getUpdateFrequency(this);
        Runnable r = new Runnable(){
            public void run(){
                //Kill if we aren't supposed to run anymore
                if(!isRunning)
                    return;

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if(networkInfo == null || !networkInfo.isConnected()) {
                    updateNotifications();
                    networkHandler.postDelayed(this, delay);
                    return;
                }

                AsyncServerCalls async = new AsyncServerCalls();
                async.handler = networkHandler;
                async.self = this;
                async.delay = delay;
                async.justOnce = justOnce;

                async.execute();
            }
        };

        networkHandler.postDelayed(r, 0);
    }

    private class AsyncServerCalls extends AsyncTask<String, String, String> {
        ArrayList<FavRoute> favorites;
        Handler handler;
        Runnable self;
        boolean justOnce;
        int delay;

        public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            return total.toString();
        }

        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000 /* milliseconds */);
                conn.setConnectTimeout(5000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                //System.out.println("The response is: " + response);
                if(response != 200) return ""; //SET TO HANDLE API RESPONSE CODES
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is);
                return contentAsString;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            favorites = SharedPrefInterface.getFavList(ServerSyncService.this);
            for(int i = 0; i<favorites.size(); i++){
                boolean set = false;
                try{

                    FavRoute favorite = favorites.get(i);
                    String URL = "http://nwoodthorpe.com/grt/V2/livetime.php?stop=" + favorite.shortStop;
                    //System.out.println("URL: " + URL);
                    String response = downloadUrl(URL);
                    JSONObject json = new JSONObject(response);
                    JSONArray dataArray = json.getJSONArray("data");

                    for(int j = 0; j<dataArray.length(); j++){
                        JSONObject inner = (JSONObject) dataArray.get(j);
                        if(inner.getString("routeId").equals(favorite.shortRoute)){
                            if(favorite.shortRoute.equals("7")){
                                //Gotta do an extra check because of crazy 7 stuff
                                //Eugghhhh this is ugly, its nicer than having it all cramped into one if statemnent
                                //But I need to refactor this soon.
                                if(favorite.longRoute.contains("owntown") && !inner.getString("name").contains("7 Downtown")){
                                    continue;
                                }

                                if(favorite.longRoute.contains("7A") && !inner.getString("name").contains("7A")){
                                    continue;
                                }

                                if(favorite.longRoute.contains("7B") && !inner.getString("name").contains("7B")){
                                    continue;
                                }

                                if(favorite.longRoute.contains("7C") && !inner.getString("name").contains("7C")){
                                    continue;
                                }

                                if(favorite.longRoute.contains("7D") && !inner.getString("name").contains("7D")){
                                    continue;
                                }

                                if(favorite.longRoute.contains("7E") && !inner.getString("name").contains("7E")){
                                    continue;
                                }

                                if(favorite.longRoute.contains("7F") && !inner.getString("name").contains("7F")){
                                    continue;
                                }
                            }
                            //Found the one we're looking for!
                            int time = Integer.parseInt(inner.getString("departure"));

                            //NOTE: 'time' is in seconds since midnight
                            set = true;
                            favorites.get(i).seconds = time;
                        }
                    }
                }catch(IOException e){
                    //Network error
                    //favorites.get(i).seconds = -2;
                } catch (JSONException e) {
                    //JSON parsing error, bus is likely not currently running
                    e.printStackTrace();
                    favorites.get(i).seconds = -3;
                }
                if(!set)
                    favorites.get(i).seconds = -3;
            }

            //System.out.println("JUST FINISHED NETWORK CYCLE");
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            SharedPrefInterface.updateFavTimes(ServerSyncService.this, favorites);

            updateNotifications();
            if(!justOnce)
                handler.postDelayed(self, SharedPrefInterface.getUpdateFrequency(ServerSyncService.this));
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}