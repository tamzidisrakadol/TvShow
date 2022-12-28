package com.example.tvshow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.tvshow.adapters.WatchListAdapter;
import com.example.tvshow.adapters.WatchListner;
import com.example.tvshow.databinding.ActivityWatchlistBinding;
import com.example.tvshow.model.TvShow;
import com.example.tvshow.utilities.TempDataHolder;
import com.example.tvshow.viewModel.WatchListViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Watchlist extends AppCompatActivity implements WatchListner {

    ActivityWatchlistBinding activityWatchlistBinding;
    WatchListViewModel watchListViewModel;
    WatchListAdapter watchListAdapter;
    List<TvShow> tvShowList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWatchlistBinding = ActivityWatchlistBinding.inflate(getLayoutInflater());
        setContentView(activityWatchlistBinding.getRoot());

        watchListViewModel = new ViewModelProvider(this).get(WatchListViewModel.class);
        activityWatchlistBinding.watchlistBackBtn.setOnClickListener(view -> {
            onBackPressed();
        });
        tvShowList = new ArrayList<>();
        loadWatchList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TempDataHolder.isDataUpdated){
            loadWatchList();
            TempDataHolder.isDataUpdated = false;
        }

    }

    private void loadWatchList(){
        activityWatchlistBinding.setIsLoading(false);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(watchListViewModel.loadWatchList().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                  activityWatchlistBinding.setIsLoading(false);
                    if (tvShowList.size()>0){
                        tvShowList.clear();
                    }
                    tvShowList.addAll(tvShows);
                    watchListAdapter = new WatchListAdapter(tvShowList,this);
                    activityWatchlistBinding.watchlistRecyclerview.setAdapter(watchListAdapter);
                    compositeDisposable.dispose();
                }));
    }

    @Override
    public void onTouchClicked(TvShow tvShow) {
        Intent intent = new Intent(Watchlist.this,Details.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }

    @Override
    public void removeTvShowFromWatchList(TvShow tvShow, int pos) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(watchListViewModel.removeFromWatchlist(tvShow)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                    tvShowList.remove(pos);
                    watchListAdapter.notifyItemRemoved(pos);
                    watchListAdapter.notifyItemRangeChanged(pos,watchListAdapter.getItemCount());
                    compositeDisposable.dispose();
                }));
    }
}