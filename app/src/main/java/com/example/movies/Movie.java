package com.example.movies;

public class Movie {

    private int id;
    private String title;
    private String languege;
    private boolean isAdult;
    private String overview;
    private String popularity;
    private String dateRealise;
    private boolean isFavorite;
    private String posterPath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguege() {
        return languege;
    }

    public void setLanguege(String languege) {
        this.languege = languege;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getDateRealise() {
        return dateRealise;
    }

    public void setDateRealise(String dateRealise) {
        this.dateRealise = dateRealise;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", languege='" + languege + '\'' +
                ", isAdult=" + isAdult +
                ", overview='" + overview + '\'' +
                ", popularity='" + popularity + '\'' +
                ", dateRealise='" + dateRealise + '\'' +
                ", isFavorite=" + isFavorite +
                ", posterPath='" + posterPath + '\'' +
                '}';
    }
}
