package com.byteattackers.byteattackers;

import static android.view.View.VISIBLE;
import static androidx.camera.core.CameraXThreads.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SmsActivity extends AppCompatActivity {
    // Saving phone number:
    public TextInputEditText smsEditText;
    public TextInputLayout smsLayout;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Phone = "phoneKey";
    Button b1, b2;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        Button panic_button = findViewById(R.id.panicbutton);
        smsEditText =findViewById(R.id.text_sms);
        panic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsSendMessage(view);
            }
        });
        b1=(Button)findViewById(R.id.buttonsave);
        b2=(Button)findViewById(R.id.buttonedit);
        smsLayout = findViewById(R.id.textSms);

//        smsLayout.setErrorEnabled(true);
//        smsLayout.setError("Ai numaru prea surt");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String phonenr = sharedpreferences.getString(Phone, "None");
        b2.setVisibility(View.GONE);
        if(phonenr.equals("None")) {
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = String.valueOf(smsEditText.getText());
                    if (phone.length() == 10) {
                        smsLayout.setErrorEnabled(false);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Phone, phone);
                        editor.commit();
                        Snackbar.make(view, "Saved phone number", Snackbar.LENGTH_SHORT).show();
                        //b1.setText("EDIT");
                        b1.setVisibility(View.GONE);
                        b2.setVisibility(VISIBLE);
                    }
                    else {
                        smsLayout.setErrorEnabled(true);
                        smsLayout.setError("Ai numaru prea surt");
                    }
                }
            });
        }
        else {
            b1.setVisibility(View.GONE);
            b2.setVisibility(VISIBLE);
            smsEditText.setText(phonenr);
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = String.valueOf(smsEditText.getText());
                    if (phone.length() == 10) {
                        smsLayout.setErrorEnabled(false);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Phone, phone);
                        editor.commit();
                        Snackbar.make(view, "Edited phone number", Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        smsLayout.setErrorEnabled(true);
                        smsLayout.setError("Ai numaru prea surt");
                    }
                }
            });
        }
    }
        public void smsSendMessage(View view) {
        // Find the TextView number_to_call and assign it to textView.
        TextView textView = (TextView) findViewById(R.id.Text_sms);



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
        }
    }
}
