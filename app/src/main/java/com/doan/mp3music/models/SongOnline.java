package com.doan.mp3music.models;

import android.net.Uri;

import com.doan.mp3music.api.ApiBuilder;
import com.google.gson.annotations.SerializedName;

public class SongOnline extends Song {

    private String image;
    @SerializedName("artist_name")
    private String artistName;
    @SerializedName("album_name")
    private String albumName;
    private int views;
    private boolean isFavorite;

    public String getImage() {
        return ApiBuilder.URL + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public Uri getContentUri() {
        return null;
    }

    @Override
    public String getData() {
        return ApiBuilder.URL +   super.getData();
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getViews() {
        return views;
    }
}
