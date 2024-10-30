package com.event.dicodingeventapp.data.retrofit

import com.event.dicodingeventapp.data.response.DetailEventResponse
import com.event.dicodingeventapp.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int,
        @Query("q") q: String
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvents(
        @Path("id") id: String
    ): Call<DetailEventResponse>

}