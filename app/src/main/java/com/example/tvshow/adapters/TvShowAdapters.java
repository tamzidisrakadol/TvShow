package com.example.tvshow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshow.R;
import com.example.tvshow.databinding.ListItemBinding;
import com.example.tvshow.model.TvShow;

import java.util.List;

public class TvShowAdapters extends RecyclerView.Adapter<TvShowAdapters.TvShowViewHolder>{
    List<TvShow> tvShowList;
    ItemClickListener itemClickListener;

    public TvShowAdapters(List<TvShow> tvShowList,ItemClickListener itemClickListener) {
        this.tvShowList = tvShowList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemBinding tvShowItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item,parent,false);
        return new TvShowViewHolder(tvShowItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder holder, int position) {
        holder.BindTvShow(tvShowList.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShowList.size();
    }

    public  class TvShowViewHolder extends RecyclerView.ViewHolder{
        ListItemBinding listItemBinding;

        public TvShowViewHolder(ListItemBinding listItemBinding) {
            super(listItemBinding.getRoot());
            this.listItemBinding = listItemBinding;
        }

        public void BindTvShow(TvShow tvShow){
            listItemBinding.setTvShow(tvShow);
            listItemBinding.executePendingBindings();
            listItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(tvShow);
                }
            });
        }
    }
}
