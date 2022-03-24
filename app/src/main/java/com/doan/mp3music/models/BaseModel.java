package com.doan.mp3music.models;

import android.net.Uri;

import java.io.Serializable;

public abstract class BaseModel implements Serializable {
    public abstract Uri getContentUri();
}
