package com.example.arcade_games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView middleCreture, leftCreture, rightCreture;
    Button loginBtn, signupBtn;
    FirebaseAuth mAuth;
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayer.player.stop();
    }

    //TODO:music stop on minimaize
    //TODO:icon
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MusicPlayer.musicPlayer(MainActivity.this, R.raw.main_music);
        mAuth = FirebaseAuth.getInstance();

        InitViews();
        initAnimations();


        signupBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);


    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(         ) {
//            MusicPlayer.player.pause();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicPlayer.player.start();
    }

    private void initAnimations() {
        middleCreture.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.creture_center_anm));
        leftCreture.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.creture_left_anm));
        rightCreture.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.creture_right_anm));
    }

    private void InitViews() {
        middleCreture = findViewById(R.id.middleSvg);
        leftCreture = findViewById(R.id.leftSvg);
        rightCreture = findViewById(R.id.rightSvg);
        loginBtn = findViewById(R.id.vBtnLogin);
        signupBtn = findViewById(R.id.vBtnSignup);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null)
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
    }

    @Override
    public void onClick(View v) {
//        music.pause();
        switch (v.getId()) {
            case (R.id.vBtnLogin):
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case (R.id.vBtnSignup):
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;

        }
    }
}
