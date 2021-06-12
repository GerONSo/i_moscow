package com.geron.ai_moscow_mobile.data_classes

import com.squareup.moshi.Json

data class User (
    @field:Json(name = "id") var login: String,
    @field:Json(name = "password") var password: String
)
