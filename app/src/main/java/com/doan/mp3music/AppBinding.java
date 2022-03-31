package com.doan.mp3music;

import android.net.Uri;
import android.text.format.Formatter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.doan.mp3music.api.ApiBuilder;
import com.doan.mp3music.models.Song;
import com.doan.mp3music.models.SongOnline;

public class AppBinding {

    @BindingAdapter("duration")
    public static void parseTime(TextView tv, int duration) {
        String str = String.format("%02d:%02d",
                (duration / 1000 % 3600) / 60, (duration / 1000 % 60));
        tv.setText(str);
    }

    @BindingAdapter("size")
    public static void parseSize(TextView tv, int size) {
        String str = Formatter.formatFileSize(tv.getContext(), size);
        tv.setText(str);
    }

    @BindingAdapter("thumb")
    public static void thumb(ImageView im, Song song) {
        if (song == null) return;
        if(song instanceof SongOnline) {
            Glide.with(im)
                    .load(((SongOnline) song).getImage())
                    .error(R.drawable.ic_album)
                    .into(im);
        } else {
            Uri uri = Uri
                    .parse("content://media/external/audio/albumart/" + song.getAlbumId());
            Glide.with(im)
                    .load(uri)
                    .error(R.drawable.ic_album)
                    .into(im);
        }
    }

    @BindingAdapter("image")
    public static void image(ImageView im, String image) {
        Glide.with(im)
                .load(image)
                .error(R.drawable.ic_artist)
                .into(im);
    }

}
