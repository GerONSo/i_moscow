package com.geron.ai_moscow_mobile.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geron.ai_moscow_mobile.CookieRepository
import com.geron.ai_moscow_mobile.ServerHelper
import com.geron.ai_moscow_mobile.data_classes.Account
import com.geron.ai_moscow_mobile.data_classes.Cookie
import com.geron.ai_moscow_mobile.data_classes.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel : ViewModel() {
    private val myAccount: MutableLiveData<Account> by lazy {
        MutableLiveData<Account>()
    }

    fun getMyAccountProfile(): MutableLiveData<Account> {
        return myAccount
    }

    suspend fun getMyAccount(cookie: Cookie) {
        ServerHelper.service = ServerHelper.makeApiService()
        viewModelScope.launch {
            val response = ServerHelper.service?.getMyAccount(cookie.cookie)
            withContext(Dispatchers.Main) {
                try {
                    response?.let { response ->
                        if (response.isSuccessful) {
                            myAccount.value = response.body()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("HTTP request", "Server didn't send response")
                }
            }

        }
    }

    suspend fun updateAccount(cookie: Cookie, account: Account) {
        ServerHelper.service = ServerHelper.makeApiService()
        viewModelScope.launch {
            val response = ServerHelper.service?.updateAccount(cookie.cookie, account)
            withContext(Dispatchers.Main) {
                try {
                    response?.let { response ->
                        if (response.isSuccessful) {
                            myAccount.value = response.body()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("HTTP request", "Server didn't send response")
                }
            }

        }
    }
}