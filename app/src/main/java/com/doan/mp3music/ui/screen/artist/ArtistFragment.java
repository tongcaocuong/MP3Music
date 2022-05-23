package com.doan.mp3music.ui.screen.artist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doan.mp3music.R;
import com.doan.mp3music.api.ApiBuilder;
import com.doan.mp3music.databinding.FragmentArtistBinding;
import com.doan.mp3music.databinding.FragmentOnlineBinding;
import com.doan.mp3music.models.Artist;
import com.doan.mp3music.models.Song;
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

public class ArtistFragment extends BaseFragment<FragmentArtistBinding, BaseViewModel> implements MediaListener<Artist> {

    private BaseBindingAdapter<Artist> adapter;

    @Override
    protected Class<BaseViewModel> getViewModelClass() {
        return BaseViewModel.class;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_artist;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new BaseBindingAdapter<>(
                R.layout.item_artist, getLayoutInflater());
        binding.setAdapter(adapter);
        adapter.setListener(this);
        ApiBuilder.getInstance().getArtist().enqueue(new Callback<List<Artist>>() {
            @Override
            public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
                adapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<List<Artist>> call, Throwable t) {

            }
        });
        getActivity().setTitle("Artist");
    }

    @Override
    public BaseBindingAdapter getBaseAdapter() {
        return adapter;
    }

    @Override
    public void onItemMediaClicked(Artist item) {
        MainActivity act = (MainActivity) getActivity();
        List<SongOnline> data = act.getFmMain().getData();
        ArrayList<SongOnline> songs = new ArrayList<>();
        for (SongOnline song : data) {
            if (song.getArtist().equals(item.getId() + "")) {
                songs.add(song);
            }
        }
        act.getFmOnline().setSongs(data);
        act.showFragment(act.getFmOnline());
    }

    @Override
    public void onUnFavoriteClicked(Artist item) {

    }
}
