package com.example.gpsmapas;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class Route {
	private List<Segment> segments;
	private Long id;
	private String posInit, posFin;
	private Long puntuacion, tiempo;
	private Double distancia;

	public Route() {
		segments = new ArrayList<Segment>();
	}
	
	public Route(List<LatLng> puntos, Long punt, Long tiempo){
		this.tiempo = tiempo;
		int size = puntos.size();
		this.puntuacion = punt;
		this.segments = new ArrayList<Segment>();
		for(int i = 1; i < size ; i++){
			LatLng l1 = puntos.get(i-1);
			LatLng l2 = puntos.get(i);
			this.segments.add(new Segment(l1,l2,punt));
		}
		this.posInit = this.segments.get(0).getPosInit();
		this.posFin = this.segments.get(size-2).getPosFin();
	}
	
	public PolylineOptions getPolylineOptions(){
		PolylineOptions po = new PolylineOptions();
		po.width(10);
		if (this.puntuacion == null){
			return po;
		}
		if (this.puntuacion < 2){
			po.color(Color.RED);
		} else if (this.puntuacion < 4) {
			po.color(Color.YELLOW);
		} else {
			po.color(Color.GREEN);
		}
		
			
		po.add(this.segments.get(0).getLatLngInit());
		for(Segment s: this.segments){
			po.add(s.getLatLngFin());
			Log.v("ROFL", "segmento");
		}
		
		return po;
	}

	public Long getTiempo() {
		return tiempo;
	}

	public void setTiempo(Long tiempo) {
		this.tiempo = tiempo;
	}

	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}

	public Double getDistancia() {
		return distancia;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	public List<Segment> getSegments() {
		return segments;
	}

	public Long getId() {
		return id;
	}

	public String getPosFin() {
		return posFin;
	}

	public String getPosInit() {
		return posInit;
	}

	public Long getPuntuacion() {
		return puntuacion;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPuntuacion(Long puntuacion) {
		this.puntuacion = puntuacion;
	}

	public void setPosInit(String posInit) {
		this.posInit = posInit;
	}

	public void setPosFin(String posFin) {
		this.posFin = posFin;
	}

}
