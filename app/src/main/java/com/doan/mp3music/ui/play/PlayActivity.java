package com.doan.mp3music.ui.play;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.doan.mp3music.R;
import com.doan.mp3music.api.ApiBuilder;
import com.doan.mp3music.databinding.ActivityPlayBinding;
import com.doan.mp3music.models.Song;
import com.doan.mp3music.models.SongOnline;
import com.doan.mp3music.service.MP3Service;
import com.doan.mp3music.ui.base.BaseActivity;
import com.doan.mp3music.ui.base.BaseViewModel;
import com.doan.mp3music.ui.screen.MediaListener;
import com.doan.mp3music.ui.screen.main.MainActivity;
import com.doan.mp3music.utils.Dialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayActivity extends BaseActivity<ActivityPlayBinding, BaseViewModel> implements SeekBar.OnSeekBarChangeListener, MediaListener<Song> {
    private String device;
    private MP3Service service;
    private PlayListDialog dialog;
    private int currentId;
    private boolean isFavorited = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            MP3Service.MP3Binder mp3Binder = (MP3Service.MP3Binder) binder;
            service = mp3Binder.getService();
            service.getLiveController().observe(PlayActivity.this, mediaController -> {
                binding.setController(mediaController);
                if (service.getController().isLooping()) {
                    binding.imLoop.setColorFilter(getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    binding.imLoop.setColorFilter(getColor(android.R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                if (!MainActivity.isVip) {
                    binding.imDownload.setVisibility(View.GONE);
                }
                if ((mediaController.getSong() instanceof SongOnline)) {
                    int id = mediaController.getSong().getId();
                    if (id != currentId) {
                        currentId = id;
                        checkFavorite();
                    }
                } else {
                    binding.imFavorite.setVisibility(View.GONE);
                    binding.imDownload.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void checkFavorite() {
        ApiBuilder.getInstance().checkFavorite(device, currentId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.code() == 200) {
                    binding.imFavorite.setImageResource(R.drawable.ic_favorited);
                    isFavorited = true;
                } else {
                    binding.imFavorite.setImageResource(R.drawable.ic_unfavorite);
                    isFavorited = false;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    protected Class<BaseViewModel> getViewModelClass() {
        return BaseViewModel.class;
    }

    @Override
    protected void init() {
        device = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Intent intent = new Intent(this,
                MP3Service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        binding.sbTime.setOnSeekBarChangeListener(this);
        binding.tvList.setOnClickListener(view -> {
            dialog = PlayListDialog.newInstance(service, PlayActivity.this);
            dialog.show(getSupportFragmentManager(), null);
        });
        binding.imLoop.setOnClickListener(v -> {
            service.getController().loop(!service.getController().isLooping());
        });
        binding.imBack.setOnClickListener(view -> finish());
        binding.imFavorite.setOnClickListener(v -> {
            Call<ResponseBody> call;
            if (isFavorited) {
                call = ApiBuilder.getInstance().removeFavorite(device, currentId);
            } else {
                call = ApiBuilder.getInstance().addFavorite(device, currentId);
            }
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        checkFavorite();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        });
        binding.imDownload.setOnClickListener(v -> {
            Dialog.showDialog(this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath(), "MP3Music");
                        f.mkdirs();
                        String link = service.getController().getSong().getData();
                        URL url = new URL(link);
                        String name = link.split("/")[link.split("/").length -1];
                        URLConnection connection = url.openConnection();
                        InputStream stream = connection.getInputStream();
                        byte[] b = new byte[1024];
                        int count  = stream.read(b);
                        File fileDownload = new File(f, name);
                        FileOutputStream out = new FileOutputStream(fileDownload);
                        while (count > 0) {
                            out.write(b, 0, count);
                            count = stream.read(b);
                        }
                        stream.close();
                        out.close();
                        MediaScannerConnection.scanFile(PlayActivity.this, new String[] {
                                fileDownload.getAbsolutePath()
                        }, null, new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.e(getClass().getName(), path);
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Dialog.dismiss();
                            }
                        });
                    }
                }
            }).start();

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

    @Override
    public void onUnFavoriteClicked(Song item) {

    }
}
