package com.geron.ai_moscow_mobile.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geron.ai_moscow_mobile.ServerHelper
import com.geron.ai_moscow_mobile.data_classes.Cookie
import com.geron.ai_moscow_mobile.data_classes.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel : ViewModel() {
    private val isRegistered: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getRegistered(): MutableLiveData<Boolean> {
        return isRegistered
    }

    suspend fun register(user: User) {
        ServerHelper.service = ServerHelper.makeApiService()
        viewModelScope.launch {
            val response = ServerHelper.service?.register(user)
            withContext(Dispatchers.Main) {
                try {
                    response?.let { response ->
                        isRegistered.value = response.isSuccessful
                    }
                } catch (e: Exception) {
                    Log.d("HTTP request", "Server didn't send response")
                }
            }

        }
    }
}