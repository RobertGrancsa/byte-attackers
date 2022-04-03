package com.byteattackers.byteattackers;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import com.google.mlkit.vision.objects.defaults.PredefinedCategory;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.List;

public class ObjectDetectAcitivity extends Activity {
    private ObjectDetector objectDetector;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private TextView readText;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private ExtendedFloatingActionButton photoButton;
    private ImageLabeler labeler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        // Live detection and tracking
//        ObjectDetectorOptions options =
//                new ObjectDetectorOptions.Builder()
//                        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
//                        .enableClassification()  // Optional
//                        .build();

//        objectDetector = ObjectDetection.getClient(options);

        ImageLabelerOptions options =
             new ImageLabelerOptions.Builder()
                 .setConfidenceThreshold(0.7f)
                 .build();

        labeler = ImageLabeling.getClient(options);

        this.imageView = this.findViewById(R.id.imageView1);
        photoButton = this.findViewById(R.id.button1);
        readText = findViewById(R.id.readText);
        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
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
            }
            else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

            InputImage image = InputImage.fromBitmap(photo, 0);

            labeler.process(image)
                    .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                        @Override
                        public void onSuccess(List<ImageLabel> labels) {
//                            readText.setText(labels.get(0).getText());
//                            photoButton.shrink();
                            for (ImageLabel label : labels) {
                                String text = label.getText();
                                float confidence = label.getConfidence();
                                int index = label.getIndex();
                                Log.d(TAG, "object detected: " + text + confidence + index);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            // ...
                        }
                    });

//            objectDetector.process(image)
//                    .addOnSuccessListener(
//                            new OnSuccessListener<List<DetectedObject>>() {
//                                @Override
//                                public void onSuccess(List<DetectedObject> detectedObjects) {
//                                    for (DetectedObject detectedObject : detectedObjects) {
//                                        Rect boundingBox = detectedObject.getBoundingBox();
//                                        Integer trackingId = detectedObject.getTrackingId();
//                                        for (DetectedObject.Label label : detectedObject.getLabels()) {
//                                            String text = label.getText();
//                                            Log.d(TAG, "object_dectect: " + label.getText());
//                                            if (PredefinedCategory.FOOD.equals(text)) {
//
//                                            }
//                                            int index = label.getIndex();
//                                            if (PredefinedCategory.FOOD_INDEX == index) {
//
//                                            }
//                                            float confidence = label.getConfidence();
//                                        }
//                                    }
////                                    Log.d(TAG, "object_dectect: " + detectedObjects);
////                                    if (!detectedObjects.isEmpty()) {
////
////                                        List<DetectedObject.Label> label = detectedObjects.get(0).getLabels();
//////                                    label.get(0).getText();
////                                        readText.setText(label.get(0).getText());
////                                        photoButton.shrink();
////                                    }
//                                }
//                            })
//                    .addOnFailureListener(
//                            new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    // Task failed with an exception
//                                    // ...
//                                }
//                            });
        }
    }

}
