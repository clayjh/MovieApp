package com.zooeydigital.movieapp

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface MovieAPIService {

    companion object {
        fun create(): MovieAPIService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(authOkHttpClient())
                .baseUrl(BuildConfig.MOVIE_BASE_URL)
                .build()

            return retrofit.create(MovieAPIService::class.java)
        }

        private fun authOkHttpClient(): OkHttpClient {
            val httpClient = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY

                httpClient.addInterceptor(logging)
            }
            httpClient.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request = chain.request()

                    val apiKeyUrl = request.url.newBuilder()
                        .addQueryParameter("api_key", BuildConfig.MOVIE_DB_KEY)
                        .build()

                    request = request.newBuilder()
                        .url(apiKeyUrl)
                        .build()

                    return chain.proceed(request)
                }
            })
            return httpClient.build()
        }
    }


    @GET("movie/popular")
    fun getPopularMovies(): Call<MovieResult>

    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") id: Int): Call<Movie>
}