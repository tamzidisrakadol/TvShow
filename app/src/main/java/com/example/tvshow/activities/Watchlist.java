package com.example.tvshow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tvshow.R;
import com.example.tvshow.databinding.ActivityWatchlistBinding;
import com.example.tvshow.viewModel.WatchListViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Watchlist extends AppCompatActivity {

    ActivityWatchlistBinding activityWatchlistBinding;
    WatchListViewModel watchListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWatchlistBinding = ActivityWatchlistBinding.inflate(getLayoutInflater());
        setContentView(activityWatchlistBinding.getRoot());

        watchListViewModel = new ViewModelProvider(this).get(WatchListViewModel.class);
        activityWatchlistBinding.watchlistBackBtn.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void loadWatchList(){
        activityWatchlistBinding.setIsLoading(false);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(watchListViewModel.loadWatchList().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                  activityWatchlistBinding.setIsLoading(false);
                    Toast.makeText(this, "watchlist :"+tvShows.size(), Toast.LENGTH_SHORT).show();
                }));
    }
}