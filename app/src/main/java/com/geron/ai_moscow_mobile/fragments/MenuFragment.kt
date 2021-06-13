package com.geron.ai_moscow_mobile.fragments

import android.os.Bundle
import android.telecom.Call
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.geron.ai_moscow_mobile.AccountTypeRepository
import com.geron.ai_moscow_mobile.CallbackHelper
import com.geron.ai_moscow_mobile.R

class MenuFragment : Fragment() {

    lateinit var buttonMenuClose: ImageView
    lateinit var buttonProfileOpen: Button
    lateinit var buttonMyEvents: Button
    lateinit var buttonMyProjects: Button
    lateinit var buttonAllProjects: Button
    lateinit var buttonAllEvents: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        buttonMenuClose = view.findViewById(R.id.btn_menu_close)
        buttonProfileOpen = view.findViewById(R.id.button_profile)
        buttonAllEvents = view.findViewById(R.id.button_all_events)
        buttonAllProjects = view.findViewById(R.id.button_all_projects)
        buttonMyEvents = view.findViewById(R.id.button2)
        buttonMyProjects = view.findViewById(R.id.button_events_my)

        buttonAllEvents.setOnClickListener {
            CallbackHelper.onMenuBackButtonClicked()
        }

        buttonAllProjects.apply {
            setOnClickListener {
                CallbackHelper.onAllProjects()
            }
        }

        buttonMyEvents.setOnClickListener {
            CallbackHelper.onMyEvents()
        }

        buttonMyProjects.setOnClickListener {
            CallbackHelper.onMyProjects()
        }

        buttonProfileOpen.setOnClickListener {
            CallbackHelper.onProfileOpen()
        }

        buttonMenuClose.setOnClickListener {
            CallbackHelper.onMenuBackButtonClicked()
        }
    }

    override fun onResume() {
        super.onResume()
        buttonAllProjects.text = if(AccountTypeRepository.type == AccountTypeRepository.AccountType.MASTER) {
            "Рекомендуемые участники"
        }
        else {
            "Все проекты"
        }
    }
}