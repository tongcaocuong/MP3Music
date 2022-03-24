package com.doan.mp3music.ui.screen.online;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.doan.mp3music.R;
import com.doan.mp3music.api.ApiBuilder;
import com.doan.mp3music.databinding.FragmentOnlineBinding;
import com.doan.mp3music.models.Song;
import com.doan.mp3music.models.SongOnline;
import com.doan.mp3music.ui.base.BaseBindingAdapter;
import com.doan.mp3music.ui.base.BaseFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineFragment extends BaseFragment<FragmentOnlineBinding, OnlineViewModel> implements BaseBindingAdapter.BaseBindingListener {

    private BaseBindingAdapter<SongOnline> adapter;

    @Override
    protected Class<OnlineViewModel> getViewModelClass() {
        return OnlineViewModel.class;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_online;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new BaseBindingAdapter<>(
                R.layout.item_song_online, getLayoutInflater());
        binding.setAdapter(adapter);
        adapter.setListener(this);
        //getSong();
    }

    public void getSong() {
        ApiBuilder.getInstance().getSong().enqueue(new Callback<List<SongOnline>>() {
            @Override
            public void onResponse(Call<List<SongOnline>> call, Response<List<SongOnline>> response) {
                adapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<List<SongOnline>> call, Throwable t) {
                Toast.makeText(getContext(),  t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
