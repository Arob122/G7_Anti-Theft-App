package com.example.g7anti_theftapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class linker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linker);
        Intent i = new Intent(getApplicationContext(), SimChangedReceiver.class);
        this.startActivity(i);
    }
}