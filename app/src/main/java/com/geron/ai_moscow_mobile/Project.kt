package com.geron.ai_moscow_mobile

data class Project(
    val id: String,
    val name: String,
    val masterId: String,
    val chatId: String,
    val listSlavesId: List<String>,
    val photoLink: String,
    val metaData: ProjectMetadata,
    val tags: List<String>
)

data class ProjectMetadata(
    val description: String
)