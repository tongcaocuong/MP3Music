package com.doan.mp3music.ui.play;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.SeekBar;

import com.doan.mp3music.R;
import com.doan.mp3music.databinding.ActivityPlayBinding;
import com.doan.mp3music.models.Song;
import com.doan.mp3music.service.MP3Service;
import com.doan.mp3music.ui.base.BaseActivity;
import com.doan.mp3music.ui.base.BaseViewModel;
import com.doan.mp3music.ui.screen.MediaListener;

public class PlayActivity extends BaseActivity<ActivityPlayBinding, BaseViewModel> implements SeekBar.OnSeekBarChangeListener, MediaListener<Song> {

    private MP3Service service;
    private PlayListDialog dialog;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            MP3Service.MP3Binder mp3Binder = (MP3Service.MP3Binder) binder;
            service = mp3Binder.getService();
            service.getLiveController().observe(PlayActivity.this, mediaController -> {
                binding.setController(mediaController);
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected Class<BaseViewModel> getViewModelClass() {
        return BaseViewModel.class;
    }

    @Override
    protected void init() {
        Intent intent = new Intent(this,
                MP3Service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        binding.sbTime.setOnSeekBarChangeListener(this);
        binding.tvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = PlayListDialog.newInstance(service, PlayActivity.this);
                dialog.show(getSupportFragmentManager(), null);
            }
        });
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            binding.getController().seek(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onItemMediaClicked(Song item) {
        dialog.dismiss();
        service.getController().create(service.getController().getSongs().indexOf(item));
    }
}
