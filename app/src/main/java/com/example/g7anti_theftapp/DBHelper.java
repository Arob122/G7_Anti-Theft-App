package com.example.g7anti_theftapp;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import static android.content.Context.MODE_PRIVATE;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String TABLE_USER = "users";
    private static final String COLUMN_USER_EMAIL = "Email";
    ;

    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(Email TEXT primary key, password TEXT,SIM_serialNumber TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String Email, String password, String SIM_serialNumber, String status){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("Email", Email);
        contentValues.put("password", password);
        contentValues.put("status", status);
        Log.d("getSerialNumber"," try "+  SIM_serialNumber);
        contentValues.put("SIM_serialNumber", SIM_serialNumber);
        long result = MyDB.insert("users", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean checkEmail(String Email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where Email = ?", new String[]{Email});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }


    public Boolean checkEmailpassword(String Email, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where Email = ? and password = ?", new String[] {Email,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public String getSerialNumber(String Email, String password){
        /*SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select SIM_serialNumber from users where Email = ? and password = ?", new String[] {Email,password});
        cursor.moveToFirst();
        String SIM_serialNumber="";
        try{
            SIM_serialNumber = cursor.getString(cursor.getColumnIndex("SIM_serialNumber"));
        }catch(Exception e){
            Log.d("getSerialNumber","Exception");
        }
       return SIM_serialNumber;

*/
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select SIM_serialNumber from users", null);
        cursor.moveToFirst();
        String SIM_serialNumber="";
        try{
            SIM_serialNumber = cursor.getString(cursor.getColumnIndex("SIM_serialNumber"));
            Log.d("getSerialNumber",SIM_serialNumber);

        }catch(Exception e){
            Log.d("getSerialNumber","Exception");
        }
       return SIM_serialNumber;
    }

    public String getName(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select Email from users", null);
        cursor.moveToFirst();
        String Email="";
        try{
            Email = cursor.getString(cursor.getColumnIndex("Email"));
            Log.d("getSerialNumber","username "+Email);

        }catch(Exception e){
            Log.d("getSerialNumber","username Exception");
        }
        return Email;
    }

    public String getPassword(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select password from users", null);
        cursor.moveToFirst();
        String password="";
        try{
            password = cursor.getString(cursor.getColumnIndex("password"));
            Log.d("getSerialNumber","password "+password);

        }catch(Exception e){
            Log.d("getSerialNumber","password Exception");
        }
        return password;
    }

    public String getUserStatus(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select status from users", null);
        cursor.moveToFirst();
        String status="";
        try{
            status = cursor.getString(cursor.getColumnIndex("status"));
            Log.d("getSerialNumber","status "+status);

        }catch(Exception e){
            Log.d("getSerialNumber","status Exception");
        }
        return status;
    }

    public void setSerialNumber(String SerialNumber,String Email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("UPDATE users\n SET SIM_serialNumber = ? WHERE Email = ?;",new String[] {SerialNumber,Email});
    }

    public void setStatus(String status,String Email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("UPDATE users\n SET status = ? WHERE Email = ?;",new String[] {status,Email});
    }

}
