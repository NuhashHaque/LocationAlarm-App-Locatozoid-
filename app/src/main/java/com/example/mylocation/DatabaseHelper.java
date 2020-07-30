package com.example.mylocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {



    public static final String DATABASE_NAME="location.db";
    public static final String TABLE_NAME="location";
    public static final String COL1="ID";
    public static final String COL2="ADDRESS";
    public static final String COL3= "LATITUDE";
    public static final String COL4="LONGITUDE";
    public static final String COL5="CONDITION";
    public static final String COL6="RANGE";
   // public
    //database create constructor
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT ,ADDRESS TEXT,LATITUDE REAL,LONGITUDE REAL,Condition INTEGER,RANGE REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
    public boolean insertData(String Address,double Latitude,double Longitude,int Condition,double Range)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        //SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL2,Address);
        contentValues.put(COL3,Latitude);
        contentValues.put(COL4,Longitude);
        contentValues.put(COL5,Condition);
        contentValues.put(COL6,Range);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
        {
            return false;
        }
        else
        {
            return  true;
        }

    }

    public Cursor getAllData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME,null);
        if(res.getCount() == 0) {
            db.close();
            res.close();

        }          //db.close();
        //res.close();
        return res;
        //db.close();

    }
    public void delete()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        //db.rawQuery("delete from "+ TABLE_NAME,null);
        db.delete(TABLE_NAME, null, null);
        //db.close();
    }
    public void deleteEntry(String id) {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME+" where ADDRESS='"+id+"'");
       // db.close();
    }
    public void updateConditionHIGH(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_NAME+" SET Condition=1 WHERE ADDRESS='"+id+"'");
        //db.close();

        //db.execSQL("delete from "+TABLE_NAME+" where ADDRESS='"+id+"'");
        //"UPDATE myTable SET Column1 = someValue WHERE columnId = "+ someValue

    }
    public void updateConditionLOW(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        //db.execSQL("delete from "+TABLE_NAME+" where ADDRESS='"+id+"'");

        db.execSQL("UPDATE "+TABLE_NAME+" SET Condition=0 WHERE ADDRESS='"+id+"'");
       // db.close();

    }

}
