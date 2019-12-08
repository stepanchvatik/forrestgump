package com.example.forrestgump;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ProgrammingKnowledge on 4/3/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Forrest.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ITEMS (ID INTEGER PRIMARY KEY AUTOINCREMENT,ARMOR INTEGER,BOOTS INTEGER)");
        db.execSQL("CREATE TABLE SCORES (ID INTEGER PRIMARY KEY AUTOINCREMENT,SCORE INTEGER,DATETIME DEFAULT CURRENT_TIMESTAMP)");
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID",1);
        contentValues.put("ARMOR",1);
        contentValues.put("BOOTS",1);

        long result = db.insert("ITEMS",null ,contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ITEMS");
        onCreate(db);
    }

   /* public boolean (String name,String surname,String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID,"1");
        contentValues.put(SCORE,0);
        contentValues.put(ARMOR,0);
        contentValues.put(BOOTS,0);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }*/

    public Cursor getItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ARMOR,BOOTS from ITEMS",null);
        return res;
    }

    public Cursor getHighestScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select max(SCORE) from SCORES",null);

        return res;
    }
    public boolean updateScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SCORE",score);

        db.insert("SCORES",null, contentValues);
        return true;
    }


    public boolean buy(String what,int x) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(what,x);
        db.update("ITEMS", contentValues, "ID = ?",new String[] { "1" });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS ITEMS");
        return 1;
    }
}