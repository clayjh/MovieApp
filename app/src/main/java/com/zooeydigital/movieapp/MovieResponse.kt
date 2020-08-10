package com.zooeydigital.movieapp

sealed class MovieResponse<out T> {
    object Loading : MovieResponse<Nothing>()
    data class OnSuccess<T>(val data: T) : MovieResponse<T>()
    data class OnError(val message: String) : MovieResponse<Nothing>()
}