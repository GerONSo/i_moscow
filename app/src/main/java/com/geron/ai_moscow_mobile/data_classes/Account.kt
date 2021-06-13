package com.geron.ai_moscow_mobile.data_classes

import android.net.MailTo
import android.util.EventLogTags
import java.net.PasswordAuthentication

data class Account (
    val id: String,
    val name: String,
    val mail: String,
    val password: String,
    val tags: List<String>,
    val photoLink: String,
    val myEvents: List<String>,
    val masterProjectIds: List<String>,
    val slaveProjectIds: List<String>,
    val chatIds: List<String>,
    val metadata: AccountMetadata
)

data class AccountMetadata (
    val description: String
)