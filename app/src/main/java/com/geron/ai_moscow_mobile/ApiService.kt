package com.geron.ai_moscow_mobile

import com.geron.ai_moscow_mobile.data_classes.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("/get_events_list")
    suspend fun getEventsList(): Response<List<Event>>

    @POST("/authorize")
    suspend fun authorize(@Body user: User): Response<Cookie>

    @POST("/create_account")
    suspend fun register(@Body user: User): Response<User>

    @GET("/get_my_events/{cookie}")
    suspend fun getMyEventsList(@Path("cookie") cookie: String): Response<List<Event>>

    @POST("/join_to_event/{cookie}")
    suspend fun joinToEvent(@Path("cookie") cookie: String, @Body eventId: EventID): Response<Event>

    @GET("/get_my_account/{cookie}")
    suspend fun getMyAccount()

    @GET("/get_projects_list")
    suspend fun getProjectsList(): Response<List<Project>>

    @GET("/get_accounts_list")
    suspend fun getAccountsList(): Response<List<Account>>

    @GET("/get_my_master_projects/{cookie}")
    suspend fun getMyMasterProjects(@Path("cookie") cookie: String): Response<List<Project>>

    @GET("/get_my_slave_projects/{cookie}")
    suspend fun getMySlaveProjects(@Path("cookie") cookie: String): Response<List<Project>>

}