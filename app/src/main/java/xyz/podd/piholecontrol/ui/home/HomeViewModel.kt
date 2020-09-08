package xyz.podd.piholecontrol.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.podd.piholecontrol.Storage
import xyz.podd.piholecontrol.model.Device

class HomeViewModel(private val context: Context) : ViewModel() {
    private val storage = Storage(context)

    private val _devices = MutableLiveData<List<Device>>()
    val devices: LiveData<List<Device>> = _devices

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {  _devices.postValue(storage.devices) }
    }
}