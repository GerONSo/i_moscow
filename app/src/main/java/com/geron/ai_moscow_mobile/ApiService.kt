package com.geron.ai_moscow_mobile

import com.geron.ai_moscow_mobile.data_classes.Cookie
import com.geron.ai_moscow_mobile.data_classes.Event
import com.geron.ai_moscow_mobile.data_classes.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("/get_events_list")
    suspend fun getEventsList(): Response<List<Event>>

    @POST("/authorize")
    suspend fun authorize(@Body user: User): Response<Cookie>
}