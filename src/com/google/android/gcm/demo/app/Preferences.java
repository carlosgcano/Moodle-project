package com.google.android.gcm.demo.app;

import com.google.android.gcm.demo.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Preferences extends Activity {
	
	Button restrichoraria, ubicacion,back; 
	
    /** Called when the activity is first created. */
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Lo vincula a la interfaz
        setContentView(R.layout.preferencias);
        
        restrichoraria = (Button) findViewById(R.id.restrichoraria);
        //ubicacion =(Button) findViewById(R.id.ubicacion);
        back = (Button) findViewById(R.id.back);
        
        restrichoraria.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 //Creamos el Intent
                 Intent intent = new Intent(Preferences.this, TimeRestrictMenu.class);

                 startActivity(intent);
            }
       });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 //Creamos el Intent
                 Intent intent =new Intent(Preferences.this, DemoActivity.class);
                 startActivity(intent);
                 finish();
                 
            }
       });
        
        
        
   
        
       
    }
}