package com.stamp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stamp.AdvancedViewModelStatus
import com.stamp.model.Pass
import com.stamp.network.StampApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StampViewModel : ViewModel() {

    private val _status = MutableLiveData<AdvancedViewModelStatus>()
    val status: LiveData<AdvancedViewModelStatus>
        get() = _status

    private val _pass = MutableLiveData<Pass>()
    val pass: LiveData<Pass>
        get() = _pass

    private val _amount = MutableLiveData<Int>()
    val amount: LiveData<Int>
        get() = _amount

    private val _code = MutableLiveData<String>()
    val code: LiveData<String>
        get() = _code

    private val _eventPassSelected = MutableLiveData<Pass>()
    val eventPassSelected: LiveData<Pass>
        get() = _eventPassSelected

    // todo: should here be an event for the selected amount? (needs to be different than other events)

    private val _eventCodeSelected = MutableLiveData<String>()
    val eventCodeSelected: LiveData<String>
        get() = _eventCodeSelected

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    init {
        _status.value = AdvancedViewModelStatus.IDLE
        _amount.value = 1
    }

    fun setPass(pass: Pass) {
        _pass.value = pass
        _eventPassSelected.value = pass
    }

    fun setAmount(amount: Int) {
        _amount.value = amount
    }

    fun postCode(code: String) {
        _code.postValue(code)
        _eventCodeSelected.postValue(code)
    }

    fun eventPassSelectedComplete()
    {
        _eventPassSelected.value = null
    }

    fun eventCodeSelectedComplete()
    {
        _eventCodeSelected.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun commitStamp() {
        // do not allow re-sending already sent stamp
        if (_status.value == AdvancedViewModelStatus.SUCCESS)
            return

        _status.value = AdvancedViewModelStatus.BUSY
        coroutineScope.launch {
            val id = _pass.value?.id
            val amount = _amount.value
            val code = _code.value

            if (id != null && amount != null && code != null) {
                val info = StampInformation(id, amount, code)
                val deferred = StampApi.retrofitService.postStampAsync(info)

                try {
                    deferred.await()
                    _status.value = AdvancedViewModelStatus.SUCCESS
                } catch (e: Exception) {
                    _status.value = AdvancedViewModelStatus.ERROR
                }

            } else {
                _status.value = AdvancedViewModelStatus.ERROR
            }
        }
    }

    data class StampInformation(
        val pass_id: Int,
        val amount: Int,
        val code: String
    ) {
        override fun toString(): String {
            return super.toString()
        }
    }

}