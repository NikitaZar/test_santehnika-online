package ru.nikitazar.santehnika_online.app

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp
import ru.nikitazar.santehnika_online.R

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val mapkitApiKey = applicationContext.getString(R.string.MAPKIT_API_KEY)
        MapKitFactory.setApiKey(mapkitApiKey)
    }
}