package com.geron.ai_moscow_mobile.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geron.ai_moscow_mobile.CookieRepository
import com.geron.ai_moscow_mobile.ServerHelper
import com.geron.ai_moscow_mobile.data_classes.Cookie
import com.geron.ai_moscow_mobile.data_classes.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MyEventsViewModel : ViewModel() {
    private val eventsList: MutableLiveData<List<Event>> by lazy {
        MutableLiveData<List<Event>>(listOf()).also {
            runBlocking {
                getMyEvents(CookieRepository.cookie)
            }
        }
    }

    fun getEventList(): MutableLiveData<List<Event>> {
        return eventsList
    }

    private suspend fun getMyEvents(cookie: Cookie?) {
        if(cookie == null) return
        ServerHelper.service = ServerHelper.makeApiService()
        viewModelScope.launch {
            val response = ServerHelper.service?.getMyEventsList(cookie.cookie)
            withContext(Dispatchers.Main) {
                try {
                    response?.let { response ->
                        if (response.isSuccessful) {
                            val events = response.body()
                            Log.d("myEvents", response.body().toString())
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