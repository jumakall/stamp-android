package com.stamp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StampsViewModel : ViewModel() {

    private val _newStamp: MutableLiveData<StampViewModel> = MutableLiveData()
    val newStamp: LiveData<StampViewModel>
        get() = _newStamp

    fun newStamp()
    {
        _newStamp.value = StampViewModel()
    }

}