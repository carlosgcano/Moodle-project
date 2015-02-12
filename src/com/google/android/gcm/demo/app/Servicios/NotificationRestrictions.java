package com.google.android.gcm.demo.app.Servicios;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.google.android.gcm.demo.app.DemoActivity;
import com.google.android.gcm.demo.app.TimeRestrict;

import android.content.Context;
import android.content.SharedPreferences;


public class NotificationRestrictions {
	Calendar inicio = Calendar.getInstance();
	Calendar fin = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.UK);
	Collection<String> valuesSharedprefs;
	

	
	
	//Esta clase nos dirá si la hora del sistema coincide con el horario de restriccion, si es asi se
	//devolverá un boolean = true que es el que se comprobara cada vez que se active el reconocimiento de actividades
	 
	
	@SuppressWarnings("unchecked")
	public boolean  isRestrict()		{
		boolean b=false;
		
		//Recogemos las preferencias guardadas en sharedpreferences
		SharedPreferences sp=DemoActivity.getPrefs();
		if(sp!=null){
			
			//Recogemos los valores de sharedpreferences
			valuesSharedprefs = (Collection<String>) sp.getAll().values();
			if(valuesSharedprefs!=null){
				
				
				for(String s: valuesSharedprefs){
					
					if(s.startsWith("Inicio")){
						
						//Por cada uno de los string de sharedpreferences recogemos
						//el tiempo de inicio y de fin
						int inihora=Integer.parseInt(s.substring(8, 10));
						int inimin=Integer.parseInt(s.substring(11, 13));
						
						int finhora=Integer.parseInt(s.substring(21, 23));
						int finmin=Integer.parseInt(s.substring(24, 26));
						
						inicio.set(Calendar.HOUR_OF_DAY,inihora);
						inicio.set(Calendar.MINUTE,inimin);
							
						fin.set(Calendar.HOUR_OF_DAY,finhora);
						fin.set(Calendar.MINUTE,finmin);

						//vemos si el tiempo actual esta entre el tiempo de inicio y fin de nuestra restricción
						 if ( inicio.before(Calendar.getInstance()) && fin.after(Calendar.getInstance())) {
							 
							 //Si es asi entonces nuestro boolean sera true y cuando se reconozca
							 //una nueva actividad entonces no se lanzara
							 b=true;
							System.out.println("Es tiempo restringido?: "+b);
							 break;
						    }
					}else{
						throw new IllegalArgumentException("Error debido a que el argumento no aparece en sharedpreferences");
					}
				
				}
			}
			
			
		}
		System.out.println("Valor final del boolean de restriccion:"+b);
		return b; 	
		
	}
	
}
