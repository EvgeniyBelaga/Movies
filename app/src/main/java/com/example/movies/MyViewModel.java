package com.example.movies;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MyViewModel extends ViewModel {

    private MutableLiveData<Integer> pageMld;
    private MutableLiveData<Integer> totalPagesMld;
    private MutableLiveData<List<Movie>> movieListLd;
    private MutableLiveData<Boolean> isLoadedMld= new MutableLiveData<>();
    private MutableLiveData<Boolean> isErrorMld= new MutableLiveData<>();
    private MutableLiveData<String> sortCategoryMld;

    public MutableLiveData<Integer> getPageMld() {
        if(pageMld==null){
            pageMld= new MutableLiveData<>();
        }
        return pageMld;
    }

    public MutableLiveData<Integer> getTotalPagesMld() {
        if(totalPagesMld==null){
            totalPagesMld= new MutableLiveData<>();
        }
        return totalPagesMld;
    }

    public MutableLiveData<Boolean> getIsLoaded() {
        if(isLoadedMld==null){
            isLoadedMld= new MutableLiveData<>();
        }
        return isLoadedMld;
    }


    public MutableLiveData<Boolean> getIsErrorMld() {
        if(isErrorMld==null){
            isErrorMld= new MutableLiveData<>();
        }
        return isErrorMld;
    }

    public void setErrorMld(boolean isError){
        isErrorMld.setValue(isError);
    }

    public MutableLiveData<List<Movie>> getMovieListLd(){
        if(movieListLd==null){
            movieListLd= new MutableLiveData<>();
        }
        return movieListLd;
    }

    public void setMovieListLd(List<Movie> movieList){
        movieListLd.setValue(movieList);
    }

    public MutableLiveData<String> getSortCategoryMld() {
        if(sortCategoryMld==null){
            sortCategoryMld= new MutableLiveData<>();
        }
        return sortCategoryMld;
    }


    public void setSortcategoryMld(String sortCategory){
        sortCategoryMld.setValue(sortCategory);
    }

    public void getMovies(String apiKey, int page, String sortingCategory) {

        isLoadedMld.setValue(false);
        isErrorMld.setValue(false);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiMoviesInterface.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        ApiMoviesInterface apiMoviesInterface = retrofit.create(ApiMoviesInterface.class);

        Call<String> userCall;
        if(sortingCategory.length()==0) {
            userCall = apiMoviesInterface.getMovies(ApiMoviesInterface.CATEGORY, apiKey, page);
        }
        else{
            userCall = apiMoviesInterface.getSortedMovies(apiKey, page, sortingCategory);
        }


        userCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {

                    if(response.code()==200){
                        isLoadedMld.setValue(true);
                        isErrorMld.setValue(false);
                        JSONObject jsonObject= new JSONObject(response.body());

                        if(jsonObject.has("page")) {
                            int page = jsonObject.getInt("page");
                            pageMld.setValue(page);
                        }

                        if(jsonObject.has("total_pages")) {
                            int totalPages = jsonObject.getInt("total_pages");
                            totalPagesMld.setValue(totalPages);
                        }

                        if(jsonObject.has("results")) {
                            JSONArray jsonArrayResults= jsonObject.getJSONArray("results");
                            if(jsonArrayResults!=null && jsonArrayResults.length()>0){
                                List<Movie> movieList= new ArrayList<>();
                                for(int i=0; i<jsonArrayResults.length(); i++){
                                    try {
                                        JSONObject jsonObjectMovie= jsonArrayResults.getJSONObject(i);
                                        if(jsonObjectMovie!=null){
                                            Movie movie= new Movie();
                                            if(jsonObjectMovie.has("id")){
                                                int id= jsonObjectMovie.getInt("id");
                                                movie.setId(id);
                                            }
                                            if(jsonObjectMovie.has("adult")){
                                                boolean isAdult= jsonObjectMovie.getBoolean("adult");
                                                movie.setAdult(isAdult);
                                            }
                                            if(jsonObjectMovie.has("original_language")){
                                                String  language= jsonObjectMovie.getString("original_language");
                                                movie.setLanguege(language);
                                            }
                                            if(jsonObjectMovie.has("title")){
                                                String  title= jsonObjectMovie.getString("title");
                                                movie.setTitle(title);
                                            }
                                            if(jsonObjectMovie.has("overview")){
                                                String  overview= jsonObjectMovie.getString("overview");
                                                movie.setOverview(overview);
                                            }
                                            if(jsonObjectMovie.has("popularity")){
                                                String  popularity= jsonObjectMovie.getString("popularity");
                                                movie.setPopularity(popularity);
                                            }
                                            if(jsonObjectMovie.has("release_date")){
                                                String  releaseDate= jsonObjectMovie.getString("release_date");
                                                movie.setDateRealise(releaseDate);
                                            }
                                            if(jsonObjectMovie.has("poster_path")){
                                                String  posterPath= jsonObjectMovie.getString("poster_path");
                                                movie.setPosterPath(posterPath);
                                            }
                                            movieList.add(movie);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                movieListLd.setValue(movieList);
                            }
                        }
                    }
                    else{
                        isLoadedMld.setValue(true);
                        isErrorMld.setValue(true);
                    }
                } catch (Exception e) {
                    isLoadedMld.setValue(true);
                    isErrorMld.setValue(true);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                isLoadedMld.setValue(true);
                isErrorMld.setValue(true);

            }
        });
    }
}
