package com.example.gpsmapas;

import java.util.ArrayList;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity{

	GoogleMap mapa;
	boolean tomarRuta = false;
	ArrayList<LatLng> rutaTomada = new ArrayList<LatLng>();
	
	
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
				tomarRuta = !tomarRuta;
				
			}
		});
		mapa.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
			
			@Override
			public void onMyLocationChange(Location location) {
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
			    	
		    	}
		    	//mapa.animateCamera(CameraUpdateFactory.newLatLng(ll));
		    	
			}
		});
		//locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0.0f, locListener);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}