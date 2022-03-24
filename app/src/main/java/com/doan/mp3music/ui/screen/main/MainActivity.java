package com.doan.mp3music.ui.screen.main;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.doan.mp3music.R;
import com.doan.mp3music.databinding.ActivityMainBinding;
import com.doan.mp3music.service.MP3Service;
import com.doan.mp3music.ui.base.BaseActivity;
import com.doan.mp3music.ui.base.BaseFragment;
import com.doan.mp3music.ui.screen.favorite.FavoriteFragment;
import com.doan.mp3music.ui.screen.online.OnlineFragment;
import com.doan.mp3music.ui.screen.song.SongFragment;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements BottomNavigationView.OnNavigationItemSelectedListener {

    private OnlineFragment fmOnline = new OnlineFragment();
    private FavoriteFragment fmFavorite = new FavoriteFragment();
    private SongFragment fmMyMusic = new SongFragment();
    private MP3Service service;

    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected void init() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        requestPermission(permissions, new PermissionListener() {
            @Override
            public void onResult(boolean isGranted) {
                if (isGranted) {
                    binding.bottomNav.setOnItemSelectedListener(MainActivity.this);
                    showFragment(fmOnline);
                    Intent intent = new Intent(MainActivity.this,
                            MP3Service.class);
                    bindService(intent, connection, Context.BIND_AUTO_CREATE);
                } else {
                    finish();
                }
            }
        });
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            MP3Service.MP3Binder mp3Binder = (MP3Service.MP3Binder) binder;
            service = mp3Binder.getService();
            binding.controllerView.setService(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void showFragment(BaseFragment fmShow) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.container, fmShow);
        transaction.commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_online:
                showFragment(fmOnline);
                break;
            case R.id.nav_favorite:
                showFragment(fmFavorite);
                break;
            case R.id.nav_my_music:
                showFragment(fmMyMusic);
                break;
        }
        return true;
    }

    public MP3Service getService() {
        return service;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
