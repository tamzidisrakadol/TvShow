package com.example.tvshow.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshow.repository.SearchTvShowRepository;
import com.example.tvshow.response.TvShowResponses;

public class SearchTvModel extends ViewModel {
    SearchTvShowRepository searchTvShowRepository;

    public SearchTvModel() {
        searchTvShowRepository = new SearchTvShowRepository();
    }

    public LiveData<TvShowResponses> searchTvResponse(String query,int page){
        return searchTvShowRepository.searchTvShow(query,page);
    }



}
