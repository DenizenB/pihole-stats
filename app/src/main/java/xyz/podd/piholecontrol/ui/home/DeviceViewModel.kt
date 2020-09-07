package xyz.podd.piholecontrol.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import xyz.podd.piholecontrol.R
import xyz.podd.piholecontrol.model.Device
import xyz.podd.piholecontrol.service.PiHoleControl

class DeviceViewModel(private val device: Device) : ViewModel() {
    private val _summary = MutableLiveData<PiHoleSummary>()
    val summary: LiveData<PiHoleSummary> = _summary

    fun fetchStatus() {
        val service = PiHoleControl().buildService(device.url)

        val exceptionHandler = CoroutineExceptionHandler{ _, throwable ->
            throwable.printStackTrace()
            _summary.value = PiHoleSummary(R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
        }

        viewModelScope.launch(exceptionHandler) {
            val response = service.getSummary(device.authToken)
            val status = response.body()

            if (status != null) {
                val drawable = when (status.enabled) {
                    true -> R.drawable.ic_sentiment_very_satisfied_black_24dp
                    else -> R.drawable.ic_sentiment_very_dissatisfied_black_24dp
                }

                _summary.value = PiHoleSummary(drawable,
                    "${status.queriesToday} queries",
                    "${status.blockedTodayPercentage}% blocked")
            }
        }
    }
}

data class PiHoleSummary(val drawable: Int, val queries: String = "", val blocked: String = "")