package com.geron.ai_moscow_mobile.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geron.ai_moscow_mobile.AccountTypeRepository
import com.geron.ai_moscow_mobile.CookieRepository
import com.geron.ai_moscow_mobile.Project
import com.geron.ai_moscow_mobile.ServerHelper
import com.geron.ai_moscow_mobile.data_classes.Cookie
import com.geron.ai_moscow_mobile.data_classes.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MyProjectsViewModel : ViewModel() {
    private val myMasterProjects: MutableLiveData<List<Project>> by lazy {
        MutableLiveData<List<Project>>(listOf(Project(name = "Пока здесь ничего нет")))
    }

    private val mySlaveProjects: MutableLiveData<List<Project>> by lazy {
        MutableLiveData<List<Project>>(listOf(Project(name = "Пока здесь ничего нет")))
    }

    fun getMyMasterProjectList(): MutableLiveData<List<Project>> {
        return myMasterProjects
    }

    fun getMySlaveProjectList(): MutableLiveData<List<Project>> {
        return mySlaveProjects
    }

    fun getMyProjects(cookie: Cookie?) {
        if(AccountTypeRepository.type == AccountTypeRepository.AccountType.MASTER) {
            runBlocking {
                getMyMasterProjects(cookie)
            }
        }
        else {
            runBlocking {
                getMySlaveProjects(cookie)
            }
        }
    }

    private suspend fun getMySlaveProjects(cookie: Cookie?) {
        if(cookie == null) return
        ServerHelper.service = ServerHelper.makeApiService()
        viewModelScope.launch {
            val response = ServerHelper.service?.getMySlaveProjects(cookie.cookie)
            withContext(Dispatchers.Main) {
                try {
                    response?.let { response ->
                        if (response.isSuccessful) {
                            val projects = response.body()
                            mySlaveProjects.value = projects
                        }
                    }
                } catch (e: Exception) {
                    Log.d("HTTP request", "Server didn't send response")
                }
            }

        }
    }

    private suspend fun getMyMasterProjects(cookie: Cookie?) {
        if(cookie == null) return
        ServerHelper.service = ServerHelper.makeApiService()
        viewModelScope.launch {
            val response = ServerHelper.service?.getMyMasterProjects(cookie.cookie)
            withContext(Dispatchers.Main) {
                try {
                    response?.let { response ->
                        if (response.isSuccessful) {
                            val projects = response.body()
                            myMasterProjects.value = projects
                        }
                    }
                } catch (e: Exception) {
                    Log.d("HTTP request", "Server didn't send response")
                }
            }

        }
    }
}