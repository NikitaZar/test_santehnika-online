package ru.nikitazar.santehnika_online.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import ru.nikitazar.santehnika_online.R

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {
    private val KEY_IS_CHEKED = "KEY_IS_CHEKED"
    private var isChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        savedInstanceState?.let {
            isChecked = it.getBoolean(KEY_IS_CHEKED)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bt_day_night_menu, menu)

        menu?.let {
            val item = it.findItem(R.id.bt_day_night).setActionView(R.layout.switch_day_night)
            val sw = item.actionView.findViewById<SwitchMaterial>(R.id.switch_day_night)
            sw.isChecked = isChecked
            sw.setOnCheckedChangeListener { _, checked ->
                isChecked = checked
                when (checked) {
                    false -> delegate.localNightMode = MODE_NIGHT_NO
                    true -> delegate.localNightMode = MODE_NIGHT_YES
                }
            }
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_CHEKED, isChecked)
    }


}