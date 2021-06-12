package com.geron.ai_moscow_mobile

object CallbackHelper {
    lateinit var onEventItemClicked: (position: Int) -> Unit
    //Эту херню можно звать из каждого фрагмента, где надо вернуться в меню
    lateinit var onMenuClicked: ()->Unit
    lateinit var onMenuBackButtonClicked: ()->Unit
    lateinit var onProfileOpen: () -> Unit
    lateinit var onMyEvents:() -> Unit
    lateinit var onMyProjects:() -> Unit
    lateinit var onAllProjects:()->Unit
}