package com.example.arcade_games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    CardView sevenBoomCrd,numberMasterCrd,turboRacerCrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sevenBoomCrd = findViewById(R.id.card_sevenBoom);
        numberMasterCrd = findViewById(R.id.card_NumbeMaster);
        turboRacerCrd = findViewById(R.id.card_turboRacer);

        sevenBoomCrd.setOnClickListener(this);
        numberMasterCrd.setOnClickListener(this);
        turboRacerCrd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_sevenBoom:

                break;
            case R.id.card_NumbeMaster:

                break;
            case R.id.card_turboRacer:

                break;
        }
    }
}