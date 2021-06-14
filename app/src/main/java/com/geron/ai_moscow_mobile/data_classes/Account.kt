package com.geron.ai_moscow_mobile.data_classes

import android.net.MailTo
import android.util.EventLogTags
import com.squareup.moshi.Json
import java.net.PasswordAuthentication

data class Account (
    @field:Json(name = "id") var id: String,
    @field:Json(name = "name") var name: String,
    @field:Json(name = "mail") var mail: String,
    @field:Json(name = "password") var password: String,
    @field:Json(name = "tags") var tags: List<String>,
    @field:Json(name = "photo_link") var photoLink: String,
    @field:Json(name = "my_events") var myEvents: List<String>,
    @field:Json(name = "master_project_ids") var masterProjectIds: List<String>,
    @field:Json(name = "slave_project_ids") var slaveProjectIds: List<String>,
    @field:Json(name = "chat_ids") var chatIds: List<String>,
    @field:Json(name = "metadata") var metadata: AccountMetadata
)

data class AccountMetadata (
    @field:Json(name = "description") var description: String
)