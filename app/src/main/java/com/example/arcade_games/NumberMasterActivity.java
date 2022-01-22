package com.example.arcade_games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class NumberMasterActivity extends AppCompatActivity implements View.OnClickListener,CallBackInterface {
    //TODO: fix double click
    ImageView playerImg, enemyImg;
    Button goBtn;
    EditText guessEt;
    TextView levelTv, numberRangeTv;
    SoundPool soundFx;
    int punchSound, playerHitSOund, enemyHitSound, dieSound, gameOverSound, gameWinSound;

    ConstraintLayout enemyLy;
    ProgressBar livesBar;

    AnimationDrawable playerAnmt;
    AnimationDrawable enemyAnmt;
    Animation enemyMove;

    int[] enemyHitsDrawables = {R.drawable.en1_hit_anm, R.drawable.en2_hit_anm, R.drawable.en3_hit_anm};
    int[] enemyAttackDrawables = {R.drawable.en1_attack_anm, R.drawable.en2_attack_anm, R.drawable.en3_attack_anm};
    int[] enemyWalkDrawables = {R.drawable.en1_walk_anm, R.drawable.en2_walk_anm, R.drawable.en3_walk_anm};
    int anmIndex = 0;

    Random rnd = new Random();
    Integer drawnNumber;
    int maxGameNumber;
    int level;
    int livesOfLevel;
    int livesLeft;

    int screenWidth;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MusicPlayer.player.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_master);

        initViews();
        initVars();
        initPlayerAnimation();
        initValues();
        initSounds();
        goBtn.setOnClickListener(this);

        drawRandomNumber();
        CreateNewEnemy();
        enemyAnmt.start();


    }
    private void initViews() {
        numberRangeTv = findViewById(R.id.tv_numberRange);
        levelTv = findViewById(R.id.tv_level);
        livesBar = findViewById(R.id.pb_livesBar);
        goBtn = findViewById(R.id.btn_go);
        guessEt = findViewById(R.id.et_guess);
        playerImg = findViewById(R.id.img_player);
        enemyLy = findViewById(R.id.ly_enemy);
    }
    private void initVars() {
        screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        level = 1;
        maxGameNumber = 10;
        livesOfLevel = 7;
        livesLeft = livesOfLevel;


    }
    private void initPlayerAnimation() {
        playerImg.setImageResource(R.drawable.player_attack_anm);
        playerAnmt = (AnimationDrawable) playerImg.getDrawable();
    }
    private void initValues() {
        levelTv.setText("Level " + level);
        livesBar.setMax(livesOfLevel);
        livesBar.setProgress(livesLeft);
        numberRangeTv.setText("Range: 1 - " + String.valueOf(maxGameNumber));
    }
    private void initSounds() {
        MusicPlayer.musicPlayer(NumberMasterActivity.this,R.raw.number_master_music);
        soundFx = new SoundPool(7, AudioManager.STREAM_MUSIC, 0);
        punchSound = soundFx.load(this, R.raw.punch_sound, 1);
        playerHitSOund = soundFx.load(this, R.raw.playerhit_sound, 1);
        dieSound = soundFx.load(this, R.raw.die_sound, 1);
        enemyHitSound = soundFx.load(this, R.raw.hit_sound, 1);
        gameOverSound = soundFx.load(this, R.raw.game_over_sound, 1);
        gameWinSound = soundFx.load(this, R.raw.game_win_sound, 1);
    }
    private void drawRandomNumber() {
        drawnNumber = rnd.nextInt(maxGameNumber) + 1;
    }
    private void CreateNewEnemy() {
        enemyImg = new ImageView(this);
        enemyLy.addView(enemyImg);
        enemyLy.setMinWidth(200);
        enemyImg.setImageResource(enemyWalkDrawables[anmIndex]);
        enemyAnmt = (AnimationDrawable) enemyImg.getDrawable();
        enemyMove = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enemy_slide_right_anm);
        enemyLy.startAnimation(enemyMove);

        enemyMove.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                goBtn.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                enemyAnmt.stop();
                goBtn.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        playerAnmt.stop();
        enemyAnmt.stop();
        if (guessEt.getText().toString().isEmpty()) {
            Toast.makeText(this,"Number: " + drawnNumber,Toast.LENGTH_SHORT).show(); //TODO: TO DELETE!!! JUST FOR TESTING
            return;
        }
        int userGuess = Integer.parseInt(guessEt.getText().toString());

        if (userGuess == drawnNumber) {
            userRightGuess();
            playerAnmt.start();
            enemyAnmt.start();
        } else {
            userWrongGuess();
            if (livesLeft == 0) {

            }
        }
    }

    private void userRightGuess() {
        startGuessAnimation(R.drawable.player_attack_anm, enemyHitsDrawables[anmIndex]);
        playSoundEffect(enemyHitSound);
        enemyImg.animate().alpha(0).setDuration(800).setStartDelay(400);
        if (level < 5) {
            setNextLevel();
        } else {
            MusicPlayer.player.stop();
            soundFx.play(gameWinSound, 1, 1, 1, 0, 1);
            goBtn.setClickable(false);
            endGame("You Win!", "Good Job Try Again!");
        }
    }
    private void userWrongGuess() {
        livesLeft--;
        livesBar.setProgress(livesLeft);

        if (livesLeft == 0) {
            MusicPlayer.player.stop();
            startGuessAnimation(R.drawable.player_die_anmt, enemyAttackDrawables[anmIndex]);
            playSoundEffect(dieSound);
            soundFx.play(gameOverSound, 1, 1, 1, 0, 1);
            endGame("Game Over!", "Better Luck Next Time...");
        } else {
            startGuessAnimation(R.drawable.player_hit_anm, enemyAttackDrawables[anmIndex]);
            playSoundEffect(playerHitSOund);
        }
    }

    private void playSoundEffect(int id) {
        soundFx.play(id, 1, 1, 1, 0, 1);
        soundFx.play(punchSound, 1, 1, 1, 0, 1);
    }

    private void endGame(String headline, String subText) {
        goBtn.setClickable(false);
        Bundle bundle = new Bundle();
        bundle.putString("headline", headline);
        bundle.putString("subText", subText);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, EndGameFragment.class, bundle)
                .commit();
    }


    private void setNextLevel() {
        level++;
        maxGameNumber += 5;
        livesOfLevel -= 1;
        livesLeft = livesOfLevel;
        initValues();
        drawRandomNumber();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enemyLy.removeViewAt(0);
                if (level % 2 != 0)
                    anmIndex++;
                CreateNewEnemy();
                enemyAnmt.start();
            }
        }, 2000);
    }

    private void startGuessAnimation(int playerAnm, int enemyAnm) {
        playerImg.setImageResource(playerAnm);
        playerAnmt = (AnimationDrawable) playerImg.getDrawable();

        enemyImg.setImageResource(enemyAnm);
        enemyAnmt = (AnimationDrawable) enemyImg.getDrawable();

        playerAnmt.start();
        enemyAnmt.start();
    }

    @Override
    public void callBack(int viewId) {
       switch (viewId){
           case R.id.btn_tryAgain:
                    Intent intent = new Intent(NumberMasterActivity.this, NumberMasterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;

           case R.id.btn_goBack:
               startActivity(new Intent(NumberMasterActivity.this, HomeActivity.class));
               break;
       }
    }
}