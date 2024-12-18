package com.example.assignment2_maris_radu.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInterface {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dog.ceo/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: DogApi = retrofit.create(DogApi::class.java)
    val dogImagesApi: DogImagesApi = retrofit.create(DogImagesApi::class.java)
}