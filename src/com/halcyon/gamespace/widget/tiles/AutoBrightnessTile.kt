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
package com.halcyon.gamespace.widget.tiles

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.halcyon.gamespace.R

class AutoBrightnessTile @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : BaseTile(context, attrs) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        autoBrightnessEnabled = systemSettings.autoBrightness
        title?.text = context.getString(R.string.auto_brightness_title)
        title?.isSelected = true
        icon?.setImageResource(R.drawable.ic_action_auto_brightness)
    }

    private var autoBrightnessEnabled = false
        set(value) {
            field = value
            if (value) {
                summary?.text = context.getString(R.string.state_enabled)
            } else {
                summary?.text = context.getString(R.string.state_disabled)
            }
            systemSettings.autoBrightness = value
            isSelected = value
        }

    override fun onClick(v: View?) {
        super.onClick(v)
        autoBrightnessEnabled = !autoBrightnessEnabled
    }
}
