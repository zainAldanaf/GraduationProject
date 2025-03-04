package com.example.GraduationProject.View.Patients;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.GraduationProject.Controller.BookOppiontment;
import com.example.GraduationProject.R;

public class DetailsScreen extends AppCompatActivity {
   TextView title;
   TextView des;

   Button book_opptiontment;
   ImageView img;
    private RequestQueue requestQueue;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);
        title = findViewById(R.id.topic_title);
        des = findViewById(R.id.topic_des);
        book_opptiontment = findViewById(R.id.BOOK_oppintment);
        img = findViewById(R.id.topic_image);


        title.setText(getIntent().getStringExtra("title"));
        des.setText(getIntent().getStringExtra("content"));


        book_opptiontment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailsScreen.this, BookOppiontment.class));
            }
        });

    }}



//    public void initVideo(){
//        player=new SimpleExoPlayer.Builder(DetailsScreen.this).build();
//        playerView.setPlayer(player);
//
//        Uri uri=Uri.parse(link);
//        DataSource.Factory datasource=new DefaultDataSourceFactory(DetailsScreen.this,"video");
//
//        MediaSource mediaSource=new ProgressiveMediaSource.Factory(datasource).createMediaSource(MediaItem.fromUri(uri));
//        player.setPlayWhenReady(playWhenReady);
//        player.seekTo(currentwindow,playBackPosition);
//        player.prepare(mediaSource,false,false);
//    }
//
//    public void releaseVideo(){
//        if (player != null ){
//            playWhenReady = player.getPlayWhenReady();
//            playBackPosition=player.getCurrentPosition();
//            currentwindow=player.getCurrentWindowIndex();
//            player.release();
//            player=null;
//        }
//    }
//    @Override
//    protected void onStart() {
//        initVideo();
//        super.onStart();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        releaseVideo();
//
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        releaseVideo();
//    }
//}