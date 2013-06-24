package com.example.gpsmapas;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final String TAG = "DATABASE THINGY";
	
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "rutasDB";

	private static final String TABLE_ROUTES = "rutas";
	private static final String TABLE_SEGMENTS = "segmentos";

	private static final String RUTAS_ID = "ruta_id";
	private static final String RUTAS_POS_INIT = "posicion_inicial";
	private static final String RUTAS_POS_FIN = "posicion_final";
	private static final String RUTAS_DISTANCIA = "distancia";
	private static final String RUTAS_TIEMPO = "tiempo_total";
	private static final String RUTAS_PUNT = "puntuacion";
	
	private static final String SEGMENTOS_ID = "segmento_id";
	private static final String SEGMENTOS_POS_INIT = "posicion_inicial";
	private static final String SEGMENTOS_POS_FIN = "posicion_final";
	private static final String SEGMENTOS_PUNT = "segmento_puntuacion";
	private static final String SEGMENTOS_RUTA = "segmento_ruta_fk";


	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		String createSegmentos = "CREATE TABLE " + TABLE_SEGMENTS + "(" 
				+ SEGMENTOS_ID + " INTEGER PRIMARY KEY," + SEGMENTOS_POS_INIT + " TEXT," + SEGMENTOS_POS_FIN + " TEXT,"
				+ SEGMENTOS_RUTA + " INTEGER NOT NULL," + SEGMENTOS_PUNT + " INTEGER NOT NULL," + RUTAS_PUNT + " INTETGER )";

		String createRutas = "CREATE TABLE " + TABLE_ROUTES + "(" 
				+ RUTAS_ID + " INTEGER PRIMARY KEY," + RUTAS_POS_INIT + " TEXT," + RUTAS_POS_FIN + " TEXT,"
				+ RUTAS_DISTANCIA + " REAL," + RUTAS_TIEMPO + " INTEGER," + RUTAS_PUNT + " INTETGER )";
		Log.v(TAG,"creating");
		db.execSQL(createSegmentos);
		db.execSQL(createRutas);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEGMENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new 
	public void addRoute (Route r ){
		Log.v(TAG,"Guardando ruta");
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(RUTAS_DISTANCIA, r.getDistancia());
		values.put(RUTAS_POS_FIN, r.getPosFin());
		values.put(RUTAS_POS_INIT, r.getPosInit());
		values.put(RUTAS_PUNT, r.getPuntuacion());
		values.put(RUTAS_TIEMPO, r.getTiempo());

		// Inserting Row
		long id = db.insert(TABLE_ROUTES, null, values);
		
		for (Segment s : r.getSegments()){
			ContentValues vals = new ContentValues();
			vals.put(SEGMENTOS_RUTA, id);
			vals.put(SEGMENTOS_POS_FIN, s.getPosFin());
			vals.put(SEGMENTOS_POS_INIT, s.getPosInit());
			vals.put(SEGMENTOS_PUNT, s.getPuntuacion());
			db.insert(TABLE_SEGMENTS, null, vals);
		}
		
		db.close(); // Closing database connection

	}
	
	public List<Route> getAllRoutes(){
		List<Route> rutas = new ArrayList<Route>();
		String selectQuery = "SELECT "+ RUTAS_ID + "," + RUTAS_PUNT + " FROM " + TABLE_ROUTES;
		String segmentQuery = "SELECT " + SEGMENTOS_POS_INIT + "," + SEGMENTOS_POS_FIN + " FROM "
				+ TABLE_SEGMENTS + " WHERE " + SEGMENTOS_RUTA + "=";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			Log.v(TAG,"obteniendo rutas");
			do {
				Route r = new Route();
				r.setId(Long.parseLong(cursor.getString(0)));
				r.setPuntuacion(Long.parseLong(cursor.getString(1)));
				Cursor c = db.rawQuery(segmentQuery + r.getId(), null);
				if (c.moveToFirst()) {
					do {
						Segment s = new Segment();
						s.setPosInit(c.getString(0));
						s.setPosFin(c.getString(1));
						r.getSegments().add(s);
					} while (c.moveToNext());
				}
				rutas.add(r);
				Log.v(TAG,"ruta mas");
			} while (cursor.moveToNext());
		}


		
		return rutas;
	}
	/*
	// Getting single contact
	Contact getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
				KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		// return contact
		return contact;
	}
	
	// Getting All Contacts
	public List<Contact> getAllContacts() {
		List<Contact> contactList = new ArrayList<Contact>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Contact contact = new Contact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setPhoneNumber(cursor.getString(2));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

	// Updating single contact
	public int updateContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, contact.getName());
		values.put(KEY_PH_NO, contact.getPhoneNumber());

		// updating row
		return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
	}

	// Deleting single contact
	public void deleteContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
		db.close();
	}


	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
	*/
}
