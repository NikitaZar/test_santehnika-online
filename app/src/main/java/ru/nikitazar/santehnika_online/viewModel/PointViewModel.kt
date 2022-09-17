package ru.nikitazar.santehnika_online.viewModel

import android.hardware.camera2.MultiResolutionImageReader
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.nikitazar.santehnika_online.repository.PointRepository
import javax.inject.Inject

@HiltViewModel
class PointViewModel @Inject constructor(
    private val repository: PointRepository
) : ViewModel() {

    private val edited = MutableStateFlow(Point())
    private val mutex = Mutex()

    fun save() = viewModelScope.launch(Dispatchers.IO) {
        mutex.withLock {
            repository.savePoint(edited.value)
            edited.value = Point()
        }
    }

    fun remove() = viewModelScope.launch(Dispatchers.IO) {
        mutex.withLock {
            repository.removePoint()
        }
    }

    fun edit(point: Point) {
        edited.value = point
    }
}