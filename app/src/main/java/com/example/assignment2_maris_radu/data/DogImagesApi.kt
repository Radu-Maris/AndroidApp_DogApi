package com.example.assignment2_maris_radu.data

import com.example.assignment2_maris_radu.data.dto.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DogImagesApi {
    @GET("api/breed/{breed_name}/images")
    suspend fun getBreedsImages(@Path("breed_name") breedName: String): ImageResponse
}