package com.example.tvshow.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshow.repository.TvShowDetailsRepository;
import com.example.tvshow.response.TvShowDetailResponse;

public class TvShowDetailsViewModel extends ViewModel {
    TvShowDetailsRepository tvShowDetailsRepository;

    public TvShowDetailsViewModel() {
        tvShowDetailsRepository = new TvShowDetailsRepository();
    }

    public LiveData<TvShowDetailResponse> getLiveTvShowDetails(String showID){
        return tvShowDetailsRepository.getTvShowDetails(showID);
    }

}
