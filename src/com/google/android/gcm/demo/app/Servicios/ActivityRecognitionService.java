package com.google.android.gcm.demo.app.Servicios;

import java.util.HashSet;
import java.util.Set;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gcm.demo.app.DemoActivity;
import com.google.android.gcm.demo.app.ServerUtilities;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityRecognitionService extends IntentService{
	
	
		
		private static final String TAG ="ActivityRecognition";
		private static final Set<String> defValues= new HashSet<String>();
		public ActivityRecognitionService() {
			super("ActivityRecognitionService");
		}

		/**
		* Google Play Services calls this once it has analysed the sensor data
		*/
				
		static String lastContext;
		@Override
		protected void onHandleIntent(Intent intent) {
			Log.d(TAG,"onHandleIntent");
			if (ActivityRecognitionResult.hasResult(intent)) {
				ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
				String context = getFriendlyName(result.getMostProbableActivity().getType());
				
				Log.d(TAG, "ActivityRecognitionResult: "+getFriendlyName(result.getMostProbableActivity().getType()));
				Log.d(TAG, result.toString());
				if(lastContext != context){
					lastContext = context;					
					Intent i = new Intent("MOVIL_STATE");
					i.putExtra("Context", getFriendlyName(result.getMostProbableActivity().getType()));
					sendBroadcast(i);
					
					//Enviamos un mensaje a GCM con el estado del movil.
					ServerUtilities.sendXmpp(getApplicationContext(), context);
					
				}
			}else{
				Log.d(TAG, "No hay resultados");
			}
		}

		/**
		* When supplied with the integer representation of the activity returns the activity as friendly string
		* @param type the DetectedActivity.getType()
		* @return a friendly string of the
		*/
		private static String getFriendlyName(int detected_activity_type){
			switch (detected_activity_type ) {
				case DetectedActivity.IN_VEHICLE:
					return "En coche";
				case DetectedActivity.ON_BICYCLE:
					return "En bicicleta";
				case DetectedActivity.ON_FOOT:
					return "Andando";
				case DetectedActivity.TILTING:
					return "Inclinado";
				case DetectedActivity.STILL:
					return "Parado";
				default:
					return "Contexto desconocido";
			}
		}
		public void stop(){
			this.stopSelf();
		}
}

