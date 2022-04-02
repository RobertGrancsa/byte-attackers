package com.byteattackers.byteattackers;

import static androidx.camera.core.CameraXThreads.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SmsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        Button panic_button = findViewById(R.id.panicbutton);

        panic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsSendMessage(view);
            }
        });
    }
        public void smsSendMessage(View view) {
        // Find the TextView number_to_call and assign it to textView.
        TextView textView = (TextView) findViewById(R.id.Text_sms);
        EditText smsEditText = (EditText) findViewById(R.id.Text_number);


        // Concatenate "smsto:" with phone number to create smsNumber.
        String smsNumber = "smsto:" + smsEditText.getText().toString();
        // Find the sms_message view.

        // Create the intent.
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        // Set the data for the intent as the phone number.
        smsIntent.setData(Uri.parse(smsNumber));
        // Add the message (sms) with the key ("sms_body").
        smsIntent.putExtra("sms_body", getString(R.string.message_to_send));
        // If package resolves (target app installed), send intent.
        if (smsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(smsIntent);
        } else {
            Log.e(TAG, "Can't resolve app for ACTION_SENDTO Intent.");
        }
    }
}
