package com.example.tvshow.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tvshow.R;
import com.example.tvshow.adapters.TvShowAdapters;
import com.example.tvshow.databinding.ActivityMainBinding;
import com.example.tvshow.model.TvShow;
import com.example.tvshow.response.TvShowResponses;
import com.example.tvshow.viewModel.MostPopularTvShowViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    MostPopularTvShowViewModel mostPopularTvShowViewModel;
    List<TvShow> tvShowList = new ArrayList<>();
    TvShowAdapters tvShowAdapters;
    int currentPage =1;
    int totalAvailPages = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);

        activityMainBinding.recyclerView.setHasFixedSize(true);

        //init viewModel
        mostPopularTvShowViewModel = new ViewModelProvider(this).get(MostPopularTvShowViewModel.class);

        //recyclerView with adapter
        tvShowAdapters = new TvShowAdapters(tvShowList);
        activityMainBinding.recyclerView.setAdapter(tvShowAdapters);

        //if we cannot scroll & current page is less than total page -> current page will be updated + add more show to list
        activityMainBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activityMainBinding.recyclerView.canScrollVertically(1)){
                    if (currentPage <=totalAvailPages){
                        currentPage+=1;
                        getTvShow();
                    }
                }
            }
        });

        //to fetch all show
        getTvShow();

    }

    private void getTvShow() {
        toggleLoading();

        //live_observer & adding show to list
        mostPopularTvShowViewModel.getMostPopularShow(currentPage).observe(this, tvShowResponses -> {
           toggleLoading();
            if (tvShowResponses != null){
                //get total pages to load all shows
                totalAvailPages = tvShowResponses.getTotalPage();
                if (tvShowResponses.getListTvShow() != null){
                    int oldCount=tvShowList.size();
                    tvShowList.addAll(tvShowResponses.getListTvShow());

                    //newly inserted in list(starting point, total size)
                    tvShowAdapters.notifyItemRangeInserted(oldCount,tvShowList.size());
                }
            }
        });
    }

    private void toggleLoading(){
        if(currentPage==1){
            activityMainBinding.setIsLoading(activityMainBinding.getIsLoading() == null || !activityMainBinding.getIsLoading());
        }else{
            activityMainBinding.setIsLoadingMore(activityMainBinding.getIsLoadingMore() == null || !activityMainBinding.getIsLoadingMore());
        }
    }
}