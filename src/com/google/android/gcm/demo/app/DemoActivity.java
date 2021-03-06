/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gcm.demo.app;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.MailTo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gcm.demo.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import gcmserver.*;
//import com.google.android.gcm.demo.app.Servicios.JackPlugService;

/**
 * Main UI for the demo app.
 */
public class DemoActivity extends Activity{
			
			
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static String UDID = "";
    public static final String SENDER_ID = "272622375614"; /** GCM */
    static final String TAG = "GCM Demo";
    public static final AtomicInteger msgId = new AtomicInteger();
    
    private static DemoActivity instance;
    private static SharedPreferences prefs;
    EditText mEditText;
    TextView mContext;
    TextView mDisplay;
    ImageView mStatus;
    Button mSimIdle, mSimOn_foot; 
    GoogleCloudMessaging gcm;
    Context context;
    String regid;
    ComponentName service;
    //JackPlugBroadcastReceiver receiver = new JackPlugBroadcastReceiver();
    
    AsyncTask<Void, Void, String> a;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        setContentView(R.layout.main2);
        UDID = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
//        mDisplay = (TextView) findViewById(R.id.display);
        mContext = (TextView) findViewById(R.id.context);
        
//        mEditText = (EditText) findViewById(R.id.editText);
        mStatus = (ImageView) findViewById(R.id.imageStatus);
        mSimIdle = (Button) findViewById(R.id.simIdle);
        mSimOn_foot =(Button) findViewById(R.id.SimOn_foot);
        context = getApplicationContext();
        
        
        
        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
        	// Get Instance of GCM
        	Log.d("GooglePlayService", "true");
            gcm = GoogleCloudMessaging.getInstance(context);
            // Check if the device have a regid if not need to register
            regid = getRegistrationId(context);
            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
        
//        service = startService(new Intent(context, JackPlugService.class));
        
//        activamos el reconocimiento de actividades.
        new ActivityRecognitionScan(this).startActivityRecognitionScan();
        
//		  Registramos el BroadCastReciver "ContextReceiver" y el intent que transporta la informacion.        
        registerReceiver(ContextReceiver, new IntentFilter("MOVIL_STATE"));
        
        
        
        
//        boton de testeo Idle
        mSimIdle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/**editor.putInt("contexto", 1);
				editor.commit();**/
				ServerUtilities.sendXmpp(context, "idle");
			}
		});
        
//      boton de testeo Walk
        mSimOn_foot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/**editor.putInt("contexto", 2);
				editor.commit();**/
				
				//ServerUtilities.sendXmpp(context, "idle");
				//ServerUtilities.Interrupt();

			}
		});
        
        instance=this;
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }

    @Override
    //Cuando se cierra la aplicacion o desaparece de memoria, hay que parar todos los hilos en ejecucion.
    //Ondestroy es un metodo que funciona justo antes de quitar de memoria la aplicacion
    //Si ponemos a null un thread nos aseguramos que ese hilo se cerrara una vez terminada la aplicacion
    //otras formas de hacerlo es con getthread().interrupt() o llamando al metodo notify del asynctask y haciendo
    //interrupt en el hijo
    protected void onDestroy() {
    	super.onDestroy(); 
    	//cancelamos el hilo que hace la peticion del reconocimiento de actividad
    	ServerUtilities.Interruption();
    	//cancelamos el hilo que llama a Server utilities
    	a.cancel(true);
    	//finalizamos el hilo principal
    	DemoActivity.this.finish();

    	
    	
    }

    public static SharedPreferences getPrefs() {
		synchronized (DemoActivity.class) {			
			return prefs;				
		}
    	
	}
    
	/**
     *  Broadcast para actualizar el contexto desde el servicio
     * 
     * */
    private BroadcastReceiver ContextReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			mContext.setText(intent.getExtras().getString("Context"));
			
			/**try {

			     JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
			     Iterator itr = json.keys();
			      while (itr.hasNext()) {
			        String key = (String) itr.next();
			        Log.d(TAG, "..." + key + " => " + json.getString(key));
			      }
			   
			    } catch (JSONException e) {
			      Log.d(TAG, "JSONException: " + e.getMessage());
			     
			     
			    }**/
			
		}
		
	};
    
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(DemoActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
    	
    	ServerUtilities.register(UDID, regid);
    }
    
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
        Log.d("GooglePlayServices", resultCode+" "+ConnectionResult.SUCCESS);
        	if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            mStatus.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_offline));
            return false;
        }
        mStatus.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_online));
        return true;
    }
    
    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
       a= new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    //  For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            	
            	if(mDisplay!=null){
            		mDisplay.append(msg + "\n");
            	}
                	
            }
        }.execute(null, null, null);
    }
    
    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    
    public static DemoActivity getInstance() {
		return instance;
	}
    
    
    //Vincula las 2 actividades: preferencias y principal
    public void preferences(View view){
        Intent i = new Intent(this, Preferences.class);
              startActivity(i);
      }

}
