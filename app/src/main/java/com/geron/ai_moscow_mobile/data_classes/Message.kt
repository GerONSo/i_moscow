package com.geron.ai_moscow_mobile.data_classes

import com.squareup.moshi.Json

data class Message(
    @field:Json(name = "id") var id: String,
    @field:Json(name = "from_user") var fromUser: String,
    @field:Json(name = "message_type") var messageType: String,
    @field:Json(name = "metadata") var messageMetadata: MessageMetadata
)

data class SendMessage(
    @field:Json(name = "chat_id") var chatId: String,
    @field:Json(name = "message_type") val messageType: String,
    @field:Json(name = "metadata") val messageMetadata: MessageMetadata
)

data class MessageMetadata(
    @field:Json(name = "message") var message: String
)
