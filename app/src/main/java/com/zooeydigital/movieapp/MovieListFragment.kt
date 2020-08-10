package com.zooeydigital.movieapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.zooeydigital.movieapp.MovieDetailFragment.Companion.MOVIE_ID_KEY
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.android.synthetic.main.fragment_movie_list.progress


class MovieListFragment : Fragment(), MoviesAdapter.MovieListener {

    companion object {
        const val NUM_COLUMNS = 2
    }

    private val movieListViewModel: MovieListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieListViewModel.movieListLiveData.observe(this, Observer {
            when (it) {
                MovieResponse.Loading -> showLoading(true)
                is MovieResponse.OnSuccess -> loadMovies(it.data)
                is MovieResponse.OnError -> showErrorMessage(it.message)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        movieListViewModel.getPopularMovies()
    }

    private fun loadMovies(movies: List<Movie>) {
        showLoading(false)
        error_text.visibility = View.INVISIBLE

        val moviesAdapter = MoviesAdapter(requireContext(), this)
        val gridLayoutManager = GridLayoutManager(context, NUM_COLUMNS)
        recycler_view.layoutManager = gridLayoutManager
        recycler_view.addItemDecoration(getDivider(LinearLayout.HORIZONTAL))
        recycler_view.addItemDecoration(getDivider(LinearLayout.VERTICAL))
        recycler_view.adapter = moviesAdapter
        moviesAdapter.setMovies(movies)
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

    private fun getDivider(orientation: Int): DividerItemDecoration {
        return DividerItemDecoration(
            requireContext(),
            orientation
        )
    }

    override fun onMovieClicked(movie: Movie) {
        val bundle = Bundle()
        bundle.putInt(MOVIE_ID_KEY, movie.id)
        findNavController().navigate(R.id.movieDetailFragment, bundle)
    }
}