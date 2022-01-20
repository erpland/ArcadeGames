package com.example.arcade_games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView mario;
    Button loginBtn;
    TextView toSignupTv;
    EditText emailEt,passwordEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getSupportActionBar().hide();

        initViews();
        initAnimations();

        loginBtn.setOnClickListener(this);
        toSignupTv.setOnClickListener(this);




    }
    private void initViews() {
        mario = findViewById(R.id.marioBtm);
        loginBtn = findViewById(R.id.btn_Login);
        toSignupTv = findViewById(R.id.createAccount);
        emailEt = findViewById(R.id.et_loginEmail);
        passwordEt = findViewById(R.id.et_loginPassword);
    }

    private void initAnimations() {
        mario.setImageResource(R.drawable.mario_steps_anm);
        AnimationDrawable marioSteps = (AnimationDrawable) mario.getDrawable();
        Animation marioMove = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mario_move_right_anm);
        marioSteps.start();
        mario.startAnimation(marioMove);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_Login:
                LoginAction();
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                break;
            case R.id.createAccount:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
        }
    }

    private void LoginAction() {
    }
}