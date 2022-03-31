package com.doan.mp3music.ui.screen.song;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.doan.mp3music.R;
import com.doan.mp3music.databinding.FragmentSongBinding;
import com.doan.mp3music.models.Song;
import com.doan.mp3music.ui.base.BaseBindingAdapter;
import com.doan.mp3music.ui.base.BaseFragment;
import com.doan.mp3music.ui.screen.MediaListener;
import com.doan.mp3music.ui.screen.main.MainActivity;

import java.util.ArrayList;
import java.util.Collections;

public class SongFragment extends BaseFragment<FragmentSongBinding, SongViewModel> implements MediaListener<Song> {

    private BaseBindingAdapter<Song> adapter;

    @Override
    protected Class<SongViewModel> getViewModelClass() {
        return SongViewModel.class;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_song;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new BaseBindingAdapter<>(
                R.layout.item_song, getLayoutInflater());
        binding.setAdapter(adapter);
        adapter.setListener(this);
        adapter.setData(viewModel.getSong(getContext()));
        binding.btnShuffle.setVisibility(View.VISIBLE);
        binding.btnShuffle.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) getActivity();
            ArrayList<Song> arr = viewModel.getSong(getContext());
            Collections.shuffle(arr);
            activity.getService().setData(arr);
            activity.getService().getController().create(0);
        });
    }

    @Override
    public void onItemMediaClicked(Song item) {
        MainActivity activity = (MainActivity) getActivity();
        activity.getService().setData(adapter.getData());
        activity.getService().getController()
                .create(adapter.getData().indexOf(item));
    }

    @Override
    public void onUnFavoriteClicked(Song item) {

    }

    @Override
    public BaseBindingAdapter getBaseAdapter() {
        return adapter;
    }
}
