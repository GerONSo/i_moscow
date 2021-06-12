package com.geron.ai_moscow_mobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.geron.ai_moscow_mobile.viewmodels.EventsViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentEvent = model.getEventList().value?.get(position)
        Picasso.get()
            .load(ServerHelper.BASE_URL + currentEvent?.photoLink)
            .into(logoImageView)
        departmentTextView.text = currentEvent?.department
        creatorTextView.text = currentEvent?.creator
        timeTextView.text = currentEvent?.time
        placeTextView.text = currentEvent?.address
        descriptionTextView.text = currentEvent?.fullDescription
    }
}