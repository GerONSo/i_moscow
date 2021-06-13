package com.geron.ai_moscow_mobile.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geron.ai_moscow_mobile.Project
import com.geron.ai_moscow_mobile.ServerHelper
import com.geron.ai_moscow_mobile.data_classes.Account
import com.geron.ai_moscow_mobile.data_classes.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AllProjectsViewModel : ViewModel() {
    private val projectsList: MutableLiveData<List<Project>> by lazy {
        MutableLiveData<List<Project>>(listOf()).also {
            runBlocking {
                getProjects()
            }
        }
    }

    private val slavesList: MutableLiveData<List<Account>> by lazy {
        MutableLiveData<List<Account>>(listOf()).also {
            runBlocking {
                getSlaves()
            }
        }
    }

    fun getProjectList(): MutableLiveData<List<Project>> {
        return projectsList
    }

    fun getSlaveList(): MutableLiveData<List<Account>> {
        return slavesList
    }

    private suspend fun getSlaves() {
        ServerHelper.service = ServerHelper.makeApiService()
        viewModelScope.launch {
            val response = ServerHelper.service?.getAccountsList()
            withContext(Dispatchers.Main) {
                try {
                    response?.let { response ->
                        if (response.isSuccessful) {
                            val projects = response.body()
                            Log.d("allProjects", "success")
                            slavesList.value = projects
                        }
                    }
                } catch (e: Exception) {
                    Log.d("HTTP request", "Server didn't send response")
                }
            }
        }
    }

    private suspend fun getProjects() {
        ServerHelper.service = ServerHelper.makeApiService()
        viewModelScope.launch {
            val response = ServerHelper.service?.getProjectsList()
            withContext(Dispatchers.Main) {
                try {
                    response?.let { response ->
                        if (response.isSuccessful) {
                            val projects = response.body()
                            Log.d("allProjects", "success")
                            projectsList.value = projects
                        }
                    }
                } catch (e: Exception) {
                    Log.d("HTTP request", "Server didn't send response")
                }
            }
        }
    }
}