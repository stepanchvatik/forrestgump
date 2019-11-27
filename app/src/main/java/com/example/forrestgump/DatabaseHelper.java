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
    public static final String DATABASE_NAME = "ForrestDB";
    public static final String TABLE_NAME = "forrest";
    public static final String ID = "ID";
    public static final String SCORE = "SCORE";
    public static final String ARMOR = "ARMOR";
    public static final String BOOTS = "BOOTS";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,SCORE INTEGER,ARMOR INTEGER,BOOTS INTEGER)");
        //db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID,1);
        contentValues.put(SCORE,0);
        contentValues.put(ARMOR,0);
        contentValues.put(BOOTS,0);

        long result = db.insert(TABLE_NAME,null ,contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
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

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean updateScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCORE,score);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { "1" });
        return true;
    }
    public Cursor getScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select SCORE from "+TABLE_NAME,null);
        return res;
    }


    public boolean buy(String what,int x) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(what,x);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { "1" });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}