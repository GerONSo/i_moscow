package com.geron.ai_moscow_mobile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.res.Resources
import androidx.recyclerview.widget.DiffUtil
import com.geron.ai_moscow_mobile.data_classes.Event
import com.squareup.picasso.Picasso

class EventsAdapter(
    val isMyEvents: Boolean = false,
    val isProjects: Boolean = false
) : RecyclerView.Adapter<EventViewHolder>() {

    var events: List<Event> = listOf()
    var myEvents: List<Event> = listOf()
    var allProjects: List<Project> = listOf()
    lateinit var resources: Resources

    fun updateAllProjectsList(projects_: List<Project>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(DiffUtilCallback(allProjects, projects_))
        this.allProjects = projects_
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateMyEventsList(events_: List<Event>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(DiffUtilCallback(myEvents, events_))
        this.myEvents = events_
        diffResult.dispatchUpdatesTo(this)
    }

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
        if(isProjects) {
            val currentProject = if(isProjects) {
                allProjects[position]
            }
            else {
                allProjects[position]
            }
            holder.titleTextView?.text = currentProject.name
//            holder.descriptionTextView?.text = currentProject.metaData.description
            Picasso.get().load(ServerHelper.BASE_URL + currentProject.photoLink)
                .into(holder.photoImageView)
            holder.view.setOnClickListener {
//                CallbackHelper.onEventItemClicked(position)
            }
            return
        }

        val currentEvent = if (!isMyEvents) {
            events[position]
        } else {
            myEvents[position]
        }
        if (isMyEvents) {
            holder.plusFab?.setImageDrawable(resources.getDrawable(R.drawable.ic_done))
        }
        holder.titleTextView?.text = currentEvent.name
        holder.descriptionTextView?.text = currentEvent.shortDescription
        Picasso.get().load(ServerHelper.BASE_URL + currentEvent.photoLink)
            .into(holder.photoImageView)
        holder.view.setOnClickListener {
            CallbackHelper.onEventItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return if(isProjects) {
            allProjects.size
        }
        else if (!isMyEvents) {
            events.size
        } else {
            myEvents.size
        }
    }

}