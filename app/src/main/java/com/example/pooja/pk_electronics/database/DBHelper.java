package com.example.pooja.pk_electronics.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pooja.pk_electronics.model.DashboardModel;

import java.util.ArrayList;

/**
 * Created by pooja on 5/17/2017.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "pkelectronics";
    public static final String TABLE_NAME = "tbl_getdata";
    public static final String TABLE_NAME2 = "tbl_getdata2";

    Context context;
    public static final String FIELD1 = "id";
    public static final String FIELD2 = "product_name";
    public static final String FIELD3 = "price1";
    public static final String FIELD4 = "price2";
    public static final String FIELD5 = "price3";
    public static final String FIELD6 = "updated_on";

    public static final String FIELDDate = "dates";
    public static final String FIELDBy = "updated_by";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qry = " CREATE TABLE " + TABLE_NAME + "(" + FIELD1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FIELD2 + " TEXT, " + FIELD3 + " TEXT ," + FIELD4 + " TEXT," + FIELD5 + " TEXT," + FIELD6 + " TEXT) ";
        String qry2 = " CREATE TABLE " + TABLE_NAME2 + "(" + FIELD1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FIELDDate + " TEXT, " + FIELDBy + " TEXT ) ";
        sqLiteDatabase.execSQL(qry);
        sqLiteDatabase.execSQL(qry2);
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String qr = " DROP TABLE IF  EXISTS " + TABLE_NAME;
        String qr2 = " DROP TABLE IF  EXISTS " + TABLE_NAME2;
        sqLiteDatabase.execSQL(qr);
        sqLiteDatabase.execSQL(qr2);
    }

    public void insert(DashboardModel model) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD1, model.getId());
        cv.put(FIELD2, model.getPname());
        cv.put(FIELD3, model.getPrice1());
        cv.put(FIELD4, model.getPrice2());
        cv.put(FIELD5, model.getPrice3());
        cv.put(FIELD6, model.getUpdatedOn());

        db.insert(TABLE_NAME, null, cv);

    }

    public void insertDate(String a, String b) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
//        cv.put(FIELDDate, a);
//        cv.put(FIELDBy, b);
//        db.insert(TABLE_NAME2, null, cv);

//        int S = 0;
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(FIELDDate, a); //These Fields should be your String values of actual column names
//        cv.put(FIELDBy, b);
//        db.update(TABLE_NAME2, cv, "dates =" + S, null);
//        db.close();


        cv.put(FIELDDate, b);
        cv.put(FIELDBy, a);
        db.replace(TABLE_NAME2, null, cv);
    }

    public void clearSingeOrders(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String KEY_NAME = "id";
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + KEY_NAME + "='" + id + "'");
        db.close();
    }

    public void updateSingeOrders(int id, String mypnanme, String myprice1, String myprice2, String myprice3) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD2, mypnanme); //These Fields should be your String values of actual column names
        cv.put(FIELD3, myprice1);
        cv.put(FIELD4, myprice2);
        cv.put(FIELD5, myprice3);
        db.update(TABLE_NAME, cv, "id=" + id, null);
        db.close();
    }
    public String getDate() {

        String date = null;
        SQLiteDatabase db = getReadableDatabase();
            // String select_qry = " SELECT * FROM " + TABLE_NAME +" WHERE outle_id ="+outletID;
            String select_qry = " SELECT dates FROM " + TABLE_NAME2;

            Cursor cursor = db.rawQuery(select_qry, null);
     //   Cursor  cursor = database.rawQuery(query,null);
        if (cursor.moveToFirst()) {
           // while (cursor.isAfterLast() != true) {
                 date =  cursor.getString(cursor.getColumnIndex("dates"));
                Log.e("s",date);
          //  }
        }

        return date;
    }

    public ArrayList<DashboardModel> getOrders() {
        ArrayList<DashboardModel> orderList = new ArrayList<>();
        try {

            SQLiteDatabase db = getReadableDatabase();
            // String select_qry = " SELECT * FROM " + TABLE_NAME +" WHERE outle_id ="+outletID;
            String select_qry = " SELECT * FROM " + TABLE_NAME;

            Cursor cursor = db.rawQuery(select_qry, null);
            if (cursor != null) {

                while (cursor.moveToNext()) {

                    DashboardModel model = new DashboardModel();
                    model.setId(cursor.getInt(0));
                    model.setPname(cursor.getString(1));
                    model.setPrice1(cursor.getString(2));
                    model.setPrice2(cursor.getString(3));
                    model.setPrice3(cursor.getString(4));
                    model.setUpdatedOn(cursor.getString(5));

                    Log.d("all", orderList.toString());

                    orderList.add(model);


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderList;
    }

    public void clearOrders() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);

    }
}
