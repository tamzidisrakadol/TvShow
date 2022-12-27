package com.example.tvshow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshow.R;
import com.example.tvshow.databinding.ItemBottomsheetBinding;
import com.example.tvshow.model.Episode;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder>{
    List<Episode> episodeList;

    public EpisodeAdapter(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemBottomsheetBinding itemBottomsheetBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_bottomsheet,parent,false);
        return new ViewHolder(itemBottomsheetBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindShow(episodeList.get(position));
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ItemBottomsheetBinding itemBottomsheetBinding;
        public ViewHolder(ItemBottomsheetBinding itemBottomsheetBinding) {
            super(itemBottomsheetBinding.getRoot());
            this.itemBottomsheetBinding = itemBottomsheetBinding;
        }
        public void bindShow(Episode episode){
            String title = "s";
            String season = episode.getSeason();
            if (season.length()==1){
                season = "0".concat(season);
            }
            String epiNumber = episode.getEpisode();
            if (epiNumber.length()==1){
                epiNumber="0".concat(epiNumber);
            }
            epiNumber="E".concat(epiNumber);
            title = title.concat(season).concat(" ").concat(epiNumber);
            itemBottomsheetBinding.setTitle(title);
            itemBottomsheetBinding.setName(episode.getName());
            itemBottomsheetBinding.setAirDate(episode.getAirDate());
        }
    }


}
