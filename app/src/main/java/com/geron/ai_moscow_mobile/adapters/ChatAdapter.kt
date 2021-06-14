package com.geron.ai_moscow_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.geron.ai_moscow_mobile.DiffUtilCallback
import com.geron.ai_moscow_mobile.DiffUtilCallbackMutable
import com.geron.ai_moscow_mobile.EventViewHolder
import com.geron.ai_moscow_mobile.R
import com.geron.ai_moscow_mobile.data_classes.Event
import com.geron.ai_moscow_mobile.data_classes.Message

class ChatAdapter() : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    var messages: MutableList<Message?> = mutableListOf()

    fun updateList(messages_: MutableList<Message?>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(DiffUtilCallbackMutable(messages, messages_))
        this.messages = messages_
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class ChatViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.txt_message)
        val pictureImageView: ImageView = view.findViewById(R.id.message_cr_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_chat_message, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.messageTextView.text = messages[position]?.messageMetadata?.message
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}