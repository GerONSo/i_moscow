package com.geron.ai_moscow_mobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geron.ai_moscow_mobile.EventsAdapter
import com.geron.ai_moscow_mobile.R
import com.geron.ai_moscow_mobile.viewmodels.AllProjectsViewModel

class AllProjectsFragment : Fragment() {

    val projectsRecyclerView: RecyclerView by lazy {
        requireView().findViewById(R.id.rv_projects)
    }

    val projectsAdapter: EventsAdapter by lazy {
        EventsAdapter(isProjects = true)
    }

    val allProjectsViewModel: AllProjectsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_projects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        allProjectsViewModel.getEventList().observe(viewLifecycleOwner, { allProjects ->
            projectsAdapter.updateAllProjectsList(allProjects)
        })
        projectsRecyclerView.apply {
            adapter = projectsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }


}