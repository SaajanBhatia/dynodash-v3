package com.example.dynodash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dynodash.ui.customer.list.RestaurantListItem;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "past_restaurants";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "restaurants";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_RESTAURANT_ID = "restaurantID";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_ADDRESS + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_RESTAURANT_ID + " TEXT" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // upgrade logic here
    }

    public void addRestaurant(RestaurantListItem restaurant) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, restaurant.getRestaurantName());
        values.put(COLUMN_ADDRESS, restaurant.getRestaurantAddr());
        values.put(COLUMN_DESCRIPTION, restaurant.getRestaurantDesc());
        values.put(COLUMN_RESTAURANT_ID, restaurant.getRestaurantID());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<RestaurantListItem> getAllRestaurants() {
        List<RestaurantListItem> restaurants = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {COLUMN_NAME, COLUMN_ADDRESS, COLUMN_DESCRIPTION, COLUMN_RESTAURANT_ID};
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
            String restaurantID = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_ID));
            RestaurantListItem restaurant = new RestaurantListItem(name, description, address, restaurantID);
            restaurants.add(restaurant);
        }
        cursor.close();
        db.close();
        return restaurants;
    }

    public boolean restaurantExists(RestaurantListItem restaurant) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_NAME + " = ? AND " + COLUMN_ADDRESS + " = ? AND " + COLUMN_DESCRIPTION + " = ? AND " + COLUMN_RESTAURANT_ID + " = ?";
        String[] selectionArgs = {restaurant.getRestaurantName(), restaurant.getRestaurantAddr(), restaurant.getRestaurantDesc(), restaurant.getRestaurantID()};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
}
