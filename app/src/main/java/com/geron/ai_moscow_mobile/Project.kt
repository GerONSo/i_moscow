package com.geron.ai_moscow_mobile

data class Project(
    val id: String = "",
    val name: String = "",
    val masterId: String = "",
    val chatId: String = "",
    val listSlavesId: List<String> = listOf(),
    val photoLink: String = "",
    val metaData: ProjectMetadata? = null,
    val tags: List<String> = listOf()
)

data class ProjectMetadata(
    val description: String
)