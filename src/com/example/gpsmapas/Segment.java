package com.example.gpsmapas;

import com.google.android.gms.maps.model.LatLng;

public class Segment {
	private Long id;
	private String posInit, posFin;
	private Long puntuacion;
	private Double distancia;

	public Segment(String init, String fin, Long punt) {
		this.posInit = init;
		this.posFin = fin;
		this.puntuacion = punt;
	}

	public Segment(LatLng l1, LatLng l2, Long punt) {
		this.puntuacion = punt;
		this.posInit = l1.latitude + "," + l1.longitude;
		this.posFin = l2.latitude + "," + l2.longitude;
		this.distancia = Math.sqrt(Math.pow(l1.latitude - l2.latitude, 2)
				+ Math.pow(l1.longitude - l2.longitude, 2));
	}

	public Segment() {
	}
	
	public LatLng getLatLngInit(){
		String[] pos = posInit.split(",");
		LatLng ll = new LatLng(Double.parseDouble(pos[0]), Double.parseDouble(pos[1])); 
		return ll;
	}

	public LatLng getLatLngFin(){
		String[] pos = posFin.split(",");
		LatLng ll = new LatLng(Double.parseDouble(pos[0]), Double.parseDouble(pos[1])); 
		return ll;
	}

	public Long getId() {
		return id;
	}

	public Double getDistancia() {
		return distancia;
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
