package com.example.tvshow.response;

import com.example.tvshow.model.TvShowDetails;
import com.google.gson.annotations.SerializedName;

public class TvShowDetailResponse{

    @SerializedName("tvShow")
    private TvShowDetails tvShowDetails;

    public TvShowDetails getTvShowDetails(){
        return tvShowDetails;
    }

}
