package com.geron.ai_moscow_mobile.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geron.ai_moscow_mobile.CookieRepository
import com.geron.ai_moscow_mobile.R
import com.geron.ai_moscow_mobile.adapters.ChatAdapter
import com.geron.ai_moscow_mobile.data_classes.ChatId
import com.geron.ai_moscow_mobile.data_classes.Message
import com.geron.ai_moscow_mobile.data_classes.MessageMetadata
import com.geron.ai_moscow_mobile.data_classes.SendMessage
import com.geron.ai_moscow_mobile.viewmodels.ChatViewModel
import kotlinx.coroutines.runBlocking

class ChatFragment(val chatId: String) : Fragment() {

    lateinit var chatRecyclerView: RecyclerView
    lateinit var chatAdapter: ChatAdapter
    lateinit var chatEditText: EditText
    lateinit var sendMessageButton: ImageView

    val chatViewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chatEditText = view.findViewById(R.id.et_message)
        chatAdapter = ChatAdapter()
        chatRecyclerView = view.findViewById(R.id.rv_chat)
        sendMessageButton = view.findViewById(R.id.btn_send_message)

        sendMessageButton.setOnClickListener {
            runBlocking {
                chatViewModel.sendMessage(CookieRepository.cookie!!, SendMessage(
                    chatId,
                    "default",
                    MessageMetadata(chatEditText.text.toString())
                ))
            }
        }
        chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).also {
                it.stackFromEnd = true
            }

        }

        runBlocking {
            chatViewModel.getMessages(CookieRepository.cookie!!, ChatId(chatId))
        }
        chatViewModel.getChatMessages().observe(viewLifecycleOwner, { messages ->
            chatAdapter.updateList(messages)
            chatRecyclerView.requestFocus()
            chatRecyclerView.scrollToPosition(messages.size - 1)
            chatEditText.setText("")
            closeKeyboard()
        })
    }

    private fun closeKeyboard() {
        val imm: InputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}