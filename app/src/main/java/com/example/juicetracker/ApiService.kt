package com.example.juicetracker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("channels/2856744/feeds.json")
    fun getLatestValue(
        @Query("api_key") apiKey: String,
        @Query("results") results: Int = 1 // Get the latest value
    ): Call<ThingSpeakResponse>
}

data class ThingSpeakResponse(val feeds: List<Feed>?)
data class Feed(val field1: String?)  // field1 is a string in JSON


