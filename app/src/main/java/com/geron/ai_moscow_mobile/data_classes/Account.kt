package com.geron.ai_moscow_mobile.data_classes

import android.net.MailTo
import android.util.EventLogTags
import java.net.PasswordAuthentication

data class Account (
    var id: String,
    var name: String,
    var mail: String,
    var password: String,
    var tags: List<String>,
    var photoLink: String,
    var myEvents: List<String>,
    var masterProjectIds: List<String>,
    var slaveProjectIds: List<String>,
    var chatIds: List<String>,
    var metadata: AccountMetadata
)

data class AccountMetadata (
    var description: String
)