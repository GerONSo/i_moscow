package com.geron.ai_moscow_mobile.data_classes

import com.squareup.moshi.Json

data class Event (
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "short_description") val shortDescription: String,
    @field:Json(name = "event_types") val eventTypes: List<String>,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "time") val time: String,
    @field:Json(name = "full_description") val fullDescription: String,
    @field:Json(name = "address") val address: String,
    @field:Json(name = "mail") val mail: String,
    @field:Json(name = "link") val link: String,
    @field:Json(name = "photo_link") val photoLink: String,
    @field:Json(name = "participants") val participants: List<String>
)
