package com.byteattackers.byteattackers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class TranslateActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private TextView readText;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private TextRecognizer recognizer;
    private Translator englishRomanianTranslator;
    private TranslatorOptions options;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_camera);
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.button1);
        readText = findViewById(R.id.readText);



        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                englishRomanianTranslator = Translation.getClient(options);
                DownloadConditions conditions = new DownloadConditions.Builder()
                        .requireWifi()
                        .build();
                englishRomanianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                button.setVisibility(View.VISIBLE);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(this, "translation failure", Toast.LENGTH_SHORT).show();
                            }
                        });
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

            InputImage image = InputImage.fromBitmap(photo, 0);
            Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
                @Override
                public void onSuccess(Text text) {
                    englishRomanianTranslator.translate(text.getText())
                            .addOnSuccessListener(new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String s) {
                                    readText.setText(s);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    readText.setText("Something went wrong at translate");
                                }
                            });
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Romanian-English was selected", Toast.LENGTH_SHORT).show();
                options = new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ROMANIAN)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build();
                break;
                //return true;
            case R.id.item2:
                Toast.makeText(this, "Ukrainian-English was selected", Toast.LENGTH_SHORT).show();
                options = new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.UKRAINIAN)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build();
                break;
                //return true;
            case R.id.item3:
                Toast.makeText(this, "French-English was selected", Toast.LENGTH_SHORT).show();
                options = new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.FRENCH)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build();
                break;
                //return true;
            case R.id.item4:
                Toast.makeText(this, "Italian-English was selected", Toast.LENGTH_SHORT).show();
                options = new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ITALIAN)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build();
                break;
                //return true;
//            default:
//                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
