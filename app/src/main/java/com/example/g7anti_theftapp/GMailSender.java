package com.example.g7anti_theftapp;

import android.os.AsyncTask;
import android.util.Log;

import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

public class GMailSender {
    public static final String TAG = "TAG";

    public static void sendMail(String recepient, String MailSubject, String MailLetter){
        Log.d(TAG, "ERRRROR 0 :");

        Properties properties=new Properties();
        //

        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        final String myAccount= "Anti.Theft.application@gmail.com";
        final String password="Seniors2021";


        javax.mail.Session session= Session.getInstance(properties, new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccount,password);

            }

        });

        Log.d(TAG, "after javax.mail.PasswordAuthentication :");

        javax.mail.Message message=new MimeMessage(session);
        //prepare message
        try{
            message.setFrom(new InternetAddress(myAccount));
            message.setRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(recepient));//You can change TO
            message.setSubject(MailSubject);
            message.setContent(MailLetter,"text/html; charset=utf-8");
        }
        catch (Exception ex){
            Log.d(TAG, "ERRRROR 2 :" + ex.toString());
        }
        //end
        //send the message
        new SendMail().execute(message);

    }


    private static class SendMail extends AsyncTask<javax.mail.Message,String,String> {

        @Override
        protected String doInBackground(Message... messages) {
            try{
                Transport.send(messages[0]);
                return "success";
            }
            catch (Exception ex){
                Log.d(TAG, "ERRRROR 12 :" + ex.toString());
                return "error";
            }
        }
    }
}