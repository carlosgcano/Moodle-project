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

	TimePicker inicio, fin;
	int i=0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		
		setContentView(R.layout.timerestrict);
		inicio = (TimePicker) findViewById(R.id.timestart2);
		fin = (TimePicker) findViewById(R.id.timeend2);
		Button button = (Button) findViewById(R.id.timeRestricAccept);

		button.setOnClickListener(new View.OnClickListener() {
		
		
			@Override
			public void onClick(View v) {
				
				// displaying timepicker value as pop up notification
				SharedPreferences prefs = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
				Editor editor = prefs.edit();
				
				Set<String> keys=prefs.getAll().keySet();
				for(String s: keys){
					if(s.contains("timeRestrict"))
						i++;
				}
				
				Calendar calinicio = Calendar.getInstance();
				calinicio.set(Calendar.HOUR_OF_DAY, inicio.getCurrentHour());
				calinicio.set(Calendar.MINUTE, inicio.getCurrentMinute());
				calinicio.getTimeInMillis();
				String ini=calinicio.getTime().toString();
				
				Calendar calfin = Calendar.getInstance();
				calfin.set(Calendar.HOUR_OF_DAY, inicio.getCurrentHour());
				calfin.set(Calendar.MINUTE, fin.getCurrentMinute());
				calfin.getTimeInMillis();
				String finn=calinicio.getTime().toString();
				
				
				//guardamos la hora en shared preferences
				editor.putString("timeRestrict"+i, ini);
				editor.commit();
				
				
				
		/**if (calinicio.get(Calendar.HOUR) < calfin.get(Calendar.HOUR)) {
					Toast.makeText(getBaseContext(), "CORRECTO",
							Toast.LENGTH_LONG).show();

				} else {
					if (calinicio.get(Calendar.HOUR) > calfin
							.get(Calendar.HOUR)) {
						Toast.makeText(getBaseContext(), "fecha erronea",
								Toast.LENGTH_LONG).show();

				} else {
					if (calinicio.get(Calendar.HOUR) == calfin
								.get(Calendar.HOUR)) {
						if (calinicio.get(Calendar.MINUTE) >= calfin.get(Calendar.MINUTE)) {
								Toast.makeText(getBaseContext(),
										"fecha erronea", Toast.LENGTH_LONG)
										.show();

							}
						}
					}
				}

			**/
				
				
				
				//cerramos la actividad cuando pulsamos el boton
				
				Intent intent =new Intent(TimeRestrict.this, TimeRestrictMenu.class);
                startActivity(intent);
                
                
				}
	
		});
	}
}
