package com.example.gpsmapas;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
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

public class MainActivity extends FragmentActivity{

	private GoogleMap mapa;
	private boolean tomarRuta = false;
	private ArrayList<LatLng> rutaTomada = new ArrayList<LatLng>();
	private ArrayList<LatLng> rutaTomada2 = new ArrayList<LatLng>();
	private DatabaseHandler db;
	private Context context= this;
	
	public void agregarMarca(LatLng ll) {
		if(true){
			//mapa.addMarker(new MarkerOptions().position(ll));
			rutaTomada.add(ll);
			dibujaRuta();
			
		}
	}
	
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
	protected void onDestroy() {
		super.onDestroy();
		db = new DatabaseHandler(this);
		Route r = new Route(rutaTomada, 5L, 10L);
		db.addRoute(r);
		db.close();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//locManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		mapa = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_mapa)).getMap();
		//mapa.addMarker(new MarkerOptions().position(new LatLng(92.134567, -19.223456)).title("Pruebita"));
		mapa.setMyLocationEnabled(true);
//		LatLng ll = new LatLng(mapa.getMyLocation().getLatitude(), 
//				mapa.getMyLocation().getLongitude());
//		CameraPosition pos = new CameraPosition.Builder()
//			.target(ll)
//			.zoom(16)
//			.build();
//    	CameraUpdate up = CameraUpdateFactory.newCameraPosition(pos);
//    	mapa.animateCamera(up);
//    
		
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
				Route r = new Route(rutaTomada2, 5L, 10L);
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
		    	agregarMarca(ll);
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
		//locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0.0f, locListener);
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
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
