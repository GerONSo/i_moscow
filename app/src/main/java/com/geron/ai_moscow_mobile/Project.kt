package com.geron.ai_moscow_mobile

import com.squareup.moshi.Json

data class Project(
    @field:Json(name = "id") val id: String = "",
    @field:Json(name = "name") val name: String = "",
    @field:Json(name = "master_id") val masterId: String = "",
    @field:Json(name = "chat_id")val chatId: String = "",
    @field:Json(name = "slaves_id") val listSlavesId: List<String> = listOf(),
    @field:Json(name = "photo_link") val photoLink: String = "",
    @field:Json(name = "metadata") val metaData: ProjectMetadata? = null,
    @field:Json(name = "tags") val tags: List<String> = listOf()
)

data class ProjectMetadata(
    @field:Json(name = "description") val description: String
)