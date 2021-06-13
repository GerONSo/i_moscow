package com.geron.ai_moscow_mobile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.res.Resources
import androidx.recyclerview.widget.DiffUtil
import com.geron.ai_moscow_mobile.*
import com.geron.ai_moscow_mobile.data_classes.Event
import com.squareup.picasso.Picasso

open class EventsAdapter : RecyclerView.Adapter<EventViewHolder>() {

    var events: List<Event> = listOf()

    lateinit var resources: Resources

    fun updateList(events_: List<Event>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(DiffUtilCallback(events, events_))
        this.events = events_
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        resources = parent.resources
        return EventViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_event, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = events[position]
        holder.titleTextView?.text = currentEvent.name
        holder.descriptionTextView?.text = currentEvent.shortDescription
        Picasso.get().load(ServerHelper.BASE_URL + currentEvent.photoLink)
            .into(holder.photoImageView)
        holder.view.setOnClickListener {
            CallbackHelper.onEventItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

}