package com.example.g7anti_theftapp;

import java.util.UUID;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;

public class SIM_Card extends AppCompatActivity {

    private static final int PHONE_NUMBER_HINT = 100;
    private final int PERMISSION_REQ_CODE = 200;
    Button get;
    TextView num;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card);

        get = findViewById(R.id.button3);
        num=findViewById(R.id.Number);

        get.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final HintRequest hintRequest =
                        new HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build();

                try {
                    final GoogleApiClient googleApiClient =
                            new GoogleApiClient.Builder(SIM_Card.this).addApi(Auth.CREDENTIALS_API).build();

                    final PendingIntent pendingIntent =
                            Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);

                    startIntentSenderForResult(
                            pendingIntent.getIntentSender(),
                            PHONE_NUMBER_HINT,
                            null,
                            0,
                            0,
                            0
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHONE_NUMBER_HINT && resultCode == RESULT_OK) {
            Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
            final String phoneNumber = credential.getId();
            num.setText(phoneNumber);
        }
    }
}
