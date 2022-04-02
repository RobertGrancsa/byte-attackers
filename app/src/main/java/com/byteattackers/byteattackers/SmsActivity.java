package com.byteattackers.byteattackers;

import static android.view.View.VISIBLE;
import static androidx.camera.core.CameraXThreads.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SmsActivity extends AppCompatActivity {
    // Saving phone number:
    public TextInputEditText smsEditText;
    public TextInputLayout smsLayout;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Phone = "phoneKey";
    Button b1, b2;
    SharedPreferences sharedpreferences;
    TextView textView;


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

        textView = (TextView) findViewById(R.id.Text_sms);
        b1=(Button)findViewById(R.id.buttonsave);
        b2=(Button)findViewById(R.id.buttonedit);
        smsLayout = findViewById(R.id.textSms);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        textView.setText(getCity(location));


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
                        smsLayout.setError("Phone number is too short");
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
                        smsLayout.setError("Phone number is too short");
                    }
                }
            });
        }
    }
        public void smsSendMessage(View view) {
        // Find the TextView number_to_call and assign it to textView.




        // Concatenate "smsto:" with phone number to create smsNumber.
        String smsNumber = "smsto:" + smsEditText.getText().toString();
        // Find the sms_message view.

        // Create the intent.
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        // Set the data for the intent as the phone number.
        smsIntent.setData(Uri.parse(smsNumber));
        // Add the message (sms) with the key ("sms_body").
        smsIntent.putExtra("sms_body", getString(R.string.message_to_send)+textView.getText());
        // If package resolves (target app installed), send intent.
        if (smsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(smsIntent);
        }
    }
    public String getCity(Location loc) {
        String cityName = null;
        String streetName = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
                streetName = addresses.get(0).getAddressLine(0);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return  streetName;
    }
}
