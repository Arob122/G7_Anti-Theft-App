package com.example.g7anti_theftapp;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;


/**
 * Created by delaroy on 3/3/18.
 */

public class forgetpassword extends AppCompatActivity {

    private EditText textInputEditTextEmail;
    private EditText textInputLayoutEmail;

    private Button appCompatButtonConfirm;

    private DBHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpassword);


        textInputEditTextEmail =  findViewById(R.id.Email);
        appCompatButtonConfirm =findViewById(R.id.Confirm);

        databaseHelper = new DBHelper(this);

        setTitle("Recover password");
        appCompatButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyFromSQLite();
            }
        });

    }

    private void verifyFromSQLite(){

        if (textInputEditTextEmail.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill your email", Toast.LENGTH_SHORT).show();
            return;
        }


        if (databaseHelper.checkEmail(textInputEditTextEmail.getText().toString().trim())) {
            Intent accountsIntent = new Intent(this, ConfirmPassword.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
        } else {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();

        }
    }

    private void emptyInputEditText(){
        textInputEditTextEmail.setText("");
    }
}