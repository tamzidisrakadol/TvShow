package com.example.tvshow.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tvshow.database.TvShowDatabase;
import com.example.tvshow.model.TvShow;

import java.util.List;

import io.reactivex.Flowable;

public class WatchListViewModel extends AndroidViewModel {
    TvShowDatabase tvShowDatabase;


    public WatchListViewModel(@NonNull Application application) {
        super(application);
        tvShowDatabase = TvShowDatabase.getTvShowDatabase(application);
    }

    public Flowable<List<TvShow>> loadWatchList(){
        return tvShowDatabase.tvShowDao().getWatchList();
    }
}
