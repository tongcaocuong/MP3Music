package com.doan.mp3music.ui.screen.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doan.mp3music.R;
import com.doan.mp3music.api.ApiBuilder;
import com.doan.mp3music.databinding.FragmentMainBinding;
import com.doan.mp3music.models.Song;
import com.doan.mp3music.models.SongOnline;
import com.doan.mp3music.ui.base.BaseBindingAdapter;
import com.doan.mp3music.ui.base.BaseFragment;
import com.doan.mp3music.ui.base.BaseViewModel;
import com.doan.mp3music.ui.screen.MediaListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends BaseFragment<FragmentMainBinding, BaseViewModel> implements MediaListener<SongOnline> {
    private List<SongOnline> data = new ArrayList<>();
    private List<SongOnline> dataTop = new ArrayList<>();

    @Override
    protected Class<BaseViewModel> getViewModelClass() {
        return BaseViewModel.class;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_main;
    }

    @Override
    public BaseBindingAdapter getBaseAdapter() {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSong();
        MainActivity act = (MainActivity) getActivity();
        binding.panelSong.setOnClickListener(v -> {
            act.getFmOnline().setSongs(data);
            act.showFragment(act.getFmOnline());
        });
        binding.panelDisk.setOnClickListener(v -> {
            act.showFragment(act.getFmMyMusic());
        });
        binding.panelPlaylist.setOnClickListener(v -> {
            act.showFragment(act.getFmFavorite());
        });
        binding.panelAlbum.setOnClickListener(v -> {
            act.showFragment(act.getFmAlbum());
        });
        binding.panelArtist.setOnClickListener(v -> {
            act.showFragment(act.getFmArtist());
        });
        getActivity().setTitle("MP3 Music");
        BaseBindingAdapter<SongOnline> adapter = new BaseBindingAdapter<>(
                R.layout.item_song_top, getLayoutInflater());
        binding.setAdapter(adapter);
        adapter.setListener(this);
        ApiBuilder.getInstance().getTopSong().enqueue(new Callback<List<SongOnline>>() {
            @Override
            public void onResponse(Call<List<SongOnline>> call, Response<List<SongOnline>> response) {
                dataTop = response.body();
                adapter.setData(dataTop);
            }

            @Override
            public void onFailure(Call<List<SongOnline>> call, Throwable t) {

            }
        });
    }

    public void getSong() {
        ApiBuilder.getInstance().getSong().enqueue(new Callback<List<SongOnline>>() {
            @Override
            public void onResponse(Call<List<SongOnline>> call, Response<List<SongOnline>> response) {
                MainFragment.this.data = response.body();
            }

            @Override
            public void onFailure(Call<List<SongOnline>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public List<SongOnline> getData() {
        return data;
    }

    @Override
    public void onItemMediaClicked(SongOnline item) {
        ArrayList<Song> arr = new ArrayList<>();
        for (SongOnline s: dataTop) {
            arr.add(s);
        }

        MainActivity activity = (MainActivity) getActivity();
        activity.getService().setData(arr);
        activity.getService().getController()
                .create(dataTop.indexOf(item));
    }

    @Override
    public void onUnFavoriteClicked(SongOnline item) {

    }
}