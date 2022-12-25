package com.example.tvshow.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowDetails {

    @SerializedName("url")
    private String url;

    @SerializedName("description")
    private String description;

    @SerializedName("runtime")
    private String runtime;

    @SerializedName("image_path")
    private String imagePath;

    @SerializedName("rating")
    private String rating;

    @SerializedName("genres")
    private String[] genres;

    @SerializedName("pictures")
    private String[] pictures;

    @SerializedName("episodes")
    private List<Episode> episodesList;

    public TvShowDetails(String url, String description, String runtime, String imagePath, String rating, String[] genres, String[] pictures, List<Episode> episodesList) {
        this.url = url;
        this.description = description;
        this.runtime = runtime;
        this.imagePath = imagePath;
        this.rating = rating;
        this.genres = genres;
        this.pictures = pictures;
        this.episodesList = episodesList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }

    public List<Episode> getEpisodesList() {
        return episodesList;
    }

    public void setEpisodesList(List<Episode> episodesList) {
        this.episodesList = episodesList;
    }
}
