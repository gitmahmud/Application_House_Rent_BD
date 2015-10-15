package com.experiment.comp.application_house_rent_bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by comp on 6/29/2015.
 */
public class MyDBHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "houseDB.db";
    private static final String TABLE_RENTS = "rents";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_POSTTIME = "posttime";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_AREA = "area";
    public static final String COLUMN_CONTACT = "contact";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_IMAGE= "imageurl";
    public static final String COLUMN_MAP = "mapurl";

    //


    private static final String TABLE_LOGIN = "login";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String TAG = "Shout";




    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RENTS_TABLE = "CREATE TABLE "+TABLE_RENTS +
                "("+COLUMN_ID+" INTEGER ,"
                +COLUMN_TITLE+" TEXT , "+COLUMN_POSTTIME+" DATETIME ,"
                +COLUMN_DESCRIPTION+" TEXT , "+COLUMN_AREA+" TEXT , "
                +COLUMN_CONTACT+" TEXT , "+COLUMN_STATUS+" TEXT , "
                +COLUMN_MAP+" TEXT , "+COLUMN_IMAGE+" TEXT "
                +")";
        db.execSQL(CREATE_RENTS_TABLE);
        Log.d("Shout",TABLE_RENTS+" table created");

        //
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST "+TABLE_RENTS);


        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);

    }
    public int latestID()
    {
        String query = "Select max(_id) FROM " + TABLE_RENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        int id;
        if(cursor.getString(0) == null){
            id = - 1;
        }
        else {
            id = Integer.parseInt(cursor.getString(0));
        }


        //Log.d("Shout" , "Latest id "+String.valueOf(id));
        return id;

    }

    public int lastID()
    {
        String query = "Select min(_id) FROM " + TABLE_RENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        int id;
        if(cursor.getString(0) == null){
            id = -1;
        }
        else {
            id = Integer.parseInt(cursor.getString(0));
        }


        //Log.d("Shout" , "Last id "+String.valueOf(id));
        return id;
    }

    public boolean insertRentItem(ArrayList<RentItem> rentItems )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        for(RentItem rentItem:rentItems )
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_ID,rentItem.get_rentId());
            contentValues.put(COLUMN_TITLE, rentItem.getRentTitle());
            contentValues.put(COLUMN_POSTTIME, String.valueOf(rentItem.getRentPosttime()));
            contentValues.put(COLUMN_DESCRIPTION, rentItem.getRentDescription());
            contentValues.put(COLUMN_AREA, rentItem.getRentArea());
            contentValues.put(COLUMN_CONTACT, rentItem.getRentContact());
            contentValues.put(COLUMN_STATUS, rentItem.getRentStatus().toString());
            contentValues.put(COLUMN_MAP, rentItem.getRentGoogleMapUrl());
            contentValues.put(COLUMN_IMAGE, rentItem.getRentImageName());



            long retVal =  db.insert(TABLE_RENTS, null, contentValues);
            //Log.d("Shout","Content value to insert : "+contentValues.toString());
            Log.d("Shout","Inserted with retval "+String.valueOf(retVal));



        }
        db.close();
        return true;
    }

    public boolean removeIDs(int[] X)
    {
        SQLiteDatabase db = this.getWritableDatabase();


        for(int i =0;i<X.length;i++) {
           if( !(db.delete(TABLE_RENTS,COLUMN_ID + " = "+X[i],null) >0))
           {
               db.close();
               return false;
           }

        }
        db.close();
        return true;

    }
    public ArrayList<RentItem> getListData()
    {
        String query = "Select * FROM " + TABLE_RENTS;
        ArrayList<RentItem> rentItems = new ArrayList<RentItem>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        //Log.d("Shout","Inside Get List");


       if(cursor !=null &&  cursor.moveToFirst()) {

           do {
               RentItem rentItem = getItemFromCursor(cursor);

               rentItems.add(rentItem);
               //Log.d("Shout",rentItem.getRentDescription());
           } while (cursor.moveToNext() );
       }
        else {
           Log.d("Shout","Empty SQlite DB");
       }



        //sort
        Collections.sort(rentItems );

        return rentItems;
    }
    public List<String> getDistinctAreas()
    {
        List<String> areas = new ArrayList<String>();
        String query = "SELECT distinct(area) FROM "+TABLE_RENTS+" where status = 'ACTIVE' ";
        ArrayList<RentItem> rentItems = new ArrayList<RentItem>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);



        Log.d("Nout","SQlite");
        if(cursor !=null &&  cursor.moveToFirst()) {

            do {
                Log.d("Nout",cursor.getString(0));
                areas.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }



        return areas;
    }
    public List<RentItem> getAreaItems(String area)
    {
        List<RentItem> rentItems = new ArrayList<RentItem>();
        String query = "SELECT * FROM "+TABLE_RENTS+" where area =  '" + area + "' and status = 'ACTIVE' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);



        if(cursor !=null &&  cursor.moveToFirst()) {

            do {
               RentItem rentItem = getItemFromCursor(cursor);
                rentItems.add(rentItem);
            } while (cursor.moveToNext() );
        }

        Collections.sort(rentItems );



        return rentItems;

    }

    public RentItem getItemFromCursor(Cursor cursor)
    {
        RentItem rentItem = new RentItem();
        rentItem.set_rentId(cursor.getInt(0));
        rentItem.setRentTitle(cursor.getString(1));


        Timestamp timestamp = DateTimeHelper.StringToDate(cursor.getString(2));
        rentItem.setRentPosttime(timestamp);

        rentItem.setRentDescription(cursor.getString(3));
        rentItem.setRentArea(cursor.getString(4));
        rentItem.setRentContact(cursor.getString(5));
        rentItem.setRentStatus(RentStatus.valueOf(cursor.getString(6)));
        rentItem.setRentImageName(cursor.getString(8));
        rentItem.setRentGoogleMapUrl(cursor.getString(7));

        return rentItem;

    }


    public void showCurrentSQLiteDB()
    {
        Log.d("Shout","Current SQLITEDB ITEM");
        ArrayList<RentItem> rentItems = getListData();
        for(RentItem rentItem: rentItems)
        {
            rentItem.showAtLog();
        }


    }


    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }



 /*   public void addProduct(Product product)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCTNAME,product.getProductName());
        contentValues.put(COLUMN_QUANTITY,product.getQuantity());

        SQLiteDatabase db = this.getWritableDatabase();
        long retVal =  db.insert(TABLE_PRODUCTS, null, contentValues);
        Log.d("***retval***",String.valueOf(retVal));
        db.close();
    }
    public Product findProduct(String productname) {
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " = \"" + productname + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        Product product = new Product();

        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.setQuantity(Integer.parseInt(cursor.getString(2)));

            cursor.close();
        }
        else
        {
            product = null;
        }

        db.close();
        return product;


    }
    public boolean deleteProduct(String productname) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Product product = new Product();

        if (cursor.moveToFirst()) {
            product.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(product.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
    */




}
