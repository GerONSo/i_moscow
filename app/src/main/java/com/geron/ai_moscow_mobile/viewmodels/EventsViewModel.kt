package com.geron.ai_moscow_mobile.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geron.ai_moscow_mobile.ServerHelper
import com.geron.ai_moscow_mobile.data_classes.Event
import kotlinx.coroutines.*

class EventsViewModel : ViewModel() {
    private val eventsList: MutableLiveData<List<Event>> by lazy {
        MutableLiveData<List<Event>>(listOf()).also {
            runBlocking {
                getEvents()
            }
        }
    }

    fun getEventList(): MutableLiveData<List<Event>> {
        return eventsList
    }

    private suspend fun getEvents() {
        ServerHelper.service = ServerHelper.makeApiService()
        viewModelScope.launch {
            val response = ServerHelper.service?.getEventsList()
            withContext(Dispatchers.Main) {
                try {
                    response?.let { response ->
                        if (response.isSuccessful) {
                            val events = response.body()
                            Log.d("events", "success")
                            eventsList.value = events
                        }
                    }
                } catch (e: Exception) {
                    Log.d("HTTP request", "Server didn't send response")
                }
            }

        }
    }
}