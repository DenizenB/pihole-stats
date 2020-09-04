package xyz.podd.piholecontrol.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.Response
import xyz.podd.piholecontrol.service.PiHoleControl
import xyz.podd.piholecontrol.model.Status
import java.io.InterruptedIOException

class HomeViewModel : ViewModel() {

    private val _status = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val status: LiveData<String> = _status

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    fun fetchStatus() {
        val service = PiHoleControl().buildService("https://192.168.1.251:8080/admin/")

        val exceptionHandler = CoroutineExceptionHandler{ _, throwable ->
            throwable.printStackTrace()

            val message = when (throwable) {
                is InterruptedIOException -> "Timed out"
                else -> "Error: ${throwable.localizedMessage}"
            }
            _toast.postValue(message)
        }

        viewModelScope.launch(exceptionHandler) {
            val status: Response<Status> = service.getStatus()
            if (status.isSuccessful) {
                _status.value = "PiHole status: ${status.body()?.status}"
            }
        }
    }
}