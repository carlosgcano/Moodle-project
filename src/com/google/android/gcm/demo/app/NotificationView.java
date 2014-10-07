package com.google.android.gcm.demo.app;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gcmserver.HttpRequest;
import android.app.Activity;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NotificationView extends Activity {
	
	private ProgressDialog pDialog;
	private Button btnBack;
	ArrayList<HashMap<String, String>> contentList;
	String contenido;
	private ArrayAdapter<String> listAdapter ; 
	ArrayList<String> contextos = new ArrayList<String>();
	private ListView list ; 
	String url;
	
	//SharedPreferences prefs = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//vinculamos este activity con su vista xml
		setContentView(R.layout.notificationview);
		
		//btnBack cierra esta activity y vuelve ala pantalla principal
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
		//Cuando pulsamos se inicia la funcion finish, la cual cierra la activity 	
			@Override
			public void onClick(View v) {
				finish();

			}
		});
		list = (ListView) findViewById(R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override 
			public void onItemClick(AdapterView<?> adapt, View view, int position, long id) { 
				// TODO Auto-generated method stub

				TextView Url = (TextView) view.findViewById(R.id.urlContexto);
				Intent i = new Intent(NotificationView.this, WebActivity.class);
				System.out.println(Url.getText());
				i.putExtra("url", Url.getText());
				startActivity(i);
				 
			} }); 
		
		
		
		contentList = new ArrayList<HashMap<String, String>>();
		
		
		//ejecuta llama a la clase privada para hacer la llamada al servidor en segundo plano
		Ejecuta ejecuta = new Ejecuta();
		ejecuta.execute();

	}
	
	
	
private class Ejecuta extends AsyncTask<Void, Void, Void> {
	
	
	int i=-1;
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// Showing progress dialog
		pDialog = new ProgressDialog(NotificationView.this);
		pDialog.setMessage("Por favor, espera...");
		pDialog.setCancelable(false);
		pDialog.show();

	}
	
	//doInBackground crea un hilo secundario
	protected Void doInBackground(Void... arg0) {
			String nombrecontexto="";
			String tipocontexto="";
			String idcontexto="";
			String urlrecurso="";
			/**if(prefs.getInt("contexto", i)==1){
				url="http://usevilla.ingenia.es/apiuse/api.php?user=alumno&pass=alumno&contexto=idle&tiempo=30";
			}else if (prefs.getInt("contexto", i)==2){
				url="http://usevilla.ingenia.es/apiuse/api.php?user=alumno&pass=alumno&contexto=on_foot&tiempo=30";
			}**/
			
			try {
				InputStream is = new URL("http://usevilla.ingenia.es/apiuse/api.php?user=alumno&pass=alumno&contexto=idle&tiempo=30").openStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is,Charset.forName("UTF-8")));
				StringBuilder sb = new StringBuilder();
				int cp;
				while ((cp = rd.read()) != -1) {
					sb.append((char) cp);
				}
	
				contenido = sb.toString();
				JSONArray jsonarray=new JSONArray(contenido);
				
				for (int i = 0; i < jsonarray.length(); i++) {
					//System.out.println("Este es el json:"+i+jsonarray.getString(i));
					JSONObject jsonobj=new JSONObject(jsonarray.getString(i));
					
					nombrecontexto=jsonobj.getString("nombrecontexto");		
					tipocontexto=jsonobj.getString("tipocontexto");
					idcontexto=jsonobj.getString("idcontexto");
					urlrecurso=jsonobj.getString("urlrecurso");
					
					
					System.out.println("Este es el nombre del recurso "+i+": "+nombrecontexto);
					System.out.println("Este es el tipo del recurso "+i+": "+tipocontexto);
					System.out.println("Este es el id del recurso "+i+": "+idcontexto);
					System.out.println("Este es la url del recurso "+i+": "+urlrecurso);
					
					//Hashmap auxiliar
					HashMap<String, String> aux = new HashMap<String, String>();
					
					// adding each child node to HashMap key => value
					aux.put("nombrecontexto", nombrecontexto);
					aux.put("tipocontexto", tipocontexto);
					aux.put("idcontexto", idcontexto);
					aux.put("urlrecurso", urlrecurso);
					
					contentList.add(aux);
					
				}
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			return null;
		}
		
		//una vez echa la llamada al servidor vincula el resultado dado por el servidor con la
		//interfaz del activity, dentro de ella, lo vincula concretamente con el textview
	protected void onPostExecute(Void result) {
		
		if (pDialog.isShowing()){
			pDialog.dismiss();
		}
		
		
		/**
		 * Updating parsed JSON data into ListView
		 * */
		ListAdapter adapter = new SimpleAdapter(
				NotificationView.this, contentList,
				R.layout.list_item, new String[] { "nombrecontexto", "tipocontexto","urlrecurso" }, new int[] { R.id.nombreContexto,
						R.id.tipoContexto, R.id.urlContexto });

		ListView list= (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		
		
		
		}	

		
	}
}
