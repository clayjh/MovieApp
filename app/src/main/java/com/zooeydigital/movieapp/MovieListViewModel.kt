package com.zooeydigital.movieapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListViewModel : ViewModel() {

    private val movieAPIService: MovieAPIService = MovieAPIService.create()

    val movieListLiveData: MutableLiveData<MovieResponse<List<Movie>>> = MutableLiveData()

    fun getPopularMovies() {
        movieListLiveData.value = MovieResponse.Loading

        movieAPIService.getPopularMovies().enqueue(object : Callback<MovieResult> {
            override fun onResponse(call: Call<MovieResult>, response: Response<MovieResult>) {
                response.body()?.results?.let {
                    movieListLiveData.value = MovieResponse.OnSuccess(it)
                }
            }

            override fun onFailure(call: Call<MovieResult>, t: Throwable) {
                movieListLiveData.value = MovieResponse.OnError(t.message ?: "Error Getting Movies")
            }
        })
    }
}