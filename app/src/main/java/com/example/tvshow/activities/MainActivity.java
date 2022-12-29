package com.example.tvshow.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import com.example.tvshow.R;
import com.example.tvshow.adapters.ItemClickListener;
import com.example.tvshow.adapters.TvShowAdapters;
import com.example.tvshow.databinding.ActivityMainBinding;
import com.example.tvshow.model.TvShow;
import com.example.tvshow.response.TvShowResponses;
import com.example.tvshow.viewModel.MostPopularTvShowViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemClickListener {

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

        String text = "SeriesZone";
        SpannableString spannableString = new SpannableString(text);
        int green = Color.GREEN;
        ForegroundColorSpan redSpan = new ForegroundColorSpan(green);
        spannableString.setSpan(redSpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        activityMainBinding.headerText.setText(spannableString);


        activityMainBinding.recyclerView.setHasFixedSize(true);

        //init viewModel
        mostPopularTvShowViewModel = new ViewModelProvider(this).get(MostPopularTvShowViewModel.class);

        //recyclerView with adapter
        tvShowAdapters = new TvShowAdapters(tvShowList,this);
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
        activityMainBinding.bookmark.setOnClickListener(view1 -> startActivity(new Intent(MainActivity.this,Watchlist.class)));
        activityMainBinding.movieSearchBtn.setOnClickListener(view12 -> {
            startActivity(new Intent(MainActivity.this,SearchTvActivity.class));
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

    @Override
    public void onItemClick(TvShow tvShow) {
        Intent intent = new Intent(MainActivity.this,Details.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }
}