package com.example.tvshow.network;

import com.example.tvshow.response.TvShowDetailResponse;
import com.example.tvshow.response.TvShowResponses;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    //query in url
    @GET("most-popular")
    Call<TvShowResponses> getTVPopularShow(@Query("page")int page);

    @GET("show-details")
    Call<TvShowDetailResponse> getTVShowDetails(@Query("q")String tvShowId);

    @GET("search")
    Call<TvShowResponses> searchTvShow(@Query("q")String query, @Query("page")int page);
}
