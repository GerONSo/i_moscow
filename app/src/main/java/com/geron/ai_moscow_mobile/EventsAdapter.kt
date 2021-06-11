package com.geron.ai_moscow_mobile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class EventsAdapter : RecyclerView.Adapter<EventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder =
        EventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_event, parent, false))

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        // TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return 6
    }


}