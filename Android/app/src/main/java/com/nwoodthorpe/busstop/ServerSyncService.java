package com.nwoodthorpe.busstop;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

public class ServerSyncService extends Service {
    int bound = 50;
    int iterations = 0;
    private boolean isRunning = false;
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */

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
        System.out.println("STARTED");
        if(!isRunning){
            System.out.println("CHECK");
            isRunning = true;
            Intent notificationIntent = new Intent(this, SplashActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(this, 0,
                    notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT);

            Notification notification=new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.cog)
                    .setContentText("TESTING")
                    .setContentIntent(pendingIntent).build();

            startForeground(2234, notification);
            getRandomNumber();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        System.out.println("AGH WE'VE BEEN DESTROYED NOOOOOOOOOO");
        super.onDestroy();
    }

    public void switchBounds(){
        if(bound==50)
            bound = 1000;
        else
            bound=50;
    }

    public int getIterations(){
        return iterations;
    }

    /** method for clients */
    public void getRandomNumber() {
        System.out.println("BEGINNING GENERATION CYCLE");
        final Handler h = new Handler();
        final int delay = 1000; //milliseconds

        h.postDelayed(new Runnable(){
            public void run(){
                //do something
                int r = mGenerator.nextInt(bound);
                System.out.println(r);
                iterations++;

                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                Intent notificationIntent = new Intent(ServerSyncService.this, SplashActivity.class);
                PendingIntent pendingIntent=PendingIntent.getActivity(ServerSyncService.this, 0,
                        notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT);

                Notification notification=new NotificationCompat.Builder(ServerSyncService.this)
                        .setSmallIcon(R.drawable.cog)
                        .setContentText(Integer.toString(r))
                        .setContentIntent(pendingIntent).build();

                mNotificationManager.notify(2234, notification);

                h.postDelayed(this, delay);

            }
        }, delay);
    }
}
