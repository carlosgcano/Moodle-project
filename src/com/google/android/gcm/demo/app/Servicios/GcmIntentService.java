/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gcm.demo.app.Servicios;

import gcmserver.HttpRequest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.android.gcm.demo.app.GcmBroadcastReceiver;
import com.google.android.gcm.demo.app.NotificationView;
import com.google.android.gcm.demo.app.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public HttpRequest httprequest = (HttpRequest) new HttpRequest();
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCM Demo";

    @Override
    protected void onHandleIntent(Intent intent) {
    	
    	//recogemos lo que traiga el intent.
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                //sendNotification("Received at "+ new SimpleDateFormat("HH:mm:ss.SSS").format(new Date())+" : " + extras.getString("msg").toString());
            	Log.d("onHandleIntent","GCM recibido...");
            	//Log.d("GCM data", extras.getString("data"));
            	sendNotification(extras.getString("data"), intent);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg, Intent intent) {

        String content = "";

                try {
                    /*content = httprequest.sendGet(
                            "http://usevilla.ingenia.es/apiuse/api.php",
                            "user=alumno&pass=alumno&contexto=idle&tiempo=30");*/
                    content = httprequest.sendGet(
                            "http://usevilla.ingenia.es/apiuse/cursos.php",
                            "user=a1&pass=a1");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
               
        Intent notificationIntent=new Intent(this, NotificationView.class);
		PendingIntent contenIntent= PendingIntent.getActivity(this, 0, notificationIntent, 0);
       		
        Notification mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_stat_gcm)
        .setContentTitle("Moodle Notification")
        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
        .setAutoCancel(true)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setVibrate(new long[] { 200, 500 })
        .setLights(0xffffffff, 300, 1000)
        .setContentText("Tienes actividades pendientes para realizar")
        .setContentIntent(contenIntent).getNotification();

        //mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(0, mBuilder);
    }
}
