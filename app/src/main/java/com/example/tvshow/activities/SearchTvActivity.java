package com.example.tvshow.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.tvshow.R;
import com.example.tvshow.adapters.ItemClickListener;
import com.example.tvshow.adapters.TvShowAdapters;
import com.example.tvshow.databinding.ActivitySearchTvBinding;
import com.example.tvshow.model.TvShow;
import com.example.tvshow.viewModel.SearchTvModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchTvActivity extends AppCompatActivity implements ItemClickListener {

   ActivitySearchTvBinding activitySearchTvBinding;
   SearchTvModel searchTvModel;
   List<TvShow> tvShowList = new ArrayList<>();
   TvShowAdapters tvShowAdapters;
   int currentPage = 1;
   int totalPage = 1;
   Timer timer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchTvBinding = ActivitySearchTvBinding.inflate(getLayoutInflater());
        setContentView(activitySearchTvBinding.getRoot());

        activitySearchTvBinding.searchRecyclerview.setHasFixedSize(true);
        searchTvModel = new ViewModelProvider(this).get(SearchTvModel.class);
        activitySearchTvBinding.searchListBackBtn.setOnClickListener(view -> {
            onBackPressed();
        });
        tvShowAdapters = new TvShowAdapters(tvShowList,this);
        activitySearchTvBinding.searchRecyclerview.setAdapter(tvShowAdapters);
        activitySearchTvBinding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (timer != null){
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()){
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                 currentPage = 1;
                                 totalPage =1;
                                 searchShow(editable.toString());
                                }
                            });
                        }
                    },800);
                }else{
                    tvShowList.clear();
                    tvShowAdapters.notifyDataSetChanged();
                }
            }
        });

        activitySearchTvBinding.searchRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activitySearchTvBinding.searchRecyclerview.canScrollVertically(1)){
                    if (!activitySearchTvBinding.searchEditText.getText().toString().isEmpty()){
                        if (currentPage < totalPage){
                            currentPage += 1;
                            searchShow(activitySearchTvBinding.searchEditText.getText().toString());
                        }
                    }

                }
            }
        });
        activitySearchTvBinding.searchEditText.requestFocus();


    }


    private void searchShow(String query){
        toggleLoading();
        searchTvModel.searchTvResponse(query,currentPage).observe(this,tvShowResponses -> {
            toggleLoading();
            if (tvShowResponses !=null){
                totalPage = tvShowResponses.getTotalPage();
                if (tvShowResponses.getListTvShow()!=null){
                    int oldCount = tvShowList.size();
                    tvShowList.addAll(tvShowResponses.getListTvShow());
                    tvShowAdapters.notifyItemRangeInserted(oldCount,tvShowList.size());
                }
            }
        });
    }


    private void toggleLoading(){
        if(currentPage==1){
            activitySearchTvBinding.setIsLoading(activitySearchTvBinding.getIsLoading() == null || !activitySearchTvBinding.getIsLoading());
        }else{
            activitySearchTvBinding.setIsLoadingMore(activitySearchTvBinding.getIsLoadingMore() == null || !activitySearchTvBinding.getIsLoadingMore());
        }
    }

    @Override
    public void onItemClick(TvShow tvShow) {
        Intent intent = new Intent(SearchTvActivity.this,Details.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }
}