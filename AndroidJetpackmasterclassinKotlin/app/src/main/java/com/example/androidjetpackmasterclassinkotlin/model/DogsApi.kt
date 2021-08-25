package com.example.androidjetpackmasterclassinkotlin.model

import io.reactivex.Single
import retrofit2.http.GET

interface DogsApi {
    @GET("DevTides/DogsApi/master/dogs.json")
    fun getAll(): Single<List<DogModel>>
}