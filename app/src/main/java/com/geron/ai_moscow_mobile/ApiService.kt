package com.geron.ai_moscow_mobile

import com.geron.ai_moscow_mobile.data_classes.Event
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/get_events_list")
    suspend fun getEventsList(): Response<List<Event>>
}