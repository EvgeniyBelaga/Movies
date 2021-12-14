package com.example.movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiMoviesInterface {

    final String BASE_URL= "https://api.themoviedb.org/";
    final String API_KEY= "2c46288716a18fb7aadcc2a801f3fc6b";
    final String CATEGORY= "popular";
    final String SORT_CATEGORY_RELEASE_DATE= "release_year.desc";
    final String SORT_CATEGORY_POPULARITY= "popularity.desc";

    @GET("3/movie/{category}")
    Call<String> getMovies(@Path("category") String category, @Query("api_key") String apiKey,
                           @Query("page") int page);


    @GET("3/discover/movie")
    Call<String> getSortedMovies(/*@Path("category") String category, */@Query("api_key") String apiKey,
                           @Query("page") int page, @Query("sort_by") String sortCategory);


}
