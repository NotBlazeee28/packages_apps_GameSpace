/*
 * Copyright (C) 2021 Chaldeaprjkt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.halcyon.gamespace.widget

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.provider.Settings
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.view.doOnLayout
import com.halcyon.gamespace.R
import com.halcyon.gamespace.utils.dp
import java.io.BufferedReader
import java.io.InputStreamReader

class PanelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var defaultY: Float? = null
    var relativeY = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.panel_view, this, true)
        isClickable = true
        isFocusable = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        applyRelativeLocation()
	batteryTemperature()
        brightnessSlider()
    }

    private fun batteryTemperature() {
        val intent: Intent =
            context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))!!
        val temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0).toInt() / 10
        val degree = "\u2103"
        val batteryTemp:TextView = findViewById(R.id.batteryTemp)!!
        batteryTemp.text = "$temp$degree"
    }

    private fun brightnessSlider() {
        var lightBar:SeekBar = findViewById(R.id.seekBar)!!
        var brightness = Settings.System.getInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS, 0
        )
        lightBar.setProgress(brightness)
        val contentResolver: ContentResolver = context.contentResolver
        val setting: Uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS)

        val observer: ContentObserver = object : ContentObserver(Handler()) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                var brightness1 = Settings.System.getInt(
                    context.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS, 0
                )
                lightBar.setProgress(brightness1)
            }

            override fun deliverSelfNotifications(): Boolean {
                return true
            }
        }
        contentResolver.registerContentObserver(setting, false, observer)

        lightBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Settings.System.putInt(
                    context.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS, progress
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun applyRelativeLocation() {
        doOnLayout {
            if (defaultY == null)
                defaultY = y

	    val safeArea = rootWindowInsets.getInsets(WindowInsets.Type.systemBars())
            val minY = safeArea.top + 16.dp
            val maxY = safeArea.top + (parent as View).height - safeArea.bottom - height - 16.dp
	    y = relativeY.coerceIn(minY, maxY).toFloat()
        }
    }

}
