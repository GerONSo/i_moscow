package com.geron.ai_moscow_mobile

object CallbackHelper {

    lateinit var onProjectClicked: (position: Int, isMy: Boolean) -> Unit
    lateinit var onAccountClicked: (position: Int) -> Unit
    lateinit var onEventItemClicked: (position: Int) -> Unit

    //Эту херню можно звать из каждого фрагмента, где надо вернуться в меню
    lateinit var onMenuClicked: () -> Unit
    lateinit var onMenuBackButtonClicked: () -> Unit
    lateinit var onProfileOpen: () -> Unit
    lateinit var onMyEvents: () -> Unit
    lateinit var onMyProjects: () -> Unit
    lateinit var onAllProjects: () -> Unit
    lateinit var onOpenChat: (chatId: String) -> Unit

    lateinit var onLogin: () -> Unit
    lateinit var onBackPressedStartFragment: () -> Unit
}