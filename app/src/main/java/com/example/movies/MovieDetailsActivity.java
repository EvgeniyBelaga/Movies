package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MovieDetailsActivity extends AppCompatActivity {

    ImageView mIvMovieImage;
    TextView mTvMovieTitle;
    TextView mTvMovieOverview;
    TextView mTvReleaseDate;
    TextView mTvPopularity;
    Button mBtnAddToFavorites;

    final String IMAGE_URL_PREFIX= "https://image.tmdb.org/t/p/w500/";

    Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        initilizeLayout();

        displayDetails();

        initilizeAddtoFavoriteButton();
    }

    private void initilizeLayout(){

        mIvMovieImage= findViewById(R.id.iv_details_image);
        mTvMovieTitle= findViewById(R.id.tv_details_title);
        mTvMovieOverview= findViewById(R.id.tv_details_overview);
        mTvReleaseDate= findViewById(R.id.tv_details_release_date);
        mTvPopularity= findViewById(R.id.tv_details_popularity);
        mBtnAddToFavorites= findViewById(R.id.btn_details_add_to_favorites);
    }

    private void displayDetails(){


            if(Utils.mCurrentMovie!=null){
                mMovie= Utils.mCurrentMovie;
                String imageUrl= IMAGE_URL_PREFIX+mMovie.getPosterPath();
                Picasso.get().load(imageUrl).into(mIvMovieImage);

                String title= "Title: "+mMovie.getTitle();
                String overview= "Overview: "+mMovie.getOverview();
                String releaseDate= "Release date : "+mMovie.getDateRealise();
                String popularity= "Popularity: "+mMovie.getPopularity();

                mTvMovieTitle.setText(title);
                mTvMovieOverview.setText(overview);
                mTvReleaseDate.setText(releaseDate);
                mTvPopularity.setText(popularity);
            }

    }

    private void initilizeAddtoFavoriteButton(){

        boolean isExists= false;
        if(Utils.mCurrentFavoriteList.size()>0){
            for(Movie movie: Utils.mCurrentFavoriteList){
                if(movie.getId()==mMovie.getId()){
                    isExists= true;
                }
            }
        }

        if(isExists){
            mBtnAddToFavorites.setEnabled(false);
        }
        else{
            mBtnAddToFavorites.setEnabled(true);
        }

        mBtnAddToFavorites.setOnClickListener(view -> {
            Utils.mCurrentFavoriteList.add(mMovie);
            mBtnAddToFavorites.setEnabled(false);
        });
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent intent= new Intent(MovieDetailsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}