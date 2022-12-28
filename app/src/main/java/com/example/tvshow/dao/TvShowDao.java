package com.example.tvshow.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tvshow.model.TvShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface TvShowDao {

    @Query("SELECT * FROM tvshows")
    Flowable<List<TvShow>> getWatchList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addToWatchList(TvShow tvShow);

    @Delete
    Completable removeFromWatchList(TvShow tvShow);

    @Query("SELECT * FROM tvShows where id = :tvShowId")
    Flowable<TvShow> getTvShowFromWatchlist(String tvShowId);
}
