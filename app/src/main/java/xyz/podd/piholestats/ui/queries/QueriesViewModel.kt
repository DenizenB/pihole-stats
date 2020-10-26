package xyz.podd.piholestats.ui.queries

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.podd.piholestats.Storage
import xyz.podd.piholestats.model.network.QueryData

class QueriesViewModel(context: Context) : ViewModel() {
    private val storage = Storage(context)

    private val _queries = MutableLiveData<List<QueryData>>()
    val queries: LiveData<List<QueryData>> = _queries

    fun subscribe() {
        viewModelScope.launch {
            val devices = storage.devices

            for (device in devices) {
                viewModelScope.launch(Dispatchers.IO) {
                    while (true) {
                        _queries.postValue(device.service.getQueries(QueryAdapter.MAX_COUNT * 2).data)
                        delay(1_000)
                    }
                }
            }
        }
    }
}