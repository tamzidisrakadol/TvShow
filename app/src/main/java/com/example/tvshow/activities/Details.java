package com.example.tvshow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tvshow.R;
import com.example.tvshow.adapters.EpisodeAdapter;
import com.example.tvshow.adapters.ImageSlideAdapter;
import com.example.tvshow.databinding.ActivityDetailsBinding;
import com.example.tvshow.databinding.BottomsheetLayoutBinding;
import com.example.tvshow.response.TvShowDetailResponse;
import com.example.tvshow.viewModel.TvShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

public class Details extends AppCompatActivity {
    ActivityDetailsBinding activityDetailsBinding;
    TvShowDetailsViewModel tvShowDetailsViewModel;
    BottomSheetDialog episodeBtmSheet;
    BottomsheetLayoutBinding bottomsheetLayoutBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailsBinding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityDetailsBinding.getRoot());

        //init ViewModel
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TvShowDetailsViewModel.class);
        activityDetailsBinding.detailsBackBtn.setOnClickListener(view -> {
            onBackPressed();
        });

        getTvShowDetails();
    }

    private void getTvShowDetails(){
        activityDetailsBinding.setIsLoading(true);

        //get id from intent
        String detailsId = String.valueOf(getIntent().getIntExtra("id",-1));

        //live-data observer
        tvShowDetailsViewModel.getLiveTvShowDetails(detailsId).observe(this, tvShowDetailResponse -> {
            activityDetailsBinding.setIsLoading(false);
            if (tvShowDetailResponse.getTvShowDetails() !=null){
                if (tvShowDetailResponse.getTvShowDetails().getPictures() !=null){
                    loadImageSlider(tvShowDetailResponse.getTvShowDetails().getPictures());
                }
                activityDetailsBinding.setTvShowImageUrl(tvShowDetailResponse.getTvShowDetails().getImagePath());
                activityDetailsBinding.detailsRoundedImgView.setVisibility(View.VISIBLE);
                activityDetailsBinding.setDescription(String.valueOf(HtmlCompat.fromHtml(
                        tvShowDetailResponse.getTvShowDetails().getDescription(),HtmlCompat.FROM_HTML_MODE_LEGACY
                )));
                activityDetailsBinding.detailsDescription.setVisibility(View.VISIBLE);
                activityDetailsBinding.detailsTvReadMore.setVisibility(View.VISIBLE);
                activityDetailsBinding.detailsTvReadMore.setOnClickListener(view -> {
                    if (activityDetailsBinding.detailsTvReadMore.getText().toString().equals("Read More")){
                        activityDetailsBinding.detailsDescription.setMaxLines(Integer.MAX_VALUE);
                        activityDetailsBinding.detailsDescription.setEllipsize(null);
                        activityDetailsBinding.detailsTvReadMore.setText("Read Less");
                    }else{
                        activityDetailsBinding.detailsDescription.setMaxLines(4);
                        activityDetailsBinding.detailsDescription.setEllipsize(TextUtils.TruncateAt.END);
                        activityDetailsBinding.detailsTvReadMore.setText("Read More");
                    }
                });

                activityDetailsBinding.setRating(
                        String.format(Locale.getDefault(),"%.2f",Double.parseDouble(tvShowDetailResponse.getTvShowDetails().getRating())));
                if (tvShowDetailResponse.getTvShowDetails().getGenres()!=null){
                    activityDetailsBinding.setGenre(tvShowDetailResponse.getTvShowDetails().getGenres()[0]);
                }else{
                    activityDetailsBinding.setGenre("N/A");
                }
                activityDetailsBinding.setRuntime(tvShowDetailResponse.getTvShowDetails().getRuntime()+"Min");
                activityDetailsBinding.detailDivider.setVisibility(View.VISIBLE);
                activityDetailsBinding.detailMisc.setVisibility(View.VISIBLE);
                activityDetailsBinding.detailDivider2.setVisibility(View.VISIBLE);
                activityDetailsBinding.detailEpisodeBtn.setVisibility(View.VISIBLE);
                activityDetailsBinding.detailWebsiteBtn.setVisibility(View.VISIBLE);

                activityDetailsBinding.detailWebsiteBtn.setOnClickListener(view -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(tvShowDetailResponse.getTvShowDetails().getUrl()));
                    startActivity(intent);
                });

                activityDetailsBinding.detailEpisodeBtn.setOnClickListener(view -> {
                    if (episodeBtmSheet ==null){
                        episodeBtmSheet = new BottomSheetDialog(Details.this);
                        bottomsheetLayoutBinding=DataBindingUtil.inflate(LayoutInflater.from(Details.this),
                                R.layout.bottomsheet_layout,
                                findViewById(R.id.btmSheetEpisodeContainer),
                                false);
                        episodeBtmSheet.setContentView(bottomsheetLayoutBinding.getRoot());
                        bottomsheetLayoutBinding.btmSheetRecyclerview.setAdapter(new EpisodeAdapter(tvShowDetailResponse.getTvShowDetails().getEpisodesList()));
                        bottomsheetLayoutBinding.bottomSheetTitle.setText(String.format("Episodes | %s",getIntent().getStringExtra("name")));
                        bottomsheetLayoutBinding.bottomSheetImgClose.setOnClickListener(view1 -> {
                            episodeBtmSheet.dismiss();
                        });
                    }
                    episodeBtmSheet.show();


                });

                loadBasicDetails();
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

    private void loadBasicDetails(){
        activityDetailsBinding.setTvShowName(getIntent().getStringExtra("name"));
        activityDetailsBinding.setNetworkCountry(getIntent().getStringExtra("network")+" ("+getIntent().getStringExtra("country") + ")" );
        activityDetailsBinding.setStatus(getIntent().getStringExtra("status"));
        activityDetailsBinding.setStartDate(getIntent().getStringExtra("startDate"));
        activityDetailsBinding.detailsSeriesName.setVisibility(View.VISIBLE);
        activityDetailsBinding.detailsNetworkCountry.setVisibility(View.VISIBLE);
        activityDetailsBinding.detailsSeriesStarted.setVisibility(View.VISIBLE);
        activityDetailsBinding.detailsSeriesStatus.setVisibility(View.VISIBLE);
    }

}