package com.geron.ai_moscow_mobile.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geron.ai_moscow_mobile.ServerHelper
import com.geron.ai_moscow_mobile.data_classes.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel : ViewModel() {

    private val messages: MutableLiveData<MutableList<Message?>> by lazy {
        MutableLiveData<MutableList<Message?>>(mutableListOf())
    }

    fun getChatMessages(): MutableLiveData<MutableList<Message?>> {
        return messages
    }

    suspend fun sendMessage(cookie: Cookie, message: SendMessage) {
        ServerHelper.service = ServerHelper.makeApiService()
        viewModelScope.launch {
            val response = ServerHelper.service?.sendMessage(cookie.cookie, message)
            withContext(Dispatchers.Main) {
                try {
                    response?.let { response ->
                        if (response.isSuccessful) {
                            messages.value = messages.value.also {
                                val updateMessage = response.body()
                                it?.add(updateMessage)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d("HTTP request", "Server didn't send response")
                }
            }

        }
    }

    suspend fun getMessages(cookie: Cookie, chatId: ChatId) {
        ServerHelper.service = ServerHelper.makeApiService()
        viewModelScope.launch {
            val response = ServerHelper.service?.getChatMessages(cookie.cookie, chatId)
            withContext(Dispatchers.Main) {
                try {
                    response?.let { response ->
                        if (response.isSuccessful) {
                            messages.value = response.body()?.toMutableList()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("HTTP request", "Server didn't send response")
                }
            }

        }
    }
}