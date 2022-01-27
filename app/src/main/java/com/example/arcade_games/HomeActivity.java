package com.example.arcade_games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;

    boolean isFirstRun = true;
    Button logoutBtn;
    CardView sevenBoomCrd, numberMasterCrd, turboRacerCrd;
    TextView userGreetTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        initVars();
        greetUser();
        sevenBoomCrd.setOnClickListener(this);
        numberMasterCrd.setOnClickListener(this);
        turboRacerCrd.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();

    }

    private void greetUser() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                userGreetTv.setText("Welcome, " + value.getNickname());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void initViews() {
        sevenBoomCrd = findViewById(R.id.card_sevenBoom);
        numberMasterCrd = findViewById(R.id.card_NumbeMaster);
        turboRacerCrd = findViewById(R.id.card_turboRacer);
        userGreetTv = findViewById(R.id.tv_userGreet);
        logoutBtn = findViewById(R.id.btn_logout);
    }

    private void initVars() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(mAuth.getUid());
    }


    @Override
    public void onClick(View v) {
        MusicPlayer.player.pause();
        switch (v.getId()) {
            case R.id.card_sevenBoom:
                startActivity(new Intent(HomeActivity.this, SevenBoomActivity.class));
                break;
            case R.id.card_NumbeMaster:
                startActivity(new Intent(HomeActivity.this, NumberMasterActivity.class));
                break;
            case R.id.card_turboRacer:
                startActivity(new Intent(HomeActivity.this, StreetDriverActivity.class));
                break;
            case R.id.btn_logout:
                if (mAuth != null)
                    mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
//        if(!MusicPlayer.player.isPlaying())
        MusicPlayer.musicPlayer(HomeActivity.this, R.raw.main_music);
//    }
    }
}