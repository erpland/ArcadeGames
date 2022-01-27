package com.example.arcade_games;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

class MusicPlayer {

    public static MediaPlayer player;

    public static void musicPlayer(Context context, int music) {
        if(player == null || !player.isPlaying()) {
            player = MediaPlayer.create(context, music);
            player.setLooping(true);
            player.setVolume(100, 100);
            player.start();
        }
    }
}
