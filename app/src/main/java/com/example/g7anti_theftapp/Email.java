package com.example.g7anti_theftapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Email extends AppCompatActivity {
Button sendEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        sendEmail=findViewById(R.id.sendEmail);

        ///////////////////// Nada started here to add send mail code////////////////////
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GMailSender.sendMail("nadafjj@gmail.com" , "Test",   " السلام عليكم ورحمة الله وبركاته "+ "\n\n"+"تم بحمد الله عملية ارسال الايميل ابشرك");
                Toast.makeText(Email.this, " Email sent  ", Toast.LENGTH_LONG).show();
            }
        });






    }
}
