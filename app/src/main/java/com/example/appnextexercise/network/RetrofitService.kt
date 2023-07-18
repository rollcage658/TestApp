package com.example.appnextexercise.network

import com.example.appnextexercise.model.WeeklyResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Retrofit service for making requests
interface RetrofitService {

    // Defines a GET request to the "daily_data" endpoint, which returns a WeeklyResponse object
    @GET("daily_data")
    fun getDailyData() : retrofit2.Call<WeeklyResponse>

    companion object {
        // The base URL for the API
        val baseUrl = "https://apimocha.com/nextandroid/"
        // A nullable instance of the RetrofitService
        var retrofitService: RetrofitService? = null

        // Returns an instance of the RetrofitService, creating it if it doesn't already exist
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}