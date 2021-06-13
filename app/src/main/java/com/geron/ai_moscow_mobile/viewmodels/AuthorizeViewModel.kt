package com.geron.ai_moscow_mobile.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geron.ai_moscow_mobile.CookieRepository
import com.geron.ai_moscow_mobile.ServerHelper
import com.geron.ai_moscow_mobile.data_classes.Cookie
import com.geron.ai_moscow_mobile.data_classes.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AuthorizeViewModel : ViewModel() {
    private val cookie: MutableLiveData<Cookie> by lazy {
        MutableLiveData<Cookie>()
    }

    private val isAuthorized: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getAccountCookie(): MutableLiveData<Cookie> {
        return cookie
    }

    fun getAuthorized(): MutableLiveData<Boolean> {
        return isAuthorized
    }

    suspend fun login(user: User) {
        ServerHelper.service = ServerHelper.makeApiService()
        viewModelScope.launch {
            val response = ServerHelper.service?.authorize(user)
            withContext(Dispatchers.Main) {
                try {
                    response?.let { response ->
                        isAuthorized.value = response.isSuccessful
                        if (response.isSuccessful) {
                            cookie.value = response.body()
                            CookieRepository.cookie = cookie.value
                        }
                    }
                } catch (e: Exception) {
                    Log.d("HTTP request", "Server didn't send response")
                }
            }

        }
    }
}