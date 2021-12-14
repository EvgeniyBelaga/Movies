package com.example.movies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MoviesViewHolder> {

    private Context mContext;
    private List<Movie> mMovieList;
    private MyViewModel mViewModel;

    final String IMAGE_URL_PREFIX= "https://image.tmdb.org/t/p/w500/";

    public MoviesRecyclerAdapter(Context context, List<Movie> movieList, MyViewModel myViewModel){
        mContext= context;
        mMovieList= movieList;
        mViewModel= myViewModel;
    }

    @Override
    public MoviesRecyclerAdapter.MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false);
        return new MoviesRecyclerAdapter.MoviesViewHolder(v);

    }

    @Override
    public void onBindViewHolder(MoviesRecyclerAdapter.MoviesViewHolder holder, int position) {

        Movie movie= mMovieList.get(position);

        String title= movie.getTitle();
        String releaseDate= movie.getDateRealise();
        String popularity= movie.getPopularity();

        holder.mTvTitle.setText(title);
        holder.mTvReleaseDate.setText(releaseDate);
        holder.mTvPopularity.setText(popularity);

        String imageUrl= IMAGE_URL_PREFIX+movie.getPosterPath();
        Picasso.get().load(imageUrl).into(holder.mIvMovieImage);

        holder.itemView.setOnClickListener(view -> {


            Utils.mCurrentMovie= movie;
            AppCompatActivity activity= (AppCompatActivity) mContext;
            Intent intent= new Intent(activity, MovieDetailsActivity.class);
            activity.startActivity(intent);
            activity.finish();
        });

    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvTitle;
        public TextView mTvReleaseDate;
        public TextView mTvPopularity;
        public ImageView mIvMovieImage;

        public MoviesViewHolder(View itemView) {
            super(itemView);

            mTvTitle = itemView.findViewById(R.id.tv_item_title);
            mTvReleaseDate = itemView.findViewById(R.id.tv_item_release_date);
            mTvPopularity = itemView.findViewById(R.id.tv_item_popularity);
            mIvMovieImage = itemView.findViewById(R.id.iv_item_image);

        }
    }
}
