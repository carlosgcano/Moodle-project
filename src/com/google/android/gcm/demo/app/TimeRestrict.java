package com.google.android.gcm.demo.app;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimeRestrict extends Activity {
	// timepicker es el selector de horario de la interfaz
	TimePicker inicio, fin;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
			
		// asociamos los elementos de la interfaz con cada nombtre de variable
		setContentView(R.layout.timerestrict);
		inicio = (TimePicker) findViewById(R.id.timestart2);
		inicio.setIs24HourView(DateFormat.is24HourFormat(this));
		inicio.clearFocus();
		
		fin = (TimePicker) findViewById(R.id.timeend2);
		fin.setIs24HourView(DateFormat.is24HourFormat(this));
		fin.clearFocus();
		
		Button button = (Button) findViewById(R.id.timeRestricAccept);

		button.setOnClickListener(new View.OnClickListener() {

		Calendar initialtime=new GregorianCalendar();
		Calendar finaltime=new GregorianCalendar();
		
		
		
			@Override
			public void onClick(View v) {

				// cargamos shared preferences para guardar los datos de la hora
				SharedPreferences prefs = getSharedPreferences(
						"MisPreferencias", Context.MODE_PRIVATE);
				Editor editor = prefs.edit();

				
				
				initialtime.set(Calendar.HOUR_OF_DAY, inicio.getCurrentHour());
				initialtime.set(Calendar.MINUTE, inicio.getCurrentMinute());				
				finaltime.set(Calendar.HOUR_OF_DAY, fin.getCurrentHour());
				finaltime.set(Calendar.MINUTE, fin.getCurrentMinute());

				
				System.out.println(initialtime);
				System.out.println(finaltime);
				System.out.println(!initialtime.before(finaltime));
				
				if(!initialtime.before(finaltime)){
					Toast.makeText(getBaseContext(), "Fecha incorrecta",
							Toast.LENGTH_LONG  ).show();
				}else{
					
					// para guardar el campo en shared preferences antes
					// necesitamos obtener un indice que sea diferente de
					// los anteriores.
					// en el for conseguimos un contador que se añade a
					// timeRestrict
					Set<String> keys = prefs.getAll().keySet();
					int i=0;
					for (String s : keys) {
						if (s.contains("timeRestrict"))
							i++;
						}

					// guardamos la hora en shared preferences
					String horainicio=String.format("%02d:%02d", initialtime.get(Calendar.HOUR_OF_DAY), initialtime.get(Calendar.MINUTE));
					String horafin=String.format("%02d:%02d", finaltime.get(Calendar.HOUR_OF_DAY), finaltime.get(Calendar.MINUTE));
					
					// guardamos la hora en shared preferences
					editor.putString("timeRestrict" + i, "inicio: " + horainicio + " - " + "fin: " + horafin);
					editor.commit();
					
					Toast.makeText(getBaseContext(), "Fecha añadida",
							Toast.LENGTH_LONG  ).show();
					
					// cerramos la actividad cuando pulsamos el boton
					Intent intent = new Intent(TimeRestrict.this,TimeRestrictMenu.class);
					startActivity(intent);
				}
				
			}
			
		});
	}
}
