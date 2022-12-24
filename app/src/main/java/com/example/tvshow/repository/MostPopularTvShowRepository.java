package com.example.tvshow.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvshow.network.ApiClient;
import com.example.tvshow.network.ApiService;
import com.example.tvshow.response.TvShowResponses;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTvShowRepository {



    private ApiService apiService;

    public MostPopularTvShowRepository() {
        //creating retrofit instance
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowResponses> getMostPopularTVShow(int page){
        MutableLiveData<TvShowResponses> tvShowResponsesMutableLiveData = new MutableLiveData<>();

        //setting retrofit with mutableLiveData
        apiService.getTVPopularShow(page).enqueue(new Callback<TvShowResponses>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponses> call, @NonNull Response<TvShowResponses> response) {
                tvShowResponsesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponses> call, @NonNull Throwable t) {
                tvShowResponsesMutableLiveData.setValue(null);
            }
        });
        return tvShowResponsesMutableLiveData;
    }
}
