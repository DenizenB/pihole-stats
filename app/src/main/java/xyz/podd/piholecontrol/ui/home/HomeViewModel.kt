package xyz.podd.piholecontrol.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.podd.piholecontrol.Storage
import xyz.podd.piholecontrol.model.Client
import xyz.podd.piholecontrol.model.ClientStats
import xyz.podd.piholecontrol.model.Device
import xyz.podd.piholecontrol.service.Coordinator

class HomeViewModel(private val context: Context) : ViewModel() {
    private val storage = Storage(context)

    private val _devices = MutableLiveData<List<Device>>()
    val devices: LiveData<List<Device>> = _devices

    private val _topClients = MutableLiveData<Map<Client, ClientStats>>()
    val topClients: LiveData<Map<Client, ClientStats>> = _topClients

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            val devices = storage.devices
            _devices.postValue(devices)

            val coordinator = Coordinator(devices)
            _topClients.postValue(coordinator.getTopClients())
        }
    }
}