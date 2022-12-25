package com.example.tvshow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.example.tvshow.databinding.ActivityDetailsBinding;
import com.example.tvshow.response.TvShowDetailResponse;
import com.example.tvshow.viewModel.TvShowDetailsViewModel;

public class Details extends AppCompatActivity {
    ActivityDetailsBinding activityDetailsBinding;
    TvShowDetailsViewModel tvShowDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailsBinding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityDetailsBinding.getRoot());


        tvShowDetailsViewModel = new ViewModelProvider(this).get(TvShowDetailsViewModel.class);
        getTvShowDetails();
    }

    private void getTvShowDetails(){
        activityDetailsBinding.setIsLoading(true);
        String detailsId = String.valueOf(getIntent().getIntExtra("id",-1));
        tvShowDetailsViewModel.getLiveTvShowDetails(detailsId).observe(this, new Observer<TvShowDetailResponse>() {
            @Override
            public void onChanged(TvShowDetailResponse tvShowDetailResponse) {
                activityDetailsBinding.setIsLoading(false);
                Toast.makeText(Details.this, ""+tvShowDetailResponse.getTvShowDetails().getUrl(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}