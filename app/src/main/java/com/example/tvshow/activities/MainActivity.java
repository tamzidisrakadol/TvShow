package com.example.tvshow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

        getTvShow();

    }

    private void getTvShow() {
        activityMainBinding.setIsLoading(true);

        //live_observer
        mostPopularTvShowViewModel.getMostPopularShow(0).observe(this, tvShowResponses -> {
            activityMainBinding.setIsLoading(false);
            if (tvShowResponses != null){
                if (tvShowResponses.getListTvShow() != null){
                    tvShowList.addAll(tvShowResponses.getListTvShow());
                    tvShowAdapters.notifyDataSetChanged();
                }
            }
        });
    }
}