package ru.nikitazar.santehnika_online.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


fun MapView.attachToLifecycle(lifecycleOwner: LifecycleOwner) {
    lifecycleOwner.lifecycle.addObserver(MapViewLifecycleObserver(this))
}

private class MapViewLifecycleObserver(
    private val mapView: MapView
) : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                MapKitFactory.getInstance().onStart()
                mapView.onStart()
            }
            Lifecycle.Event.ON_STOP -> {
                mapView.onStop()
                MapKitFactory.getInstance().onStop()
            }
            Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)
            else -> Unit
        }
    }
}

fun moveToLocation(mapView: MapView, targetLocation: Point) {
    mapView.map.move(
        CameraPosition(targetLocation, 14.0f, 0.0f, 0.0f),
        Animation(Animation.Type.SMOOTH, 5F),
        null
    )
}

fun drawPlacemark(point: Point, mapObjects: MapObjectCollection, color: Int): PlacemarkMapObject {
    val imageProvider = ImageProvider.fromBitmap(drawSimpleBitmap(color))
    return mapObjects.addPlacemark(point, imageProvider)
}

fun drawSimpleBitmap(color: Int): Bitmap {
    val picSize = 50
    val bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888);
    val canvas = Canvas(bitmap)
    val paint = Paint()
    paint.color = color
    paint.style = Paint.Style.FILL
    canvas.drawCircle(
        picSize / 2F,
        picSize / 2F,
        picSize / 2F,
        paint
    )
    return bitmap
}