package com.doan.mp3music.ui.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.doan.mp3music.BR;
import com.doan.mp3music.models.BaseModel;

import java.util.ArrayList;
import java.util.List;

public class BaseBindingAdapter<T extends BaseModel> extends RecyclerView.Adapter<BaseBindingAdapter.BaseBindingHolder> {

    private List<T> data;
    private @LayoutRes int resId;
    private LayoutInflater inflater;
    private BaseBindingListener listener;

    public BaseBindingAdapter(int resId, LayoutInflater inflater) {
        this.resId = resId;
        this.inflater = inflater;
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return data;
    }

    public void setListener(BaseBindingListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseBindingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                inflater, resId, parent, false);
        return new BaseBindingHolder(binding);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseBindingAdapter.BaseBindingHolder holder, int position) {
        T t = data.get(position);
        holder.binding.setVariable(BR.item, t);
        if (listener != null) {
            holder.binding.setVariable(BR.listener, listener);
        }
        holder.binding.executePendingBindings();
    }

    public class BaseBindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;
        public BaseBindingHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface BaseBindingListener{}
}
