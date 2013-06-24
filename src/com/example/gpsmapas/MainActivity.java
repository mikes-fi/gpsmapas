package com.example.gpsmapas;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
//import android.app.Activity;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;

public class MainActivity extends android.support.v4.app.FragmentActivity implements AdapterView.OnItemClickListener {

	private DrawerLayout mDrawer;
	private ListView navList;
	String[] names;
	Resources res;
	TabHost tabs;
	private GoogleMap mapa;

	private ArrayList<LatLng> rutaTomada2 = new ArrayList<LatLng>();
	private ArrayList<LatLng> rutaTomada = new ArrayList<LatLng>();
	private DatabaseHandler db;
	private Context context= this;
	
	public void dibujaRuta(){
		int s = rutaTomada.size();
		
		if ( s > 1 ){
			PolylineOptions options = new PolylineOptions().width(10).color(Color.CYAN);
			options.add(rutaTomada.get(s-2));
			options.add(rutaTomada.get(s-1));
			mapa.addPolyline(options);
		}
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        
        //************************************************pestañas
        res = getResources();
        
        tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();
         
        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Ubicación",
            res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);
         
        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Datos",
            res.getDrawable(android.R.drawable.ic_dialog_info));
        tabs.addTab(spec);
         
        tabs.setCurrentTab(0);
        
        tabs.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				
			}
		});
        
        
        //**********************************************************vista
        
        this.navList = (ListView) findViewById(R.id.left_drawer);
        this.mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        // Load an array of options names
        names= getResources().getStringArray(
                R.array.nav_options);
 
        // Set previous array as adapter of the list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);
        navList.setAdapter(adapter);
        navList.setOnItemClickListener(this);
        
        mapa = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
		mapa.setMyLocationEnabled(true);
        
		mapa.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng arg0) {
				rutaTomada2.add(arg0);
				int s = rutaTomada2.size();
				
				if ( s > 1 ){
					PolylineOptions options = new PolylineOptions().width(10).color(Color.DKGRAY);
					options.add(rutaTomada2.get(s-2));
					options.add(rutaTomada2.get(s-1));
					mapa.addPolyline(options);
				}
			}
		});
		mapa.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng arg0) {
				db = new DatabaseHandler(context);
				Route r = new Route(rutaTomada2, 1L, 10L);
				db.addRoute(r);
				db.close();
				rutaTomada2 = new ArrayList<LatLng>();
				Toast t = Toast.makeText(context, "Guardando ruta", Toast.LENGTH_LONG);
				Log.v("LOL", "guardando ruta");
				t.show();
				mapa.clear();
				
			}
		});
		
		mapa.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
			
			@Override
			public void onMyLocationChange(Location location) {
				Log.v("LOL", "location changed");
		    	double lat = location.getLatitude();
		    	double lon = location.getLongitude();
		    	
		    	LatLng ll = new LatLng(lat, lon);
		    	rutaTomada.add(ll);
				dibujaRuta();
		    	if (location.getSpeed() > 1){
			    	CameraPosition pos = new CameraPosition.Builder()
						.target(ll)
						.bearing(location.getBearing())
						.zoom(16)
						.build();
			    	CameraUpdate up = CameraUpdateFactory.newCameraPosition(pos);
			    	mapa.animateCamera(up);
			    	String sp = location.getTime() + "\n" + location.getBearing() + "\n" + location.getSpeed();
			    	Toast toast = Toast.makeText(getApplicationContext(),sp , Toast.LENGTH_SHORT);
			    	toast.show();
		    	}
		    	
			}
		});
		
        
        
/*        
      //Obtenemos una referencia a los controles de la interfaz
        final EditText txtNombre = (EditText)findViewById(R.id.txtNombre);
        Button btnHola = (Button)findViewById(R.id.botonHola);
        
        btnHola.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//Creamos el Intent pasando la actividad llamadora y la actividad a llamar
                Intent intent = new Intent(MainActivity.this, FrmSaludo.class);

                //Creamos la información a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("NOMBRE", txtNombre.getText().toString());

                //Añadimos la información al intent
                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);				
			}
		});
*/
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
	    //Toast.makeText(this, "Pulsado " + names[i], Toast.LENGTH_SHORT).show();
	    //mDrawer.closeDrawers();
	    switch(i){
	    	//unidades metricas
	    	case 2:
	    		//Creamos el Intent pasando la actividad llamadora y la actividad a llamar
                Intent intent = new Intent(MainActivity.this, FrmSaludo.class);
                
                //Creamos la información a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("NOMBRE", "Hi");

                //Añadimos la información al intent
                intent.putExtras(b);
                
                //Iniciamos la nueva actividad
                startActivity(intent);
	    	break;
	    	case 1:
	    		mapa.clear();
	    		mDrawer.closeDrawers();
	    		break;
	    	case 0:
	    		db = new DatabaseHandler(this);
	    		List<Route> rutas = db.getAllRoutes();
	    		db.close();
	    		Log.v("LOL","rutas " + rutas.size());
	    		if (rutas.size() > 0){
	    			for (Route r : rutas){
	    				Log.v("LOL","poniendo ruta x" + r.getPosInit());
	    				PolylineOptions po = r.getPolylineOptions();
	    				mapa.addPolyline(po);
	    			}
	    		}
	    		mDrawer.closeDrawers();
	    		break;
	    }
	    
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
