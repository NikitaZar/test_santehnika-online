package ru.nikitazar.santehnika_online.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.nikitazar.santehnika_online.repository.PointRepository
import javax.inject.Inject

@HiltViewModel
class PointViewModel @Inject constructor(
    private val repository: PointRepository
) : ViewModel() {

    val data = repository.data.asLiveData()

    private val _edited = MutableStateFlow(Point())
    val edited = _edited.asStateFlow()

    private val mutex = Mutex()

    fun save() = viewModelScope.launch(Dispatchers.IO) {
        mutex.withLock {
            repository.savePoint(_edited.value)
        }
    }

    fun remove() = viewModelScope.launch(Dispatchers.IO) {
        mutex.withLock {
            repository.removePoint()
        }
    }

    fun edit(point: Point) {
        _edited.value = point
    }
}