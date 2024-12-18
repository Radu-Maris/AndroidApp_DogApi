package com.example.assignment2_maris_radu.data

import com.example.assignment2_maris_radu.data.dto.BreedResponse
import retrofit2.http.GET

interface DogApi {
    @GET("api/breeds/list/all")
    suspend fun getBreeds(): BreedResponse
}