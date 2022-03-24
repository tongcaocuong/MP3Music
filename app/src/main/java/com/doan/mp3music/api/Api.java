package com.doan.mp3music.api;

import com.doan.mp3music.models.SongOnline;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    @GET("song.php")
    Call<List<SongOnline>> getSong();
}
