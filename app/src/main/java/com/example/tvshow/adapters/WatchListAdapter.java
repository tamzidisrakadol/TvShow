package com.example.tvshow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshow.R;
import com.example.tvshow.databinding.ListItemBinding;
import com.example.tvshow.databinding.WatchlistItemLayoutBinding;
import com.example.tvshow.model.TvShow;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.ViewHolder>{
    List<TvShow> tvShowList;
    WatchListner watchListner;

    public WatchListAdapter(List<TvShow> tvShowList, WatchListner watchListner) {
        this.tvShowList = tvShowList;
        this.watchListner = watchListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        WatchlistItemLayoutBinding watchlistItemLayoutBinding = DataBindingUtil.inflate(layoutInflater, R.layout.watchlist_item_layout,parent,false);
        return new ViewHolder(watchlistItemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.BindTvShow(tvShowList.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShowList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        WatchlistItemLayoutBinding watchlistItemLayoutBinding;

        public ViewHolder(WatchlistItemLayoutBinding watchlistItemLayoutBinding) {
            super(watchlistItemLayoutBinding.getRoot());
            this.watchlistItemLayoutBinding = watchlistItemLayoutBinding;
        }
        public void BindTvShow(TvShow tvShow){
            watchlistItemLayoutBinding.setTvShow(tvShow);
            watchlistItemLayoutBinding.executePendingBindings();
            watchlistItemLayoutBinding.getRoot().setOnClickListener(view -> {
                watchListner.onTouchClicked(tvShow);
            });
            watchlistItemLayoutBinding.listDeleteBtn.setOnClickListener(view -> {
                watchListner.removeTvShowFromWatchList(tvShow,getAdapterPosition());
            });

        }

    }

}
