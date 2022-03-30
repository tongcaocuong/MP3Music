package com.doan.mp3music.models;

import android.net.Uri;

import com.doan.mp3music.api.ApiBuilder;

public class SongOnline extends Song {

    private String image;
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
}
