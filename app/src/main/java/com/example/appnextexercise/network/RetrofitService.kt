package com.example.appnextexercise.network

import com.example.appnextexercise.model.WeeklyResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Retrofit service for making requests
interface RetrofitService {

    @GET("daily_data")
    fun getDailyData() : retrofit2.Call<WeeklyResponse>

    companion object {
        val baseUrl = "https://apimocha.com/nextandroid/"
        var retrofitService: RetrofitService? = null

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