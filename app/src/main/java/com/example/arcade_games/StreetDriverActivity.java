package com.example.arcade_games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class StreetDriverActivity extends AppCompatActivity implements View.OnTouchListener,CallBackInterface {
    ConstraintLayout roadView;
    ConstraintLayout itemsLayout;
    FrameLayout carLayout;
    Guideline leftGuide, rightGuide;
    ValueAnimator backgroundAnmt;
    ConstraintLayout livesLayout;
    SoundPool soundFx;

    ImageView backgroundOne;
    ImageView backgroundTwo;
    ImageView playerCar;
    ImageView item;
    TextView tvPointsView,tvLives,tvLevelUp;

    Timer itemsTimer = new Timer();
    Timer collisionTimer= new Timer();
    Random rnd = new Random();
    long road_speed = 2000;
    int score = 0;
    int[] imgId;
    int screenWidth;
    int screenHeight;
    int numberOfItemsOnScreen = 2;
    int coinId;
    int lives=3;
    int level=1;
    int hitSound,levelUpSound,coinSound,gameOverSound;
    Boolean isRunning = true;
    TimerTask itemTask;

    ObjectAnimator textColorAnimation;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MusicPlayer.player.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_driver);
        initViews();
        initVars();
        initSounds();
        InitBackgroundAnimation();
        initTextAnimations();
        CreateCar();
        backgroundAnmt.start();

        startItems();
    }
    private void initViews() {
        itemsLayout = findViewById(R.id.ly_itemsLayout);
        playerCar= new  ImageView(StreetDriverActivity.this);
        leftGuide = findViewById(R.id.guide_left);
        rightGuide = findViewById(R.id.guide_right);
        roadView = findViewById(R.id.ly_road_bounds);
        carLayout = findViewById(R.id.ly_frameLayout);
        item = new ImageView(this);
        tvPointsView = findViewById(R.id.tv_points);
        tvLevelUp = findViewById(R.id.tv_levelup);
        backgroundOne = findViewById(R.id.background_one);
        backgroundTwo = findViewById(R.id.background_two);
        livesLayout = findViewById(R.id.ly_lives);
    }
    private void initVars() {
        //
        coinId = R.drawable.ic_coin_crown;
        //
        imgId = new int[]{R.drawable.ic_coin_crown, R.drawable.ic_green_car, R.drawable.ic_yellow_car};
        screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        lives = livesLayout.getChildCount();
    }
    private void initSounds() {
        MusicPlayer.musicPlayer(StreetDriverActivity.this,R.raw.street_race_music);
        soundFx = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        hitSound = soundFx.load(this,R.raw.st_hit_sound,1);
        levelUpSound = soundFx.load(this,R.raw.st_level_up,1);
        coinSound = soundFx.load(this,R.raw.coin_sound,1);
        gameOverSound = soundFx.load(this,R.raw.game_over_sound,1);
    }
    private void InitBackgroundAnimation() {
        backgroundAnmt = ValueAnimator.ofFloat(0.0f, 1.0f);
        backgroundAnmt.setRepeatCount(ValueAnimator.INFINITE);
        backgroundAnmt.setInterpolator(new LinearInterpolator()); //קצב ליניארי
        backgroundAnmt.setDuration(road_speed);
        backgroundAnmt.addUpdateListener(animation -> {
            final float progress = (float) animation.getAnimatedValue();
            final float height = backgroundOne.getHeight();
            final float translationY = height * progress;
            backgroundOne.setTranslationY(translationY);
            backgroundTwo.setTranslationY(translationY - height);
        });
    }
    private void initTextAnimations() {
        textColorAnimation = new ObjectAnimator();
        textColorAnimation.setPropertyName("TextColor");
        textColorAnimation.setIntValues(Color.RED,Color.YELLOW);
        textColorAnimation.setDuration(300);
        textColorAnimation.setRepeatCount(1);
        textColorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnimation.start();
    }
    private void CreateCar() {
        ConstraintLayout.LayoutParams carParms = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        playerCar.setLayoutParams(carParms);
        playerCar.setImageResource(R.drawable.ic_red_car);
        carLayout.addView(playerCar);

        playerCar.setX(screenWidth /2 - carParms.width/2);
        playerCar.setY(screenHeight / 1.5f);
    }

    private void startItems() {
        itemTask = new TimerTask() {
            @Override
            public void run() {
                CreateItems();
            }
        };
        itemsTimer = new Timer();
        itemsTimer.scheduleAtFixedRate(itemTask,0,road_speed/numberOfItemsOnScreen);

        roadView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (isRunning) {
            float maxCarRight = roadView.getRight();
            float maxCarLeft = roadView.getLeft();
            float dX = 0;
            switch (motionEvent.getActionMasked()) {

                case MotionEvent.ACTION_DOWN:
                    dX = playerCar.getX() - motionEvent.getRawX();
                    break;

                case MotionEvent.ACTION_MOVE:
                    float motionX = motionEvent.getRawX() + dX;
                    if (motionX + playerCar.getWidth() > maxCarRight || motionX < maxCarLeft)
                        return false;
                    playerCar.setX(motionEvent.getRawX() + dX);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public void CreateItems() {
        new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int randomObs = rnd.nextInt((imgId.length - 1) - 0 + 1);
                            ConstraintLayout.LayoutParams itemsParms = new ConstraintLayout.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT);

                            int min = (int) leftGuide.getX();
                            int max = (int)rightGuide.getX() - itemsParms.width;

                            //TODO:bybass min,max == 0 on first run
                            int randomX = min != 0?rnd.nextInt((max - min) + 1) + min:screenWidth / 2 - itemsParms.width / 2;

                            item = new ImageView(StreetDriverActivity.this);
                            item.setLayoutParams(itemsParms);
                            item.setImageResource(imgId[randomObs]);
                            item.setX(randomX);
                            item.setId(imgId[randomObs]);
                            itemsLayout.addView(item);


                            ObjectAnimator moveAnmt = ObjectAnimator.ofFloat(item, "y", -100f, screenHeight);
                            moveAnmt.setDuration(road_speed);

                            moveAnmt.start();

                            Collision c = new Collision(item, moveAnmt);
                            c.start();
                            Thread.sleep(1);
                        } catch (
                                InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();

    }

    @Override
    public void callBack(int viewId) {
        switch (viewId){
            case R.id.btn_tryAgain:
                Intent intent = new Intent(StreetDriverActivity.this, StreetDriverActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.btn_goBack:
                startActivity(new Intent(StreetDriverActivity.this, HomeActivity.class));
                break;
        }
    }

    public class Collision extends Thread {
        ImageView item;
        float carLeft,carRight,carTop,carBottom;
        float itemLeft,itemRight,itemTop, itemBottom;
        ObjectAnimator moveAnmt;

        public Collision(ImageView item, ObjectAnimator moveAnmt) {
            this.item = item;
            this.moveAnmt = moveAnmt;
            carTop =  playerCar.getY();
            carBottom = playerCar.getY() + playerCar.getHeight();

        }

        public void run() {
            CalculateColission();
        }

        private void CalculateColission() {
            collisionTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    carLeft = playerCar.getX();
                    carRight = playerCar.getX() + playerCar.getWidth();
                    itemLeft =  item.getX();
                    itemRight =  (item.getX() + item.getWidth());
                    itemBottom = item.getY() + item.getHeight();
                    itemTop = item.getY();

//                        Log.d("tag", "carTop" + carTop +" | carBottom"+ carBottom + " | itemBottom " + itemBottom + " | itemTop " + itemTop);
                    if (carTop <= itemBottom && carBottom >= itemTop) {
                        if ((!(carLeft > itemRight)) && (!(carRight < itemLeft))) {
                            Log.d("hit At", "car Left --> : " + carLeft + " | Item Right --> " + itemRight + " | carRight -->" + carRight + " | ItemLeft --> " + itemLeft);
                            RenderChanges(true,item);
                            this.cancel();
                        }
                    }
                    else if(itemBottom >= screenHeight){
                        Log.d("hit","Miss at" + itemBottom +" | screen " + screenHeight);
                        RenderChanges(false,item);
                        this.cancel();
                    }
                }
            }, 0, 1);
        }
        void RenderChanges(Boolean isHit, ImageView item){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Boolean isCoin = item.getId() == coinId;
                    moveAnmt.cancel();
                    itemsLayout.removeViewAt(0);
                    if(isHit && isCoin) {
                        soundFx.play(coinSound,1,1,1,0,1);
                        score += 2;
                        tvPointsView.setText(String.valueOf(score));
                        textColorAnimation.setTarget(tvPointsView);
                        textColorAnimation.start();
                    }
                    else if(isHit) {
                        soundFx.play(hitSound,1,1,1,0,1);
                        livesLayout.removeViewAt(lives-1);
                        lives--;
                    }

                    if(lives==0)
                    {
                        endGame();
                    }
                    if(isCoin && isHit && score % 10 == 0)
                        LevelUp();
                }
            });
        }
    }

    private void LevelUp() {
        soundFx.play(levelUpSound,1,1,1,0,1);
        level++;
        tvLevelUp.setText("Level " + level);
        textColorAnimation.setTarget(tvLevelUp);
        textColorAnimation.start();

        if(backgroundAnmt.getDuration()>500)
            backgroundAnmt.setDuration(backgroundAnmt.getDuration() - 200);

        if(level % 3 == 0 && numberOfItemsOnScreen <= 4) {
            Log.d("tag", "" + numberOfItemsOnScreen);
            numberOfItemsOnScreen++;
            itemsTimer.cancel();
            startItems();
        }
    }

    private void endGame() {
        MusicPlayer.player.stop();
        backgroundAnmt.pause();
        itemsTimer.cancel();
        collisionTimer.cancel();
        itemsLayout.setVisibility(View.GONE);
        isRunning = false;
        soundFx.play(gameOverSound,1,1,1,0,1);
        showGameOver();

    }

    private void showGameOver() {
        Bundle bundle = new Bundle();
        bundle.putString("headline", "Game Over!");
        bundle.putString("subText", "You've Reached Level: " + String.valueOf(level));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, EndGameFragment.class, bundle)
                .commit();
    }
}