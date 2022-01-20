package com.example.arcade_games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button signupBtn;
    TextView toLoginTv;
    EditText emailEt,passwordEt,nicknameEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();

        signupBtn.setOnClickListener(this);
        toLoginTv.setOnClickListener(this);
    }

    private void initViews() {
        signupBtn = findViewById(R.id.btn_signup);
        toLoginTv = findViewById(R.id.tv_toLogin);
        emailEt = findViewById(R.id.et_signupEmail);
        passwordEt = findViewById(R.id.et_loginPassword);
        nicknameEt = findViewById(R.id.et_signupNickname);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signup:
                registerAction();
                break;

            case R.id.tv_toLogin:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;
        }

    }

    private void registerAction() {
    }
}