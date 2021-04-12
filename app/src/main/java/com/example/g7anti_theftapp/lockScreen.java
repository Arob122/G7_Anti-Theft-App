package com.example.g7anti_theftapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

public class lockScreen extends AppCompatActivity {
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        /*SharedPreferences.Editor editor = getSharedPreferences("SIM_State", MODE_PRIVATE).edit();
        editor.putBoolean("lock", true);
        editor.apply();*/
        DB.setStatus("lock", DB.getName());
    }
}