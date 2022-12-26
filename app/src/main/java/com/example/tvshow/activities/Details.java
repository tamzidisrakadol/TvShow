package com.example.tvshow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tvshow.R;
import com.example.tvshow.adapters.ImageSlideAdapter;
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

        //init ViewModel
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TvShowDetailsViewModel.class);

        getTvShowDetails();
    }

    private void getTvShowDetails(){
        activityDetailsBinding.setIsLoading(true);

        //get id from intent
        String detailsId = String.valueOf(getIntent().getIntExtra("id",-1));

        //live-data observer
        tvShowDetailsViewModel.getLiveTvShowDetails(detailsId).observe(this, new Observer<TvShowDetailResponse>() {
            @Override
            public void onChanged(TvShowDetailResponse tvShowDetailResponse) {
                activityDetailsBinding.setIsLoading(false);
                if (tvShowDetailResponse.getTvShowDetails() !=null){
                    if (tvShowDetailResponse.getTvShowDetails().getPictures() !=null){
                        loadImageSlider(tvShowDetailResponse.getTvShowDetails().getPictures());
                    }
                }
            }
        });
    }

    //banner sliding Images
    private void loadImageSlider(String[] sliderImage){
        activityDetailsBinding.detailsViewPager.setOffscreenPageLimit(1);
        activityDetailsBinding.detailsViewPager.setAdapter(new ImageSlideAdapter(sliderImage));
        activityDetailsBinding.detailsViewPager.setVisibility(View.VISIBLE);
        activityDetailsBinding.viewFadingBg.setVisibility(View.VISIBLE);
        setupSliderIndicator(sliderImage.length);
        activityDetailsBinding.detailsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setUpCurrentSliderIndicator(position);
            }
        });
    }

    //setup indicator
    private void setupSliderIndicator(int count){
        ImageView[] indicator = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0,8,0);
        for(int i=0;i<indicator.length;i++){
            indicator[i] = new ImageView(getApplicationContext());
            indicator[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.lsider_imaage_indicator));
            indicator[i].setLayoutParams(layoutParams);
            activityDetailsBinding.slideIndicator.addView(indicator[i]);
        }
        activityDetailsBinding.slideIndicator.setVisibility(View.VISIBLE);
        setUpCurrentSliderIndicator(0);
    }

    //setup current indicator
    private void setUpCurrentSliderIndicator(int pos){
        int childCount = activityDetailsBinding.slideIndicator.getChildCount();
        for(int i=0;i<childCount;i++){
            ImageView imgView = (ImageView) activityDetailsBinding.slideIndicator.getChildAt(i);
            if (i==pos){
                imgView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.slider_inactive_indicator));
            }else{
                imgView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.lsider_imaage_indicator));
            }
        }
    }

}