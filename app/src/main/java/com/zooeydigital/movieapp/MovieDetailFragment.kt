package com.zooeydigital.movieapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_movie_detail.*

class MovieDetailFragment : Fragment() {

    companion object {
        const val MOVIE_ID_KEY = "MOVIE_ID_KEY"
    }

    private val movieDetailViewModel: MovieDetailViewModel by viewModels()
    private var movieId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getInt(MOVIE_ID_KEY)
        }

        movieDetailViewModel.movieLiveData.observe(this, Observer {
            when (it) {
                MovieResponse.Loading -> showLoading(true)
                is MovieResponse.OnSuccess -> loadMovieDetail(it.data)
                is MovieResponse.OnError -> showErrorMessage(it.message)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onResume() {
        super.onResume()
        movieId?.let {
            movieDetailViewModel.getMovieDetail(it)
        }
    }

    private fun loadMovieDetail(movie: Movie) {
        showLoading(false)
        error_text.visibility = View.INVISIBLE

        Glide.with(requireContext()).load("${BuildConfig.IMAGES_BASE_URL}${movie.posterPath}")
            .centerCrop().into(movie_poster)
        movie_title.text = movie.title
        movie_overview.text = movie.overview

        var genres = ""
        movie.genres.forEach {
            genres = "${it.name} $genres"
        }
        movie_genre.text = genres
    }

    private fun showLoading(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showErrorMessage(message: String) {
        showLoading(false)
        error_text.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
}