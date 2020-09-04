package xyz.podd.piholecontrol.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import xyz.podd.piholecontrol.service.PiHoleControl
import xyz.podd.piholecontrol.model.Status

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun fetchStatus() {
        val service = PiHoleControl().buildService("https://192.168.1.251:8080/admin/")

        viewModelScope.launch {
            val status: Response<Status> = service.getStatus()
            if (status.isSuccessful) {
                _text.value = "PiHole status: ${status.body()?.status}"
            }
        }
    }
}