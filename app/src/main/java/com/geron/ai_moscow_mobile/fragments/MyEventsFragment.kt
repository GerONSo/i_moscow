package com.geron.ai_moscow_mobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geron.ai_moscow_mobile.adapters.MyEventsAdapter
import com.geron.ai_moscow_mobile.R
import com.geron.ai_moscow_mobile.viewmodels.MyEventsViewModel

class MyEventsFragment : Fragment() {

    val myEventsRecyclerView: RecyclerView by lazy { requireView().findViewById(R.id.rv_my_events) }
    val myEventsAdapter: MyEventsAdapter by lazy { MyEventsAdapter() }
    val myEventsViewModel: MyEventsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        myEventsViewModel.getEventList().observe(viewLifecycleOwner, { myEvents ->
            myEventsAdapter.updateMyEventsList(myEvents)
        })
        myEventsRecyclerView.apply {
            adapter = myEventsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}