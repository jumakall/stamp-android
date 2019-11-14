package com.stamp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stamp.model.Pass
import com.stamp.network.StampApi
import kotlinx.coroutines.*

enum class ViewModelStatus { LOADING, ERROR, DONE }
enum class PassListViewMode { CUSTOMER, VENDOR }

class PassesViewModel : ViewModel() {

    // Variable for representing the view model status
    private val _status = MutableLiveData<ViewModelStatus>()
    val status: LiveData<ViewModelStatus>
        get() = _status

    // List of passes
    private val _passes = MutableLiveData<List<Pass>>()
    val passes: LiveData<List<Pass>>
        get() = _passes

    // Navigate to selected pass
    private val _eventPassSelected = MutableLiveData<Pass>()
    val eventPassSelected: LiveData<Pass>
        get() = _eventPassSelected

    // Setup coroutine
    private val passesJob = Job()
    private val coroutineScope = CoroutineScope(passesJob + Dispatchers.Main)

    private var listMode = PassListViewMode.CUSTOMER

    fun setMode(mode: PassListViewMode) {
        listMode = mode

        if (status.value == null)
            getPasses()
    }

    fun getPasses() {
        coroutineScope.launch {
            _status.value = ViewModelStatus.LOADING
            val deferred = when(listMode) {
                PassListViewMode.CUSTOMER -> StampApi.retrofitService.getPassesAsync()
                PassListViewMode.VENDOR   -> StampApi.retrofitService.getVendorPassesAsync()
            }

            try {
                _passes.value = deferred.await()
                _status.value = ViewModelStatus.DONE
            } catch (e: Exception) {
                _status.value = ViewModelStatus.ERROR
                _passes.value = ArrayList()
            }
        }
    }

    fun selectPass(pass: Pass) {
        _eventPassSelected.value = pass
    }

    fun eventPassSelectedComplete() {
        _eventPassSelected.value = null
    }

    override fun onCleared() {
        super.onCleared()
        passesJob.cancel()
    }
}