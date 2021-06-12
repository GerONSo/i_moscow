package com.geron.ai_moscow_mobile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.geron.ai_moscow_mobile.data_classes.Event
import com.squareup.picasso.Picasso

class EventsAdapter() : RecyclerView.Adapter<EventViewHolder>() {

    var events: List<Event> = listOf()

    fun updateList(events: List<Event>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = this@EventsAdapter.events.size

            override fun getNewListSize(): Int = events.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                this@EventsAdapter.events[oldItemPosition].id == events[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                this@EventsAdapter.events[oldItemPosition] == events[newItemPosition]
        })
        this.events = events
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder =
        EventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_event, parent, false))

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = events[position]
        holder.titleTextView?.text = currentEvent.name
        holder.descriptionTextView?.text = currentEvent.shortDescription
        Picasso.get().load(ServerHelper.BASE_URL + currentEvent.photoLink).into(holder.photoImageView)
        holder.view.setOnClickListener {
            CallbackHelper.onEventItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }


}