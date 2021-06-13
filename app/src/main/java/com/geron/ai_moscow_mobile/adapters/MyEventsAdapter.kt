package com.geron.ai_moscow_mobile.adapters

import androidx.recyclerview.widget.DiffUtil
import com.geron.ai_moscow_mobile.*
import com.geron.ai_moscow_mobile.data_classes.Event
import com.squareup.picasso.Picasso

class MyEventsAdapter : EventsAdapter() {
    var myEvents: List<Event> = listOf()

    fun updateMyEventsList(events_: List<Event>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(DiffUtilCallback(myEvents, events_))
        this.myEvents = events_
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = myEvents[position]
        holder.plusFab?.setImageDrawable(resources.getDrawable(R.drawable.ic_done))
        holder.titleTextView?.text = currentEvent.name
        holder.descriptionTextView?.text = currentEvent.shortDescription
        Picasso.get().load(ServerHelper.BASE_URL + currentEvent.photoLink)
            .into(holder.photoImageView)
        holder.view.setOnClickListener {
            CallbackHelper.onEventItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return myEvents.size
    }
}