package com.geron.ai_moscow_mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.geron.ai_moscow_mobile.R
import com.geron.ai_moscow_mobile.ServerHelper
import com.geron.ai_moscow_mobile.viewmodels.EventsViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.runBlocking

class EventFragment(val position: Int) : Fragment() {

    val model: EventsViewModel by activityViewModels()

    val logoImageView: CircleImageView by lazy {
        requireView().findViewById(R.id.iv_event_logo)
    }
    val departmentTextView: TextView by lazy {
        requireView().findViewById(R.id.tv_department_value)
    }
    val creatorTextView: TextView by lazy {
        requireView().findViewById(R.id.tv_creator_value)
    }
    val timeTextView: TextView by lazy {
        requireView().findViewById(R.id.tv_time_value)
    }
    val placeTextView: TextView by lazy {
        requireView().findViewById(R.id.tv_place_value)
    }
    val descriptionTextView: TextView by lazy {
        requireView().findViewById(R.id.tv_full_description)
    }

    val joinEventButton: Button by lazy {
        requireView().findViewById(R.id.btn_join_event)
    }

    val eventLayout: CoordinatorLayout by lazy {
        requireView().findViewById(R.id.event_layout)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentEvent = model.getEventList().value?.get(position)

        joinEventButton.setOnClickListener {
            runBlocking { model.joinEvent(currentEvent, position) }
        }
        model.getJoined().observe(viewLifecycleOwner, { isJoined ->
            if(isJoined[position] == "true") {
                Snackbar.make(
                    getParentLayoutRootView()!!,
                    "Теперь вы учавстуете в этом мероприятии",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            else if(isJoined[position] == "false") {
                Snackbar.make(
                    getParentLayoutRootView()!!,
                    "Вы уже подписаны на это мероприятие",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        Picasso.get()
            .load(ServerHelper.BASE_URL + currentEvent?.photoLink)
            .into(logoImageView)
        departmentTextView.text = currentEvent?.department
        creatorTextView.text = currentEvent?.creator
        timeTextView.text = currentEvent?.time
        placeTextView.text = currentEvent?.address
        descriptionTextView.text = currentEvent?.fullDescription
    }

    private fun getParentLayoutRootView(): View? {
        return if (parentFragment != null) { //if it has a parent fragment
            requireParentFragment().view
        } else {
            requireActivity().window.decorView.rootView
        }
    }
}