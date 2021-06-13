package com.geron.ai_moscow_mobile.adapters

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.geron.ai_moscow_mobile.*
import com.squareup.picasso.Picasso

class MyProjectsAdapter : EventsAdapter() {

    var myProjects: List<Project> = listOf(Project(name = "Пока здесь ничего нет"))

    fun updateMyProjects(projects_: List<Project>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(DiffUtilCallback(myProjects, projects_))
        this.myProjects = projects_
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentProject = myProjects[position]
        holder.titleTextView?.text = currentProject.name
//            holder.descriptionTextView?.text = currentProject.metaData.description
        Picasso.get().load(ServerHelper.BASE_URL + currentProject.photoLink)
            .into(holder.photoImageView)
        holder.plusFab?.visibility = View.GONE
        holder.view.setOnClickListener {
            CallbackHelper.onProjectClicked(position, true)
        }
        return
    }

    override fun getItemCount(): Int {
        return myProjects.size
    }
}