package com.byteattackers.byteattackers;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AudioActivity extends AppCompatActivity {
    private List<AudioBook> audios = new ArrayList<AudioBook>();
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a Cloud Storage reference from the app
        StorageReference storageRef = storage.getReference();
//        StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");
        mRecyclerView = findViewById(R.id.recyclerView);


        StorageReference listRef = storage.getReference().child("mp3s");
        Log.d(TAG, "listAll onCreate: " + listRef.getName());
        listRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference prefix : listResult.getPrefixes()) {
                        Log.d(TAG, "listAll prefix onSuccess: " + prefix.getName());
                    }

                    for (StorageReference item : listResult.getItems()) {
                        Log.d(TAG, "listAll item onSuccess: " + item.getName());
                        Log.d(TAG, "listAll item onSuccess: " + item.getDownloadUrl());
                        AudioBook audioBook = new AudioBook();
                        audioBook.setFilename(item.getName());
                        audioBook.setFiletype("PDF");
                        audioBook.setUrl(item.getDownloadUrl());
                        audios.add(audioBook);
                    }

                    new AudioRecyclerView().setConfig(mRecyclerView, AudioActivity.this, audios);
                })
                .addOnFailureListener(e -> Log.d(TAG, "listAll onFailure: "));
    }
}
