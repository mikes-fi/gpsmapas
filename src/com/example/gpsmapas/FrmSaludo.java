package com.example.gpsmapas;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FrmSaludo extends Activity{
	
	private ListView navList;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saludo);
        
        //***************************************************vista
        this.navList = (ListView) findViewById(R.id.left_drawer);
        
        // Load an array of options names
        final String[] names = getResources().getStringArray(
                R.array.nav_options);
 
        // Set previous array as adapter of the list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);
        navList.setAdapter(adapter);
        
        
      //Localizar los controles
        TextView txtSaludo = (TextView)findViewById(R.id.txtSaludo);

        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();

        //Construimos el mensaje a mostrar
        txtSaludo.setText("Hola " + bundle.getString("NOMBRE"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
