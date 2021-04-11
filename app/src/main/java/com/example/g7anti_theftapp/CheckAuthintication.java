package com.example.g7anti_theftapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class CheckAuthintication extends AppCompatActivity {
    EditText username, password;
    Button btnlogin;
    DBHelper DB;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_authintication);

        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        btnlogin = (Button) findViewById(R.id.btnsignin1);
        DB = new DBHelper(this);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(CheckAuthintication.this, lockScreen.class);
                startActivity(intent);
                finish();
            }
        }, 60000);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(CheckAuthintication.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    //SharedPreferences prefs = getSharedPreferences("SIM_State", MODE_PRIVATE);
                    String username = DB.getName();//prefs.getString("username", "00000000000");//"No name defined" is the default value.
                    String password =DB.getPassword(); //prefs.getString("password", "00000000000");//"No name defined" is the default value.
                    Log.d("Check",username);
                    Log.d("Check",password);
                    Boolean checkuserpass = user.equals(username) && password.equals(pass);
                    Log.d("Check", ""+checkuserpass);
                    if(checkuserpass==true){
                        Toast.makeText(CheckAuthintication.this, "changed SIM authenticate successfully", Toast.LENGTH_SHORT).show();

                        /*Re-enter data
                        String serialNumber = DB.getSerialNumber();
                        SharedPreferences.Editor editor = getSharedPreferences("SIM_State", MODE_PRIVATE).edit();
                        editor.putString("serialNumber", serialNumber);
                        editor.putString("username", user);
                        editor.putString("password", pass);
                        editor.apply();
                        */

                        //update serial number in DB
                        TelephonyManager telephoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        String serialNumber = telephoneMgr.getSimSerialNumber();
                        DB.setSerialNumber(serialNumber, user);
                        IntentFilter intentFilter = new IntentFilter();
                        SimChangedReceiver simChangedReceiver = new SimChangedReceiver();
                        registerReceiver(simChangedReceiver, intentFilter);

                        Intent intent  = new Intent(getApplicationContext(), Homepage.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(CheckAuthintication.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CheckAuthintication.this, lockScreen.class);
                        startActivity(intent);
                        finish();
                        //SEND MAIL
                    }
                }
            }
        });
    }
}