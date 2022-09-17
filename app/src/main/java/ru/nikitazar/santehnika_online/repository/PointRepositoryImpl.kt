package ru.nikitazar.santehnika_online.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointRepositoryImpl @Inject constructor(@ApplicationContext context: Context) : PointRepository {

    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val _data = MutableStateFlow<Point?>(null)
    val data: StateFlow<Point?>
        get() = _data

    companion object {
        private val TYPE = TypeToken.getParameterized(Point::class.java).type
        private val KEY = "point"
    }

    init {
        prefs.getString(KEY, null)?.let {
            _data.value = gson.fromJson(it, TYPE)
        }
    }

    override suspend fun savePoint(point: Point): Unit = with(prefs.edit()) {
        putString(KEY, gson.toJson(point))
        apply()
        _data.emit(point)
    }

    override suspend fun removePoint(): Unit = with(prefs.edit()) {
        putString(KEY, gson.toJson(null))
        apply()
        _data.emit(null)
    }

}