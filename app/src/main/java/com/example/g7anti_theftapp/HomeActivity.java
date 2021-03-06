package com.example.g7anti_theftapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button signup, signin;
    DBHelper DB;
    boolean isSignedUp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);

        signup = (Button) findViewById(R.id.reg);
        signin = (Button) findViewById(R.id.log);

        isSignedUp = false;
        DB = new DBHelper(this);
        String EmailOld = DB.getName();//prefs.getString("username", "");//"No name defined" is the default value.
        String passwordOld = DB.getPassword();//prefs.getString("password", "");//"No name defined" is the default value.



        if (!EmailOld.equals("") && !passwordOld.equals("")) {
            isSignedUp =true;
        }
        if (isSignedUp){
            signup.setEnabled(false);

        }
        else {
            signup.setEnabled(true);

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Signup.class);
                    startActivity(intent);
                }
            });
        }



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });



    }
}

