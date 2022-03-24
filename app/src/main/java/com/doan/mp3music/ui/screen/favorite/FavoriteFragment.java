package com.doan.mp3music.ui.screen.favorite;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.doan.mp3music.R;
import com.doan.mp3music.databinding.FragmentFavoriteBinding;
import com.doan.mp3music.databinding.FragmentOnlineBinding;
import com.doan.mp3music.ui.base.BaseFragment;
import com.doan.mp3music.ui.screen.online.OnlineViewModel;

public class FavoriteFragment extends BaseFragment<FragmentFavoriteBinding, OnlineViewModel> {


    @Override
    protected Class<OnlineViewModel> getViewModelClass() {
        return OnlineViewModel.class;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_favorite;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
