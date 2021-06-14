package com.geron.ai_moscow_mobile.data_classes

import com.squareup.moshi.Json

data class ChatId (
    @field:Json(name = "chat_id") val chatId: String
)