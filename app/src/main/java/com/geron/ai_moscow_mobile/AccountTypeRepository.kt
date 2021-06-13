package com.geron.ai_moscow_mobile

object AccountTypeRepository {
    var type = AccountType.SLAVE

    enum class AccountType {
        SLAVE, MASTER
    }
}