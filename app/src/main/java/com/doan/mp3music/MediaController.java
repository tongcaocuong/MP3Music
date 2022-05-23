package com.doan.mp3music;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.doan.mp3music.api.ApiBuilder;
import com.doan.mp3music.models.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaController implements MediaPlayer.OnCompletionListener {
    private List<Song> songs;
    private Context context;
    private MediaPlayer player;
    public int index;

    public MediaController(List<Song> songs, Context context) {
        this.songs = songs;
        this.context = context;
    }

    public void create(int index) {
        release();
        this.index = index;
        String data = songs.get(index).getData();
        if (!data.startsWith("http")) {
            Uri uri = Uri.parse(songs.get(index).getData());
            player = MediaPlayer.create(context, uri);
            start();
            player.setOnCompletionListener(this);
        } else {
            player = new MediaPlayer();
            try {
                player.setDataSource(data);
                player.prepareAsync();
                player.setOnPreparedListener(mediaPlayer -> {
                    ApiBuilder.getInstance().play(songs.get(index).getId()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e(getClass().getName(), t.getMessage());
                        }
                    });
                    player.start();
                    player.setOnCompletionListener(MediaController.this);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        if (player != null) {
            player.start();
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    public void pause() {
        if (player != null) {
            player.pause();
        }
    }

    public void release() {
        if (player != null) {
            player.release();
        }
    }

    public void loop(boolean isLooping) {
        if (player != null) {
            player.setLooping(isLooping);
        }
    }

    public boolean isLooping() {
        if (player!= null) {
            return player.isLooping();
        }
        return false;
    }

    public void seek(int position) {
        if (player != null) {
            player.seekTo(position);
        }
    }

    public String getSongName() {
        return songs.get(index).getTitle();
    }

    public boolean isPlaying() {
        return player == null ? false : player.isPlaying();
    }

    public int getDuration() {
        return player == null ? 0 : player.getDuration();
    }

    public int getPosition() {
        return player == null ? 0 : player.getCurrentPosition();
    }

    // 1: next
    // -1: prev
    public void change(int value) {
        index += value;
        if (index < 0) {
            index = songs.size() - 1;
        } else if (index >= songs.size()) {
            index = 0;
        }
        create(index);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        change(1);
    }

    public Song getSong() {
        return  songs.get(index);
    }

    public List<Song> getSongs() {
        return songs;
    }
}
