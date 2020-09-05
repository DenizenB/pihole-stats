package xyz.podd.piholecontrol.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.Response
import xyz.podd.piholecontrol.R
import xyz.podd.piholecontrol.service.PiHoleControl
import xyz.podd.piholecontrol.service.Status
import xyz.podd.piholecontrol.service.TopItems
import java.io.InterruptedIOException

class HomeViewModel : ViewModel() {

    private val _status = MutableLiveData<HomeStatus>()
    val status: LiveData<HomeStatus> = _status

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    fun fetchStatus() {
        val service = PiHoleControl().buildService("https://192.168.1.251:8080/admin/")

        val exceptionHandler = CoroutineExceptionHandler{ _, throwable ->
            throwable.printStackTrace()

            val message = when (throwable) {
                is InterruptedIOException -> "Cannot reach Pi-hole"
                else -> "Error: ${throwable.localizedMessage}"
            }
            _status.value = HomeStatus(R.drawable.ic_sentiment_very_dissatisfied_black_24dp, message)
        }

        viewModelScope.launch(exceptionHandler) {
            val response: Response<Status> = service.getStatus()
            if (response.isSuccessful && response.body() != null) {
                val status = response.body()
                if (status != null) {
                    val drawable = when (status.enabled) {
                        true -> R.drawable.ic_sentiment_very_satisfied_black_24dp
                        else -> R.drawable.ic_sentiment_very_dissatisfied_black_24dp
                    }

                    val message = when (status.enabled) {
                        true -> "Your Pi-hole is online"
                        else -> "Your Pi-hole is disabled"
                    }

                    _status.value = HomeStatus(drawable, message)
                }
            }
        }

        viewModelScope.launch(exceptionHandler) {
            val status: Response<Status> = service.getStatus()
            if (status.isSuccessful) {
                _status.value = "PiHole status: ${status.body()?.status}"
            }
        }
    }
}

data class HomeStatus(val drawable: Int, val message: String)