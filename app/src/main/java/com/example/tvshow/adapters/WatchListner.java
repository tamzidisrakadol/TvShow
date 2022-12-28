package com.example.tvshow.adapters;

import com.example.tvshow.model.TvShow;

public interface WatchListner {

    void onTouchClicked(TvShow tvShow);

    void removeTvShowFromWatchList(TvShow tvShow,int pos);

}
