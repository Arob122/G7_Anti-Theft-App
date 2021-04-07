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

public class Signup extends AppCompatActivity {

    EditText username, password, repassword;
    Button signup, signin;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (Button) findViewById(R.id.btnsignin);
        DB = new DBHelper(this);

        SharedPreferences prefs = getSharedPreferences("SIM_State", MODE_PRIVATE);
        String usernameOld = prefs.getString("username", "");//"No name defined" is the default value.
        String passwordOld = prefs.getString("password", "");//"No name defined" is the default value.
        Log.d("Check",usernameOld);
        Log.d("Check",passwordOld);

        if(!usernameOld.equals("")&&!passwordOld.equals("")){
            Toast.makeText(Signup.this, "The user already exist", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),Homepage.class);
            startActivity(intent);
            finish();
        }






        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if(user.equals("")||pass.equals("")||repass.equals(""))
                    Toast.makeText(Signup.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{


                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkusername(user);
                        if(checkuser==false){
                            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            String serialNumber ="";
                            try {
                                serialNumber = tManager.getSimSerialNumber();
                                Log.d("CheckService class","Read "+ serialNumber);
                            } catch (Exception e) {
                                Log.d("CheckService class","Exception");
                            }
                            Boolean insert = DB.insertData(user, pass,serialNumber);
                            if(insert==true){
                                Toast.makeText(Signup.this, "Registered successfully", Toast.LENGTH_SHORT).show();

                                //Deem
                                SharedPreferences.Editor editor = getSharedPreferences("SIM_State", MODE_PRIVATE).edit();
                                editor.putString("serialNumber", serialNumber);
                                editor.putString("username", user);
                                editor.putString("password", pass);
                                editor.apply();
                                IntentFilter intentFilter = new IntentFilter();
                                SimChangedReceiver simChangedReceiver = new SimChangedReceiver();
                                registerReceiver(simChangedReceiver, intentFilter);
                                //end detecting
                                //Deem
                                Intent intent = new Intent(getApplicationContext(),Homepage.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(Signup.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(Signup.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Signup.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                } }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}