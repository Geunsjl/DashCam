package com.geunsjl.dashcam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Jan-Lennert on 2/01/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //All static variables
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "DashCam";

    //Table name
    private static final String TABLE_LOCATIONS = "locations";

    //Locations Table Column names
    private static final String KEY_ID = "id";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGTITUDE = "longtitude";

    //Kolomnamen
    public static final double COL_LATITUTDE = 1;
    public static final double COL_LONGTITUDE = 2;

    public static final String[] ALL_KEYS = new String[] {KEY_ID, KEY_LATITUDE, KEY_LONGTITUDE};

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //Creating tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_LOCATIONS_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LATITUDE + " DOUBLE,"
            + KEY_LONGTITUDE + " DOUBLE" + ")";
        db.execSQL(CREATE_LOCATIONS_TABLE);
    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older database if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);

        //Create tables again
        onCreate(db);
    }

    //CRUD METHODS

    //Add new record
    public void addLocation(double latitude, double longtitude)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, latitude); //latitude
        values.put(KEY_LONGTITUDE, longtitude); //longtitude

        //Inserting row
        db.insert(TABLE_LOCATIONS, null, values);
        db.close();
    }

    //Get all records
    public Cursor getAllLocations()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = null;
        Cursor c = 	db.query(true, TABLE_LOCATIONS, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public ArrayList<Cursor> getData(String Query) throws SQLException {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }

}
