package com.doan.mp3music.api;

import com.doan.mp3music.models.Album;
import com.doan.mp3music.models.Artist;
import com.doan.mp3music.models.SongOnline;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    @GET("song.php")
    Call<List<SongOnline>> getSong();

    @GET("top-view.php")
    Call<List<SongOnline>> getTopSong();

    @GET("artist.php")
    Call<List<Artist>> getArtist();

    @GET("album.php")
    Call<List<Album>> getAlbum();

    @GET("favorite.php")
    Call<List<SongOnline>> getFavorite(@Query("device") String device);

    @GET("check-favorite.php")
    Call<ResponseBody> checkFavorite(@Query("device") String device, @Query("id") int songId);

    @FormUrlEncoded
    @POST("add-favorite.php")
    Call<ResponseBody> addFavorite(@Field("device") String device, @Field("song_id") int songId);

    @FormUrlEncoded
    @POST("play.php")
    Call<ResponseBody> play(@Field("song_id") int songId);

    @FormUrlEncoded
    @POST("remove-favorite.php")
    Call<ResponseBody> removeFavorite(@Field("device") String device, @Field("song_id") int songId);

    @GET("check-subscription.php")
    Call<ResponseBody> checkSubscription(@Query("device") String device);

    @FormUrlEncoded
    @POST("add-subscription.php")
    Call<ResponseBody> addSubscription(@Field("device") String device);
}
