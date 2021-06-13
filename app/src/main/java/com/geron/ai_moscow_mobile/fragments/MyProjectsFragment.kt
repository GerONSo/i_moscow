package com.geron.ai_moscow_mobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geron.ai_moscow_mobile.CallbackHelper
import com.geron.ai_moscow_mobile.EventsAdapter
import com.geron.ai_moscow_mobile.R

class MyProjectsFragment : Fragment() {

    lateinit var btnBackMenu: ImageView
    val eventsAdapter: EventsAdapter by lazy {
        EventsAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val myProjectsRecyclerView = view.findViewById<RecyclerView>(R.id.rv_my_project)
        btnBackMenu = view.findViewById(R.id.btn_menu_open_proj)

        myProjectsRecyclerView.apply {
            adapter = eventsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        btnBackMenu.setOnClickListener {
            CallbackHelper.onMenuBackButtonClicked()

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_projects, container, false)
    }
}