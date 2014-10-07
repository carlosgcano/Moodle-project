package com.google.android.gcm.demo.app;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class TimeRestrictMenu extends ListActivity {
	/** Called when the activity is first created. */

	Button back;
	
	// Array que sirve para añadir los elementos a la lista
	ArrayList<String> listItems = new ArrayList<String>();

	// Adaptador para introducir los datos en el listview
	ArrayAdapter<String> adapter;

	int i = 0;

	int position;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences prefs = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);

		// Lo vincula a la interfaz
		setContentView(R.layout.timerestrictmenu);
		
		
		System.out.println("prueba sharepref");
		// if(prefs.contains("timeRestrict")){
		System.out.println(prefs.getString("timeRestrict", "valor por defecto"));
		for (String s : prefs.getAll().keySet()) {
			if (s.contains("timeRestrict"))
				System.out.println(prefs.getString("timeRestrict" + (i),"valor por defecto"));
			i++;
		}

		
		// simple_list_item_1 es cada uno de los elementos de la lista, no
		// aparece en timerestrictmenu.xml ya que es el elemento por defecto que
		// da google

		// no hace falta llamar al listview en el que vamos a meter los datos ya
		// que llama al list view por defecto, para ello al listview de
		// timerestrictmenu.xml le ponemos de id: @android:id/list
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listItems);
		setListAdapter(adapter);

		// activa el menu contextual para cada item del list item
		registerForContextMenu(getListView());

		ListView listView = getListView();
		// llenamos la lista con la restricción de horas que está guardada en
		// shared preferences
		for (String s : prefs.getAll().keySet()) {
			if (s.contains("timeRestrict"))
				listItems.add((String) prefs.getAll().get(s));
		}

		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Cuando pulsamos se añade el item a la lista

				Intent appInfo = new Intent(TimeRestrictMenu.this,
						TimeRestrict.class);
				startActivity(appInfo);
			}
		});
		
		
		back = (Button) findViewById(R.id.back1);
		back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 //Creamos el Intent
                 Intent intent =new Intent(TimeRestrictMenu.this, Preferences.class);
                 startActivity(intent);
                 finish();
                 
            }
       });
		
	}

	// acción que realiza el boton "+"
	public void addTimeRestrict(View v) {
		listItems.add("Horario sin definir");
		adapter.notifyDataSetChanged();
	}

	@Override
	// creación del menu contextual(menu que aparece al dejar pulsado un elemento de la lista)
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		// menu inflater es el menu que aparece al dejar pulsado un elemento de
		// la lista
		MenuInflater m = getMenuInflater();
		// restricttimemenucontextmenu contiene el boton eliminar
		m.inflate(R.menu.restricttimemenucontextmenu, menu);
	}

	@Override
	// configuramos lo que pasa cada vez que pulsamos un boton de los que
	// aparecen en el menu contextual
	public boolean onContextItemSelected(MenuItem item) {
		SharedPreferences prefs = getSharedPreferences("MisPreferencias",
				Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		switch (item.getItemId()) {
		// en caso de que se pulse en eliminar
		case R.id.delete_item:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			position = (int) info.id;

			// ademas de borrar el elemento de la lista tambien lo borramos en
			// shared preferences
			for (String s : prefs.getAll().keySet()) {
				// recorremos los elementos de shared preferences por su clave,
				// si encontramos un elemento cuyo valor sea igual
				// al valor del elemento pulsado en la lista, lo eliminamos de
				// la lista
				if (prefs.getString(s, "defValue").toString()
						.equals(listItems.get(position))) {
					editor.remove(s);
					editor.commit();
					break;
				}
			}

			// Una vez eliminado de shared preferences, lo eliminamos de la
			// lista
			listItems.remove(position);
			this.adapter.notifyDataSetChanged();

			// return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	// cuando se pulse un item se abrira una nueva activity con la seleccion de
	// la restriccion horaria
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Intent appInfo = new Intent(TimeRestrictMenu.this, TimeRestrict.class);
		startActivity(appInfo);

	}

}
