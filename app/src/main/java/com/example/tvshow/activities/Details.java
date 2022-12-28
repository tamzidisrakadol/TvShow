package com.example.tvshow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
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
import com.example.tvshow.model.TvShow;
import com.example.tvshow.response.TvShowDetailResponse;
import com.example.tvshow.utilities.TempDataHolder;
import com.example.tvshow.viewModel.TvShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Details extends AppCompatActivity {
    ActivityDetailsBinding activityDetailsBinding;
    TvShowDetailsViewModel tvShowDetailsViewModel;
    BottomSheetDialog episodeBtmSheet;
    BottomsheetLayoutBinding bottomsheetLayoutBinding;
    TvShow tvShow;
    Boolean isTvShowAvailableInWatchList = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailsBinding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityDetailsBinding.getRoot());

        //init ViewModel
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TvShowDetailsViewModel.class);

        //back-press btn
        activityDetailsBinding.detailsBackBtn.setOnClickListener(view -> {
            onBackPressed();
        });

        tvShow = (TvShow) getIntent().getSerializableExtra("tvShow");
        checkTvShowWatchList();
        getTvShowDetails();
    }

    private void getTvShowDetails() {
        activityDetailsBinding.setIsLoading(true);

        //get id from intent
        String detailsId = String.valueOf(tvShow.getId());

        //live-data observer
        tvShowDetailsViewModel.getLiveTvShowDetails(detailsId).observe(this, tvShowDetailResponse -> {
            activityDetailsBinding.setIsLoading(false);
            if (tvShowDetailResponse.getTvShowDetails() != null) {
                if (tvShowDetailResponse.getTvShowDetails().getPictures() != null) {
                    loadImageSlider(tvShowDetailResponse.getTvShowDetails().getPictures());
                }
                //set the series image
                activityDetailsBinding.setTvShowImageUrl(tvShowDetailResponse.getTvShowDetails().getImagePath());
                activityDetailsBinding.detailsRoundedImgView.setVisibility(View.VISIBLE);

                //set show description
                activityDetailsBinding.setDescription(String.valueOf(HtmlCompat.fromHtml(
                        tvShowDetailResponse.getTvShowDetails().getDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY
                )));
                activityDetailsBinding.detailsDescription.setVisibility(View.VISIBLE);
                activityDetailsBinding.detailsTvReadMore.setVisibility(View.VISIBLE);

                //click -> read more => add more description
                //click-> read less => add less description
                activityDetailsBinding.detailsTvReadMore.setOnClickListener(view -> {
                    if (activityDetailsBinding.detailsTvReadMore.getText().toString().equals("Read More")) {
                        activityDetailsBinding.detailsDescription.setMaxLines(Integer.MAX_VALUE);
                        activityDetailsBinding.detailsDescription.setEllipsize(null);
                        activityDetailsBinding.detailsTvReadMore.setText("Read Less");
                    } else {
                        activityDetailsBinding.detailsDescription.setMaxLines(4);
                        activityDetailsBinding.detailsDescription.setEllipsize(TextUtils.TruncateAt.END);// add ....
                        activityDetailsBinding.detailsTvReadMore.setText("Read More");
                    }
                });

                //add rating of series
                activityDetailsBinding.setRating(
                        String.format(Locale.getDefault(), "%.2f", Double.parseDouble(tvShowDetailResponse.getTvShowDetails().getRating())));
                if (tvShowDetailResponse.getTvShowDetails().getGenres() != null) {
                    activityDetailsBinding.setGenre(tvShowDetailResponse.getTvShowDetails().getGenres()[0]);
                } else {
                    activityDetailsBinding.setGenre("N/A");
                }

                //add date of Runtime
                activityDetailsBinding.setRuntime(tvShowDetailResponse.getTvShowDetails().getRuntime() + "Min");

                //show all View
                activityDetailsBinding.detailDivider.setVisibility(View.VISIBLE);
                activityDetailsBinding.detailMisc.setVisibility(View.VISIBLE);
                activityDetailsBinding.detailDivider2.setVisibility(View.VISIBLE);
                activityDetailsBinding.detailEpisodeBtn.setVisibility(View.VISIBLE);
                activityDetailsBinding.detailWebsiteBtn.setVisibility(View.VISIBLE);


                //open custom tab
                activityDetailsBinding.detailWebsiteBtn.setOnClickListener(view -> {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(this, Uri.parse(tvShowDetailResponse.getTvShowDetails().getUrl()));
                });

                //open bottom-sheet
                activityDetailsBinding.detailEpisodeBtn.setOnClickListener(view -> {
                    if (episodeBtmSheet == null) {
                        episodeBtmSheet = new BottomSheetDialog(Details.this);

                        //inflate the layout of bottom sheet
                        bottomsheetLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(Details.this),
                                R.layout.bottomsheet_layout,
                                findViewById(R.id.btmSheetEpisodeContainer),
                                false);
                        episodeBtmSheet.setContentView(bottomsheetLayoutBinding.getRoot());
                        bottomsheetLayoutBinding.btmSheetRecyclerview.setAdapter(new EpisodeAdapter(tvShowDetailResponse.getTvShowDetails().getEpisodesList()));
                        bottomsheetLayoutBinding.bottomSheetTitle.setText(String.format("Episodes | %s", tvShow.getName()));
                        bottomsheetLayoutBinding.bottomSheetImgClose.setOnClickListener(view1 -> {
                            episodeBtmSheet.dismiss();
                        });
                    }
                    //show btm-sheet
                    episodeBtmSheet.show();
                });


                activityDetailsBinding.detailsWatchList.setOnClickListener(view -> {
                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    if (isTvShowAvailableInWatchList) {
                        compositeDisposable.add(tvShowDetailsViewModel.removeTvFromWatchList(tvShow)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    isTvShowAvailableInWatchList = false;
                                    TempDataHolder.isDataUpdated = true;
                                    activityDetailsBinding.detailsWatchList.setImageResource(R.drawable.watchlist);
                                    Toast.makeText(this, "Remove from watchlist", Toast.LENGTH_SHORT).show();
                                    compositeDisposable.dispose();
                                }));
                    } else {
                        compositeDisposable.add(tvShowDetailsViewModel.addToWatchList(tvShow)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    TempDataHolder.isDataUpdated = true;
                                    activityDetailsBinding.detailsWatchList.setImageResource(R.drawable.ic_baseline_check_24);
                                    Toast.makeText(this, "Added to watchlist", Toast.LENGTH_SHORT).show();
                                    compositeDisposable.dispose();
                                }));
                    }
                });

                loadBasicDetails();
            }
        });
    }

    //banner sliding Images with ViewPager
    private void loadImageSlider(String[] sliderImage) {
        activityDetailsBinding.detailsViewPager.setOffscreenPageLimit(1);
        activityDetailsBinding.detailsViewPager.setAdapter(new ImageSlideAdapter(sliderImage));
        activityDetailsBinding.detailsViewPager.setVisibility(View.VISIBLE);
        activityDetailsBinding.viewFadingBg.setVisibility(View.VISIBLE);

        //indicator in viewpager
        setupSliderIndicator(sliderImage.length);

        //current position of indicator
        activityDetailsBinding.detailsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setUpCurrentSliderIndicator(position);
            }
        });
    }

    //setup indicator
    private void setupSliderIndicator(int count) {
        ImageView[] indicator = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 8, 0);
        for (int i = 0; i < indicator.length; i++) {
            indicator[i] = new ImageView(getApplicationContext());
            indicator[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.lsider_imaage_indicator));
            indicator[i].setLayoutParams(layoutParams);
            activityDetailsBinding.slideIndicator.addView(indicator[i]);
        }
        activityDetailsBinding.slideIndicator.setVisibility(View.VISIBLE);
        setUpCurrentSliderIndicator(0);
    }

    //setup current indicator
    private void setUpCurrentSliderIndicator(int pos) {
        int childCount = activityDetailsBinding.slideIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imgView = (ImageView) activityDetailsBinding.slideIndicator.getChildAt(i);
            if (i == pos) {
                imgView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slider_inactive_indicator));
            } else {
                imgView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.lsider_imaage_indicator));
            }
        }
    }

    //name+network+status+startDate
    private void loadBasicDetails() {
        activityDetailsBinding.setTvShowName(tvShow.getName());
        activityDetailsBinding.setNetworkCountry(tvShow.getNetwork() + " (" + tvShow.getCountry() + ")");
        activityDetailsBinding.setStatus(tvShow.getStatus());
        activityDetailsBinding.setStartDate(tvShow.getStartData());
        activityDetailsBinding.detailsSeriesName.setVisibility(View.VISIBLE);
        activityDetailsBinding.detailsNetworkCountry.setVisibility(View.VISIBLE);
        activityDetailsBinding.detailsSeriesStarted.setVisibility(View.VISIBLE);
        activityDetailsBinding.detailsSeriesStatus.setVisibility(View.VISIBLE);
    }


    private void checkTvShowWatchList() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTvShowFromWatchlist(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow -> {
                    isTvShowAvailableInWatchList = true;
                    activityDetailsBinding.detailsWatchList.setImageResource(R.drawable.ic_baseline_check_24);
                    compositeDisposable.dispose();
                }));
    }

}