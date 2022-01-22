package com.example.arcade_games;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button signupBtn;
    TextView toLoginTv;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText emailEt,passwordEt,nicknameEt,phoneNumberET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initVars();

        signupBtn.setOnClickListener(this);
        toLoginTv.setOnClickListener(this);
    }

    private void initVars() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
    }

    private void initViews() {
        signupBtn = findViewById(R.id.btn_signup);
        toLoginTv = findViewById(R.id.tv_toLogin);
        emailEt = findViewById(R.id.et_signupEmail);
        passwordEt = findViewById(R.id.et_signupPassword);
        nicknameEt = findViewById(R.id.et_signupNickname);
        phoneNumberET = findViewById(R.id.et_signupPhoneNum);

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
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        String nickname = nicknameEt.getText().toString();
        String phoneNumber = phoneNumberET.getText().toString();

        //בדיקה האמייל והסיסמא לא ריקים
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(RegisterActivity.this, "All Fields Are Requierd!", Toast.LENGTH_SHORT).show();
            return;
        }
        //ניסיון ליצירת משתמש תקין ובדיקת תקינות
        User user = new User(nickname,phoneNumber,RegisterActivity.this);
        if(nickname != user.getNickname() || phoneNumber != user.getPhoneNumber())
            return;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
                            myRef.child(mAuth.getUid()).setValue(user);
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("tag",(task.getException().toString()));
                            if(task.getException().toString().contains("email") || task.getException().toString().contains("least 6")){
                                Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(RegisterActivity.this, "Registeration Failed....",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
}