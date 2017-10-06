package com.globallogic.workshopble;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Javier Bianciotto on 10/04/2017.
 * GlobalLogic | javier.bianciotto@globallogic.com
 */

public class AdvertiseService extends Service {
    MyAdvertiser advertiser = null;

    public AdvertiseService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (advertiser == null) {
            advertiser = new MyAdvertiser(this);
        }
        advertiser.startAdvertising(this);

        startForeground(7777, createOngoingNotification());

        return START_NOT_STICKY;
    }

    private Notification createOngoingNotification() {
        return new NotificationCompat.Builder(this)
                .setContentTitle("BLEAdvertiser")
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MIN)
                .build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
