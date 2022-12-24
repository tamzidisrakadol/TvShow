package com.example.tvshow.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshow.repository.MostPopularTvShowRepository;
import com.example.tvshow.response.TvShowResponses;

public class MostPopularTvShowViewModel extends ViewModel {

    //setting repos with ViewModel
    MostPopularTvShowRepository mostPopularTvShowRepository;

    public MostPopularTvShowViewModel() {
        mostPopularTvShowRepository = new MostPopularTvShowRepository();
    }

    public LiveData<TvShowResponses> getMostPopularShow(int pages){
        return mostPopularTvShowRepository.getMostPopularTVShow(pages);
    }
}
