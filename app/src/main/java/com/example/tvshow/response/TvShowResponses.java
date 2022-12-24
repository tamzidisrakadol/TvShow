package com.example.tvshow.response;

import com.example.tvshow.model.TvShow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowResponses {

    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int totalPage;

    @SerializedName("tv_shows")
    private List<TvShow> listTvShow;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<TvShow> getListTvShow() {
        return listTvShow;
    }

    public void setListTvShow(List<TvShow> listTvShow) {
        this.listTvShow = listTvShow;
    }
}
