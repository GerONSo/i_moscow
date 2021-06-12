package com.geron.ai_moscow_mobile

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EventViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val photoImageView: ImageView? = view.findViewById(R.id.iv_card_event_photo)
    val titleTextView: TextView? = view.findViewById(R.id.tv_card_event_name)
    val descriptionTextView: TextView? = view.findViewById(R.id.tv_card_event_description)
    val plusFab: FloatingActionButton? = view.findViewById(R.id.fab_card_event_plus)
}