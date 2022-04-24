package com.doan.mp3music.ui.screen.favorite;

import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.doan.mp3music.R;
import com.doan.mp3music.api.ApiBuilder;
import com.doan.mp3music.databinding.FragmentFavoriteBinding;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends BaseFragment<FragmentFavoriteBinding, BaseViewModel> implements MediaListener<SongOnline> {

    private BaseBindingAdapter<SongOnline> adapter;

    @Override
    protected Class<BaseViewModel> getViewModelClass() {
        return BaseViewModel.class;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_favorite;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new BaseBindingAdapter<>(
                R.layout.item_song_favorite, getLayoutInflater());
        binding.setAdapter(adapter);
        adapter.setListener(this);
        getSong();
    }

    public void getSong() {
        String device = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        ApiBuilder.getInstance().getFavorite(device).enqueue(new Callback<List<SongOnline>>() {
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
        String device = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        ApiBuilder.getInstance().removeFavorite(device, item.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    getSong();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public BaseBindingAdapter getBaseAdapter() {
        return adapter;
    }
}
