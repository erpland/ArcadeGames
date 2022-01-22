package com.example.arcade_games;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

class MusicPlayer {

    public static MediaPlayer player;

    public static void musicPlayer(Context context,int music) {
//        if (player!=null && player.isPlaying())
//            player.stop();
        player = MediaPlayer.create(context,music);
        player.setLooping(false); // Set looping
        player.setVolume(100, 100);

        //player.release();
        player.start();
    }
}
