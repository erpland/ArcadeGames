package com.example.arcade_games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class SevenBoomActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout gameView;
    ImageView mario;
    ImageView box, coin;
    ObjectAnimator boxAnmt;
    ObjectAnimator numCountAnmt;
    ObjectAnimator marioAnmt;
    AnimationDrawable marioJumpanmt;
    Animation boomNumanmt;
    AnimatorSet coinSet;
    ObjectAnimator coinAnmtX;
    ObjectAnimator coinAnmtY;

    TextView points, drawnNumber, boomNumber;

    MediaPlayer music;
    SoundPool soundFx;
    int jumpSound,coinSound;

    int count = 0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MusicPlayer.player.stop();
        startActivity(new Intent(SevenBoomActivity.this,HomeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seven_boom);
        initViews();
        initTextColorAnimation();
        initSounds();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initMarioAnimation();
                initBoxAnimation();
                initCoinAnimation();
            }
        }, 100);

        gameView.setOnClickListener(this);

    }



    private void initViews() {
        gameView = findViewById(R.id.v_game);
        mario = findViewById(R.id.img_mario);
        box = findViewById(R.id.img_box);
        coin = findViewById(R.id.img_coin);
        points = findViewById(R.id.tv_points);
        drawnNumber = findViewById(R.id.tv_number);
        boomNumber = findViewById(R.id.tv_boomNumber);
    }
    private void initMarioAnimation() {
        marioAnmt = ObjectAnimator.ofFloat(mario, "y", box.getBottom());
        marioAnmt.setDuration(200);
        marioAnmt.setRepeatCount(1);
        marioAnmt.setRepeatMode(ObjectAnimator.REVERSE);
        marioAnmt.setInterpolator(new AccelerateDecelerateInterpolator());

    }
    private void initBoxAnimation() {
        Log.d("tag", "boxAnimation inside: " + box.getTop());
        boxAnmt = ObjectAnimator.ofFloat(box, "y", box.getTop() - 10);
        boxAnmt.setDuration(100);
        boxAnmt.setRepeatMode(ObjectAnimator.REVERSE);
        boxAnmt.setRepeatCount(1);
        boxAnmt.setStartDelay(0);
        boxAnmt.setInterpolator(new AccelerateDecelerateInterpolator());
    }
    private void initCoinAnimation() {
        coinAnmtX = ObjectAnimator.ofFloat(coin, "x", points.getRight());
        coinAnmtX.setRepeatMode(ObjectAnimator.RESTART);
        coinAnmtY = ObjectAnimator.ofFloat(coin, "y", points.getBottom());
        coinAnmtX.setDuration(500);
        coinAnmtX.setRepeatMode(ObjectAnimator.RESTART);


        coinSet = new AnimatorSet();

        coinAnmtX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                coin.setAlpha(0.0f);
                coin.setY(box.getTop());
                coin.setX(box.getTop() - (box.getWidth() / 2));
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                coin.setAlpha(1.0f);
            }
        });
    }
    private void initSounds() {
        MusicPlayer.musicPlayer(SevenBoomActivity.this,R.raw.seven_boom_music);
        soundFx = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        jumpSound = soundFx.load(this,R.raw.mario_jump_sound,1);
        coinSound = soundFx.load(this,R.raw.coin_sound,1);
    }
    @Override
    public void onClick(View v) {
        if (mario.getTranslationY() != 0.0f)
            return;

        count++;
        drawnNumber.setText(String.valueOf(count));
        numCountAnmt.start();

        soundFx.play(jumpSound,1,1,1,0,1);

        setMarioJumpDrawable();
        marioJumpanmt.start();
        marioAnmt.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boxAnmt.start();
                if (String.valueOf(count).contains("7") || count % 7 == 0) {
                    soundFx.play(coinSound,1,1,1,0,1);
                    boomNumber.setText("+" + count);
                    coinSet.playTogether(coinAnmtX, coinAnmtY);
                    coinSet.start();
                    boomNumber.startAnimation(boomNumanmt);
                    int temp = Integer.parseInt(points.getText().toString());
                    points.setText(String.valueOf(temp + count));

                }
            }
        }, 200);

    }

    private void setMarioJumpDrawable() {
        mario.setImageResource(R.drawable.mario_jump);
        marioJumpanmt = (AnimationDrawable) mario.getDrawable();
        marioJumpanmt.setOneShot(true);
    }

    private void initTextColorAnimation() {
        Integer goldColor = getResources().getColor(R.color.gold_text);
        numCountAnmt = ObjectAnimator.ofInt(drawnNumber, "textColor", goldColor, Color.BLACK);
        numCountAnmt.setEvaluator(new ArgbEvaluator());
        numCountAnmt.setDuration(500);

        boomNumanmt = new AlphaAnimation(1.0f, 0.0f);
        boomNumanmt.setDuration(1000);
        boomNumanmt.setStartOffset(100);
        boomNumanmt.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                boomNumber.setAlpha(1.0f);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boomNumber.setAlpha(0.0f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}