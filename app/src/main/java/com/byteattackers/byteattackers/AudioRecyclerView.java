package com.byteattackers.byteattackers;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;

public class AudioRecyclerView {
    private Context mContext;
    private AudioRecyclerView.AudioAdapter mCompanyAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<AudioBook> audio){
        mContext = context;
        mCompanyAdapter = new AudioRecyclerView.AudioAdapter(audio);
        mCompanyAdapter.setHasStableIds(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new AudioRecyclerView.AudioAdapter(audio));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    class AudioItemView extends RecyclerView.ViewHolder{

        private TextView companyName;
        private TextView companyTicker;
        private MaterialButton buttonPlay;
        private RelativeLayout backgroundCompany;


        public AudioItemView(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.audio_element, parent, false));

            companyName = itemView.findViewById(R.id.companyName);
            companyTicker = itemView.findViewById(R.id.companyTicker);
            backgroundCompany = itemView.findViewById(R.id.backgroundCompany);
            buttonPlay = itemView.findViewById(R.id.buttonPlay);
        }
        public void bind(AudioBook company){
            companyName.setText(company.getFilename());
            companyName.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            companyTicker.setText(company.getFiletype());
            MediaPlayer mediaPlayer = new MediaPlayer();
            buttonPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        buttonPlay.setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_round_play_arrow_24));
                        return;
                    }
                    Uri myUri = company.getUrl().getResult();
                    mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    );
                    try {
                        mediaPlayer.setDataSource(mContext, myUri);
                        mediaPlayer.prepare(); // might take long! (for buffering, etc)
                        mediaPlayer.start();
                        Log.d(TAG, "onClick: tryin to play");
                        buttonPlay.setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_round_pause_24));
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "onClick: error :(", e);
                    }
                }
            });
        }
    }

    class AudioAdapter extends RecyclerView.Adapter<AudioItemView>{
        private List<AudioBook> mAudioList;

        public AudioAdapter(List<AudioBook> mAudioList) {
            this.mAudioList = mAudioList;
        }

        @NonNull
        @Override
        public AudioItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AudioItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull AudioItemView holder, int position) {
            holder.bind(mAudioList.get(position));
        }

        @Override
        public long getItemId(int position) {
            if (position < mAudioList.size()){
                mAudioList.get(position).getFilename();
            }
            return RecyclerView.NO_ID;
        }

        @Override
        public int getItemCount() {
            return mAudioList.size();
        }
    }
}
