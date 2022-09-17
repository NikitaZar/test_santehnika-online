package ru.nikitazar.santehnika_online.repository

import com.yandex.mapkit.geometry.Point

interface PointRepository {
    suspend fun savePoint(point: Point)
    suspend fun removePoint()
}