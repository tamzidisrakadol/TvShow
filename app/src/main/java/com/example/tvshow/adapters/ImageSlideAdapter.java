package com.example.tvshow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshow.R;
import com.example.tvshow.databinding.ItemContainerSliderBinding;

public class ImageSlideAdapter extends RecyclerView.Adapter<ImageSlideAdapter.ViewHolder>{

    private final String[] sliderImage;

    public ImageSlideAdapter(String[] sliderImage) {
        this.sliderImage = sliderImage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemContainerSliderBinding itemContainerSliderBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_container_slider,parent,false);
        return new ViewHolder(itemContainerSliderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindSliderImage(sliderImage[position]);
    }

    @Override
    public int getItemCount() {
        return sliderImage.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ItemContainerSliderBinding itemContainerSliderBinding;
        public ViewHolder(ItemContainerSliderBinding itemContainerSliderBinding) {
            super(itemContainerSliderBinding.getRoot());
            this.itemContainerSliderBinding = itemContainerSliderBinding;
        }
        public void bindSliderImage(String imgUrl){
            itemContainerSliderBinding.setImgUrl(imgUrl);
        }
    }
}
