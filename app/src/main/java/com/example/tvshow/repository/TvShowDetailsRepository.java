package com.example.tvshow.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvshow.network.ApiClient;
import com.example.tvshow.network.ApiService;
import com.example.tvshow.response.TvShowDetailResponse;
import com.example.tvshow.response.TvShowResponses;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowDetailsRepository {
    ApiService apiService;

    public TvShowDetailsRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowDetailResponse> getTvShowDetails(String tvShowID){
        MutableLiveData<TvShowDetailResponse> tvShowDetailResponseMutableLiveData = new MutableLiveData<>();
        apiService.getTVShowDetails(tvShowID).enqueue(new Callback<TvShowDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowDetailResponse> call, @NonNull Response<TvShowDetailResponse> response) {
                tvShowDetailResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TvShowDetailResponse> call, @NonNull Throwable t) {
                tvShowDetailResponseMutableLiveData.setValue(null);
            }
        });
        return tvShowDetailResponseMutableLiveData;
    }


}
