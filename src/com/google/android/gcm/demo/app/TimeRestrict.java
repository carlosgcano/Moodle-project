package com.google.android.gcm.demo.app;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimeRestrict extends Activity {
	// timepicker es el selector de horario de la interfaz
	TimePicker inicio, fin;
	int i = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// asociamos los elementos de la interfaz con cada nombtre de variable
		setContentView(R.layout.timerestrict);
		inicio = (TimePicker) findViewById(R.id.timestart2);
		inicio.clearFocus();
		fin = (TimePicker) findViewById(R.id.timeend2);
		fin.clearFocus();
		Button button = (Button) findViewById(R.id.timeRestricAccept);

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// cargamos shared preferences para guardar los datos de la hora
				SharedPreferences prefs = getSharedPreferences(
						"MisPreferencias", Context.MODE_PRIVATE);
				Editor editor = prefs.edit();

				//obtenemos la hora de inicio
				//getCurrentHour() nos da la hora que pone el timepicker
				
				
				String ini ="";
				Integer hourini=inicio.getCurrentHour();				
				//time picker solo nos da el numero, si es de una sola cifra, hay que añadirle el 0 delante para que se vea como un tipo fecha
				//hacemos lo mismo para los minutos
				
				if(hourini < 10){					
					ini = "0"+hourini;
				}else{
					ini =  ""+hourini;
				}
				
				Integer iniminute =inicio.getCurrentMinute();
				if(iniminute < 10){
					ini =ini + ":" + "0"+iniminute;
				}else{
					ini = ini + ":"+iniminute;
				}
					
				//obtenemos el horario de fin y lo formateamos
				String finn="";
				Integer hourfin=fin.getCurrentHour();
				if(hourfin < 10){					
					finn = "0"+hourfin;
				}else{
					finn =  ""+hourfin;
				}
				Integer finminute=fin.getCurrentMinute();
				if(finminute < 10){					
					finn = finn + ":" + "0"+finminute;
				}else{
					finn =  finn + ":" + finminute;
				}
				

				//Casuistica de horas incorrectas:
				//si la hora de inicio es mayor que la hora de fin -> mal
				if (inicio.getCurrentHour() > fin.getCurrentHour()) {
					Toast.makeText(getBaseContext(), "Fecha incorrecta",
							Toast.LENGTH_LONG).show();

				} else {
					//si la hora de inicio es igual que la hora de fin -> miramos los minutos
					if (inicio.getCurrentHour() == fin.getCurrentHour()) {
						
						//si los minutos de inicio son mayores o iguales que los minutos de fin -> mal
						if (inicio.getCurrentMinute() >= fin.getCurrentMinute()) {
							Toast.makeText(getBaseContext(),"Fecha incorrecta", Toast.LENGTH_LONG).show();

						}else{
							
						
						//si no es ninguna de las anteriores la hora de restriccion sera correcta.
					
						
						Toast.makeText(getBaseContext(), "Fecha añadida",
								Toast.LENGTH_LONG).show();

						// para guardar el campo en shared preferences antes
						// necesitamos obtener un indice que sea diferente de
						// los anteriores.
						// en el for conseguimos un contador que se añade a
						// timeRestrict
						Set<String> keys = prefs.getAll().keySet();
						for (String s : keys) {
							if (s.contains("timeRestrict"))
								i++;
						}

						// guardamos la hora en shared preferences
						editor.putString("timeRestrict" + i, "inicio: " + ini + " - " + "fin: " + finn);
						editor.commit();

						// cerramos la actividad cuando pulsamos el boton

						Intent intent = new Intent(TimeRestrict.this,TimeRestrictMenu.class);
						startActivity(intent);

					}
				}
			}
			}
		});
	}
}
