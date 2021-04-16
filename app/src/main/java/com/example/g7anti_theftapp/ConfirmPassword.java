package com.example.g7anti_theftapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;


/**
 * Created by delaroy on 3/3/18.
 */

public class ConfirmPassword extends AppCompatActivity {
    private EditText textInputEditTextPassword;
    private EditText textInputEditTextConfirmPassword;
    private DBHelper databaseHelper;
    private Button appCompatButtonReset;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmpassword);

        databaseHelper = new DBHelper(this);

        textInputEditTextPassword = findViewById(R.id.pass1);
        textInputEditTextConfirmPassword =  findViewById(R.id.pass2);

        appCompatButtonReset =  findViewById(R.id.RESET);

        Intent intent = getIntent();
        email = databaseHelper.getName();
                //intent.getStringExtra("EMAIL");
        Log.d("Reset", "email in reset " + email);

        setTitle("Reset password");

        appCompatButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
    }


    private void updatePassword() {

        String value1 = textInputEditTextPassword.getText().toString().trim();
        String value2 = textInputEditTextConfirmPassword.getText().toString().trim();

        if (value1.isEmpty() && value2.isEmpty()){
            Toast.makeText(this, "fill all fields ", Toast.LENGTH_LONG).show();
            return;
        }

        if (!value1.contentEquals(value2)){
            Toast.makeText(this, "password doesn't match", Toast.LENGTH_LONG).show();
            return;
        }

        if (!databaseHelper.checkEmail(email)) {
           Toast.makeText(this,"email doesn't exist",Toast.LENGTH_LONG).show();
            return;

        } else {
            databaseHelper.updatePassword(email, value1);

            Toast.makeText(this, "password reset successfully", Toast.LENGTH_SHORT).show();
            emptyInputEditText();

            Intent intent = new Intent(this, Homepage.class);
            startActivity(intent);
            finish();
        }

    }

    private void emptyInputEditText()
    {
        textInputEditTextPassword.setText("");
        textInputEditTextConfirmPassword.setText("");
    }
}