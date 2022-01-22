package com.example.arcade_games;

import android.content.Context;
import android.widget.Toast;

public class User {
    private String nickname,phoneNumber;
    Context context;
    public User(){}
    public User(String nickname, String phoneNumber, Context context) {
        this.context = context;
        setNickname(nickname);
        setPhoneNumber(phoneNumber);
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setNickname(String nickname) {
        if(nickname.isEmpty()){
            toast("All Fields Are Requierd");
            return;
        }
        if(nickname.length()<2 || nickname.length() > 10){
            toast("Nickname Must Be Between 2 And 10 Charcters");
            return;
        }
        this.nickname = nickname;
    }

    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber.isEmpty()){
            toast("All Fields Are Requierd");
            return;
        }
        if(phoneNumber.length() < 9 || phoneNumber.length() > 12){
            toast("Phone Number Must Be Between 9 And 10 Numbers");
        }
        this.phoneNumber = phoneNumber;
    }
    public void toast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
