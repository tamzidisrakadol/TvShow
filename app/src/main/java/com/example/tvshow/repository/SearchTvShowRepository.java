package com.example.tvshow.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvshow.network.ApiClient;
import com.example.tvshow.network.ApiService;
import com.example.tvshow.response.TvShowResponses;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTvShowRepository {
    ApiService apiService;


    public SearchTvShowRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowResponses> searchTvShow(String query,int page){
        MutableLiveData<TvShowResponses> data = new MutableLiveData<>();
        apiService.searchTvShow(query,page).enqueue(new Callback<TvShowResponses>() {
            @Override
            public void onResponse(Call<TvShowResponses> call, Response<TvShowResponses> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TvShowResponses> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
