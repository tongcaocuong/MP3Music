package com.doan.mp3music.ui.screen.album;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doan.mp3music.R;
import com.doan.mp3music.api.ApiBuilder;
import com.doan.mp3music.databinding.FragmentAlbumBinding;
import com.doan.mp3music.databinding.FragmentArtistBinding;
import com.doan.mp3music.models.Album;
import com.doan.mp3music.models.Artist;
import com.doan.mp3music.models.SongOnline;
import com.doan.mp3music.ui.base.BaseBindingAdapter;
import com.doan.mp3music.ui.base.BaseFragment;
import com.doan.mp3music.ui.base.BaseViewModel;
import com.doan.mp3music.ui.screen.MediaListener;
import com.doan.mp3music.ui.screen.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumFragment extends BaseFragment<FragmentAlbumBinding, BaseViewModel> implements MediaListener<Album> {

    private BaseBindingAdapter<Album> adapter;

    @Override
    protected Class<BaseViewModel> getViewModelClass() {
        return BaseViewModel.class;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_album;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new BaseBindingAdapter<>(
                R.layout.item_album, getLayoutInflater());
        binding.setAdapter(adapter);
        adapter.setListener(this);
        ApiBuilder.getInstance().getAlbum().enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                adapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
        getActivity().setTitle("Album");
    }

    @Override
    public BaseBindingAdapter getBaseAdapter() {
        return adapter;
    }

    @Override
    public void onItemMediaClicked(Album item) {
        MainActivity act = (MainActivity) getActivity();
        List<SongOnline> data = act.getFmMain().getData();
        ArrayList<SongOnline> songs = new ArrayList<>();
        for (SongOnline song : data) {
            if (song.getAlbum().equals(item.getId() + "")) {
                songs.add(song);
            }
        }
        act.getFmOnline().setSongs(data);
        act.showFragment(act.getFmOnline());
    }

    @Override
    public void onUnFavoriteClicked(Album item) {

    }
}
