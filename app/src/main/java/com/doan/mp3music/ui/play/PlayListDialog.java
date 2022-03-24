package com.doan.mp3music.ui.play;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doan.mp3music.R;
import com.doan.mp3music.databinding.FragmentSongBinding;
import com.doan.mp3music.models.Song;
import com.doan.mp3music.service.MP3Service;
import com.doan.mp3music.ui.base.BaseBindingAdapter;
import com.doan.mp3music.ui.screen.MediaListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PlayListDialog extends BottomSheetDialogFragment {

    private static MP3Service service;
    private static MediaListener<Song> listener;
    private BaseBindingAdapter<Song> adapter;
    private FragmentSongBinding binding;

    public static PlayListDialog newInstance(MP3Service service, MediaListener<Song> listener) {
        PlayListDialog.service = service;
        PlayListDialog.listener = listener;
        return new PlayListDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSongBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new BaseBindingAdapter<>(
                R.layout.item_song, getLayoutInflater());
        adapter.setListener(listener);
        binding.setAdapter(adapter);
        adapter.setData(service.getController().getSongs());
    }
}

