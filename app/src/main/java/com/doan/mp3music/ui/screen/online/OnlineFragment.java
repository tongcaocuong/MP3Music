package com.doan.mp3music.ui.screen.online;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doan.mp3music.R;
import com.doan.mp3music.api.ApiBuilder;
import com.doan.mp3music.databinding.FragmentOnlineBinding;
import com.doan.mp3music.models.Song;
import com.doan.mp3music.models.SongOnline;
import com.doan.mp3music.ui.base.BaseBindingAdapter;
import com.doan.mp3music.ui.base.BaseFragment;
import com.doan.mp3music.ui.base.BaseViewModel;
import com.doan.mp3music.ui.screen.MediaListener;
import com.doan.mp3music.ui.screen.main.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineFragment extends BaseFragment<FragmentOnlineBinding, BaseViewModel> implements MediaListener<SongOnline> {

    private BaseBindingAdapter<SongOnline> adapter;
    private List<SongOnline> songs = new ArrayList<>();

    @Override
    protected Class<BaseViewModel> getViewModelClass() {
        return BaseViewModel.class;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_online;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new BaseBindingAdapter<>(
                R.layout.item_song_online, getLayoutInflater());
        binding.setAdapter(adapter);
        adapter.setListener(this);
        adapter.setData(songs);
        getActivity().setTitle("Song");
    }

    public void setSongs(List<SongOnline> songs) {
        this.songs = songs;
    }

    @Override
    public void onItemMediaClicked(SongOnline item) {
        ArrayList<Song> arr = new ArrayList<>();
        for (SongOnline s: adapter.getData()) {
            arr.add(s);
        }

        MainActivity activity = (MainActivity) getActivity();
        activity.getService().setData(arr);
        activity.getService().getController()
                .create(adapter.getData().indexOf(item));
    }

    @Override
    public void onUnFavoriteClicked(SongOnline item) {

    }

    @Override
    public BaseBindingAdapter getBaseAdapter() {
        return adapter;
    }
}
