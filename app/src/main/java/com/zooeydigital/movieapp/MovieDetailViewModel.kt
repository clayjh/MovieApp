package com.zooeydigital.movieapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailViewModel : ViewModel() {

    private val movieAPIService: MovieAPIService = MovieAPIService.create()

    val movieLiveData: MutableLiveData<MovieResponse<Movie>> = MutableLiveData()

    fun getMovieDetail(id: Int) {
        movieLiveData.value = MovieResponse.Loading

        movieAPIService.getMovieDetail(id).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                response.body()?.let {
                    movieLiveData.value = MovieResponse.OnSuccess(it)
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                movieLiveData.value = MovieResponse.OnError(t.message ?: "Error Getting Movie")
            }

        })
    }
}