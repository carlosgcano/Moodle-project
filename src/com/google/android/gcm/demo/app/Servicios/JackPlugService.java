package com.google.android.gcm.demo.app.Servicios;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class JackPlugService extends Service{
	@Override
	public void onTaskRemoved(Intent rootIntent) {
	    Log.e("FLAGX : ", ServiceInfo.FLAG_STOP_WITH_TASK + "");
	    Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
	    restartServiceIntent.setPackage(getPackageName());

	    PendingIntent restartServicePendingIntent = PendingIntent.getService(
	    		getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
	    AlarmManager alarmService = (AlarmManager) getApplicationContext().
	            getSystemService(Context.ALARM_SERVICE);
	    alarmService.set(AlarmManager.ELAPSED_REALTIME,
	            SystemClock.elapsedRealtime() + 1000,
	            restartServicePendingIntent);

	    super.onTaskRemoved(rootIntent);
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("JackPlugService", "service start");
		IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
		registerReceiver(new JackPlugBroadcastReceiver(), filter);
		
		return START_STICKY;
	}
	private class JackPlugBroadcastReceiver extends WakefulBroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			Log.d("JackPlugBroadcastReceiver","Cascos "+
					(intent.getExtras().getInt("state")==1? "enchufados":"desenchufados"));
			//state > 0 implica que hay unos cascos enchufados en el movil.
			if(intent.getIntExtra("state", 0) > 0)
			{
//				ServerUtilities.sendXmpp(context, intent);
			}
			
		}
	}
}