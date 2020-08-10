package com.zooeydigital.movieapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_item.view.*

class MoviesAdapter(
    private val context: Context,
    private val movieListener: MovieListener
) : RecyclerView.Adapter<MoviesAdapter.MovieHolder>() {

    interface MovieListener {
        fun onMovieClicked(movie: Movie)
    }

    private var movies = emptyList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.movie_item, parent, false)

        return MovieHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.movieContainer.setOnClickListener {
            movieListener.onMovieClicked(movies[position])
        }
        Glide.with(context).load("${BuildConfig.IMAGES_BASE_URL}${movies[position].posterPath}")
            .centerCrop().into(holder.movieImage)
        holder.movieTitle.text = movies[position].title
    }

    fun setMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieContainer: ConstraintLayout = view.movie_container
        val movieImage: ImageView = view.movie_image
        val movieTitle: TextView = view.movie_title
    }
}