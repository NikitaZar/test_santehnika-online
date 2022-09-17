package ru.nikitazar.santehnika_online.repository

import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.flow.StateFlow

interface PointRepository {
    val data: StateFlow<Point?>
    suspend fun savePoint(point: Point)
    suspend fun removePoint()
}