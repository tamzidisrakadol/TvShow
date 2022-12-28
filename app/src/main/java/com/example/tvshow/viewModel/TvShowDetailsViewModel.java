package com.example.tvshow.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshow.database.TvShowDatabase;
import com.example.tvshow.model.TvShow;
import com.example.tvshow.repository.TvShowDetailsRepository;
import com.example.tvshow.response.TvShowDetailResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

public class TvShowDetailsViewModel extends AndroidViewModel {
    TvShowDetailsRepository tvShowDetailsRepository;
    TvShowDatabase tvShowDatabase;

    public TvShowDetailsViewModel(@NonNull Application application) {
        super(application);
        tvShowDetailsRepository = new TvShowDetailsRepository();
        tvShowDatabase = TvShowDatabase.getTvShowDatabase(application);

    }

    public LiveData<TvShowDetailResponse> getLiveTvShowDetails(String showID){
        return tvShowDetailsRepository.getTvShowDetails(showID);
    }

    public Completable addToWatchList(TvShow tvShow){
        return tvShowDatabase.tvShowDao().addToWatchList(tvShow);
    }

    public Flowable<TvShow> getTvShowFromWatchlist(String tvShowId){
        return tvShowDatabase.tvShowDao().getTvShowFromWatchlist(tvShowId);
    }
    public Completable removeTvFromWatchList(TvShow tvShow){
        return tvShowDatabase.tvShowDao().removeFromWatchList(tvShow);
    }

}
