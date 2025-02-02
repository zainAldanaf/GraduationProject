package com.example.GraduationProject;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class INFO_Page extends AppCompatActivity {

    PlayerView playerView;
    ExoPlayer player;
    ImageView topicImage;
    String imageUrl;

    int currentWindow = 0;
    boolean playWhenReady = true;
    long playBackPosition = 0;
    String videoPath = "video.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.d_one), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        playerView = findViewById(R.id.info_playerview);

        loadVideoFromFirebase();
    }

    private void loadVideoFromFirebase() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference videoRef = storage.getReference().child(videoPath);

        videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
            initializePlayer(uri);
        }).addOnFailureListener(e -> {
            // Handle error (e.g., show a message or a default image)
        });
    }

    private void initializePlayer(Uri videoUri) {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        player.setMediaItem(mediaItem);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playBackPosition);
        player.prepare();
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playBackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadVideoFromFirebase();  // Reload video when activity starts
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }}