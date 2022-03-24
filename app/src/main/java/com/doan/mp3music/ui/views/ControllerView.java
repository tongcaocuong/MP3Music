package com.doan.mp3music.ui.views;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.doan.mp3music.MediaController;
import com.doan.mp3music.databinding.UiControlBinding;
import com.doan.mp3music.service.MP3Service;
import com.doan.mp3music.ui.play.PlayActivity;

public class ControllerView extends FrameLayout implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private UiControlBinding binding;
    private MediaController mediaController;

    public ControllerView(@NonNull Context context) {
        super(context);
        init();
    }

    public ControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        binding = UiControlBinding.inflate(
                LayoutInflater.from(getContext()),
                this,
                true
        );
        setVisibility(GONE);
        binding.sbTime.setOnSeekBarChangeListener(this);
        setOnClickListener(this);
    }

    public void setService(MP3Service service) {
        service.getLiveController().observe((FragmentActivity) getContext(), new Observer<MediaController>() {

            @Override
            public void onChanged(MediaController mediaController) {
                ControllerView.this.mediaController = mediaController;
                if (mediaController == null) {
                    setVisibility(GONE);
                } else {
                    setVisibility(VISIBLE);
                }
                binding.setController(mediaController);
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mediaController.seek(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), PlayActivity.class);
        getContext().startActivity(intent);
    }
}
