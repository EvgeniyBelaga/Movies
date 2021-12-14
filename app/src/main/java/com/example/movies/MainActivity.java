package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView mTvPage;
    RecyclerView mRvMovieList;
    Button mBtnPrevious;
    Button mBtnNext;
    ProgressBar mProgressBar;
    TextView mTvSortResults;
    Button mBtnMyFavorites;
    Button mBtnSortByDate;
    Button mBtnSortByPopularity;
    Button mBtnResetSorting;
    LinearLayout mLlPagesNav;
    TextView mTvError;

    MyViewModel mViewModel;

    MoviesRecyclerAdapter mMoviesRecyclerAdapter;

    int mCurrentPage;

    int mTotalPages;

    String mCurrentSortCategory= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        initilizeLayout();

        //getMovies(ApiMoviesInterface.API_KEY, 1);
        mViewModel.getMovies(ApiMoviesInterface.API_KEY, 1, "");

        setTvPage();

        getTotalPages();

        displayMovieList();

        displayProgressBar();

        initilizeNextPageButtonClickListener();

        initilizePreviousPageButtonClickListener();

        displaySortingTest();

        initilizeSortingButtons(mBtnSortByPopularity, ApiMoviesInterface.SORT_CATEGORY_POPULARITY);

        initilizeSortingButtons(mBtnSortByDate, ApiMoviesInterface.SORT_CATEGORY_RELEASE_DATE);

        initilizeSortingButtons(mBtnResetSorting, "");

        initilizeMyFavoritButtonClickListener();

        displayErrorMessage();
    }

    private void initilizeLayout(){

        mTvPage= findViewById(R.id.tv_page);
        mBtnPrevious= findViewById(R.id.btn_previous);
        mBtnNext= findViewById(R.id.btn_next);
        mRvMovieList= findViewById(R.id.rv_movie_list);
        mProgressBar= findViewById(R.id.progressBar);
        mTvSortResults= findViewById(R.id.tv_sort_result);
        mBtnMyFavorites= findViewById(R.id.btn_my_favorites);
        mBtnSortByPopularity= findViewById(R.id.btn_sort_by_popularity);
        mBtnSortByDate= findViewById(R.id.btn_sort_by_date);
        mBtnResetSorting= findViewById(R.id.btn_reset_sorting);
        mLlPagesNav= findViewById(R.id.ll_pages);
        mTvError= findViewById(R.id.tv_error);
    }

    private void setTvPage(){

        mViewModel.getPageMld().observe(MainActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                mCurrentPage= integer;
                mTvPage.setText("Page: "+mCurrentPage);

                if(integer==1){
                    mBtnPrevious.setEnabled(false);
                }
                else{
                    mBtnPrevious.setEnabled(true);
                }

                if(integer==mTotalPages){
                    mBtnNext.setEnabled(false);
                }
                else{
                    mBtnNext.setEnabled(true);
                }
            }
        });
    }

    private void getTotalPages(){

        mViewModel.getTotalPagesMld().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                mTotalPages= integer;

            }
        });
    }

    private void displayProgressBar(){

        mViewModel.getIsLoaded().observe(this, aBoolean -> {
            if(aBoolean){
                mProgressBar.setVisibility(View.GONE);
            }
            else{
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void displayMovieList(){

        mViewModel.getMovieListLd().observe(this, movies -> {

            if(movies!=null && movies.size()>0){
                mRvMovieList.setHasFixedSize(true);
                mRvMovieList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                mMoviesRecyclerAdapter= new MoviesRecyclerAdapter(MainActivity.this, movies, mViewModel);
                mRvMovieList.setAdapter(mMoviesRecyclerAdapter);
            }

        });
    }

    private void initilizeNextPageButtonClickListener(){

        mBtnNext.setOnClickListener(view -> {
            int nextPage= mCurrentPage+1;
            mViewModel.getMovies(ApiMoviesInterface.API_KEY, nextPage, mCurrentSortCategory);
        });
    }

    private void initilizePreviousPageButtonClickListener(){

        mBtnPrevious.setOnClickListener(view -> {
            int previousPage= mCurrentPage-1;
            mViewModel.getMovies(ApiMoviesInterface.API_KEY, previousPage, mCurrentSortCategory);
        });
    }

    private void initilizeSortingButtons(Button button, String sortingCategory){

        button.setOnClickListener(view -> {
            if(button.equals(mBtnSortByPopularity)){
                mViewModel.setSortcategoryMld(sortingCategory);
            }
            if(button.equals(mBtnSortByDate)){
                mViewModel.setSortcategoryMld(sortingCategory);
            }
            if(button.equals(mBtnResetSorting)){
                mViewModel.setSortcategoryMld("");
            }
            mViewModel.getMovies(ApiMoviesInterface.API_KEY, 1, sortingCategory);
        });

    }

    private void initilizeMyFavoritButtonClickListener(){

        if(Utils.mCurrentFavoriteList.size()>0){
            mBtnMyFavorites.setEnabled(true);
        }
        else{
            mBtnMyFavorites.setEnabled(false);
        }

        mBtnMyFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utils.mCurrentFavoriteList.size()>0) {
                    mViewModel.setMovieListLd(Utils.mCurrentFavoriteList);
                    mViewModel.setErrorMld(false);
                    mViewModel.setSortcategoryMld("My Favorites");
                }
            }
        });
    }

    private void displayErrorMessage(){

        mViewModel.getIsErrorMld().observe(this, aBoolean -> {

            if(aBoolean){
                mTvError.setVisibility(View.VISIBLE);
                mRvMovieList.setVisibility(View.GONE);
            }
            else{
                mTvError.setVisibility(View.GONE);
                mRvMovieList.setVisibility(View.VISIBLE);
            }
        });
    }

    private void displaySortingTest(){
        mViewModel.getSortCategoryMld().observe(this, s -> {
            String sortText= "";
            if(s.equals("My Favorites")){
                sortText= "My Favorites";
                mLlPagesNav.setVisibility(View.GONE);
            }
            else {
                mLlPagesNav.setVisibility(View.VISIBLE);
                if (s.equals(ApiMoviesInterface.SORT_CATEGORY_POPULARITY)) {
                    sortText = "Sorted by popularity";
                }
                if (s.equals(ApiMoviesInterface.SORT_CATEGORY_RELEASE_DATE)) {
                    sortText = "Sorted by release date";
                }
            }

            mTvSortResults.setText(sortText);
            mCurrentSortCategory= s;
        });
    }


}