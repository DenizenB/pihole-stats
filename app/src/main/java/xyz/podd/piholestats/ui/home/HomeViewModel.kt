package xyz.podd.piholestats.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.podd.piholestats.Storage
import xyz.podd.piholestats.model.Device
import xyz.podd.piholestats.model.network.TopLists
import xyz.podd.piholestats.service.Coordinator

class HomeViewModel(context: Context) : ViewModel() {
    private val storage = Storage(context)

    private val _devices = MutableLiveData<List<Device>>()
    val devices: LiveData<List<Device>> = _devices

    private val _topLists = MutableLiveData<TopLists>()
    val topLists: LiveData<TopLists> = _topLists

    fun refresh() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val devices = storage.devices
            _devices.postValue(devices)

            val coordinator = Coordinator(devices)
            _topLists.postValue(coordinator.getTopLists())
        }
    }
}