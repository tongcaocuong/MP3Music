package com.doan.mp3music.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.doan.mp3music.R;
import com.doan.mp3music.models.BaseModel;
import com.doan.mp3music.models.Song;
import com.doan.mp3music.ui.screen.favorite.FavoriteFragment;
import com.doan.mp3music.ui.screen.main.MainActivity;
import com.doan.mp3music.ui.screen.online.OnlineFragment;
import com.doan.mp3music.ui.screen.song.SongFragment;

public abstract class BaseFragment<BD extends ViewDataBinding, VM extends BaseViewModel> extends Fragment {
    protected BD binding;
    protected VM viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                getLayoutID(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(getViewModelClass());
        if (((MainActivity)getActivity()).menu != null) {
            ((MainActivity) getActivity()).menu.findItem(R.id.menu_search).setVisible((this instanceof SongFragment || this instanceof OnlineFragment || this instanceof FavoriteFragment));
        }
    }

    protected abstract Class<VM> getViewModelClass();

    protected abstract int getLayoutID();

    public abstract BaseBindingAdapter getBaseAdapter();
}
