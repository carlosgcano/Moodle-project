package com.google.android.gcm.demo.app;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;




 
public class ServerUtilities {

		private static final String SERVER_URL = "http://garmo.cica.es/gcm/";
	    private static final String TAG = "ServerUtilities";
	    
	   /**
	     * Register this account/device pair within the server.
	     *
	     */
	    static void register(String UDID, String regId) {
	        Log.i(TAG, "registering device (regId = " + regId + ")");
	        String serverUrl = SERVER_URL + "register.php";
	        Map<String, String> params = new HashMap<String, String>();
	        params.put("regID", regId);
	        params.put("UDID", UDID);
 
            try {
                post(serverUrl, params);
                return;
            } catch (IOException e) {
                Log.e(TAG, "Failed to register:" + e);
            }
        }
	 
	    /**
	     * Unregister this account/device pair within the server.
	     */
	    static void unregister(String regId) {
	        Log.i(TAG, "unregistering device (regId = " + regId + ")");
	        String serverUrl = SERVER_URL + "/unregister";
	        Map<String, String> params = new HashMap<String, String>();
	        params.put("regId", regId);
	        try {
	            post(serverUrl, params);
	        } catch (IOException e) {
	        	Log.e(TAG, "Failed to unregister:" + e);
	        }
	    }
	 
	    static void get(String UDID, String context)
	    {
	        Log.i(TAG, "Getting message (UDID = " + UDID + ")");
	        String serverUrl = SERVER_URL + "get_message.php";
	        Map<String, String> params = new HashMap<String, String>();
	        params.put("UDID", UDID);
	        params.put("context", context);
	        try {
	            post(serverUrl, params);
	        } catch (IOException e) {
	        	Log.e(TAG, "Failed to send message:" + e);
	        }
	    }
	    /**
	     * Issue a POST request to the server.
	     *
	     * @param endpoint POST address.
	     * @param params request parameters.
	     *
	     * @throws IOException propagated from POST.
	     */
	    private static void post(String endpoint, Map<String, String> params)
	            throws IOException {    
	         
	        URL url;
	        try {
	            url = new URL(endpoint);
	        } catch (MalformedURLException e) {
	            throw new IllegalArgumentException("invalid url: " + endpoint);
	        }
	        StringBuilder bodyBuilder = new StringBuilder();
	        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
	        // constructs the POST body using the parameters
	        while (iterator.hasNext()) {
	            Entry<String, String> param = iterator.next();
	            bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
	            if (iterator.hasNext()) {
	                bodyBuilder.append('&');
	            }
	        }
	        String body = bodyBuilder.toString();
	        Log.v(TAG, "Posting '" + body + "' to " + url);
	        byte[] bytes = body.getBytes();
	        HttpURLConnection conn = null;
	        try {
	            Log.v("URL", "> " + url);
	            conn = (HttpURLConnection) url.openConnection();
	            conn.setDoOutput(true);
	            conn.setUseCaches(false);
	            conn.setFixedLengthStreamingMode(bytes.length);
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Content-Type",
	                    "application/x-www-form-urlencoded;charset=UTF-8");
	            // post the request
	            OutputStream out = conn.getOutputStream();
	            out.write(bytes);
	            out.close();
	            // handle the response
	            int status = conn.getResponseCode();
	            if (status != 200) {
	              throw new IOException("Post failed with error code " + status);
	            }
	        } finally {
	            if (conn != null) {
	                conn.disconnect();
	            }
	        }
	      }
	    
		public static void sendXmpp(final Context context, final String contexto)
		{
			
			
			
			
			//creamos una una AsyncTask para enviar un gcm al servidor
			new AsyncTask<Void, Void, String>() 
			{
				GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
	            @Override
	            protected String doInBackground(Void... params) 
	            {
	                try 
	                {
	                    Bundle data = new Bundle();
	                    data.putString("user", "alumno");
	                    data.putString("pass", "alumno");
	                    data.putString("contexto", contexto);
	                    data.putString("tiempo", "15");
	                    
	                    String id = Integer.toString(DemoActivity.msgId.incrementAndGet());
	                    gcm.send(DemoActivity.SENDER_ID + "@gcm.googleapis.com", id, data);
	                    Log.d("sendXmpp", "Enviando GCM...");
	                } 
	                catch (IOException ex) 
	                {
	                }
	                return "";
	            }
	        }.execute(null, null, null);
	        
			
		}
		
}
