/*
 * Copyright (C) 2025 Halcyon Project
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

package com.halcyon.gamespace.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

object GameDetectionUtils {

    fun detectInstalledGames(context: Context): List<String> {
        val pm = context.packageManager
        val packages = pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0))
        val detected = mutableSetOf<String>()
        val whitelist = loadWhitelist(context)

        for (app in packages) {
            if (isGame(app) || whitelist.contains(app.packageName)) {
                detected.add(app.packageName)
            }
        }

        return detected.toList()
    }

    private fun isGame(app: ApplicationInfo): Boolean {
        if (app.category == ApplicationInfo.CATEGORY_GAME) return true
        if ((app.flags and ApplicationInfo.FLAG_IS_GAME) != 0) return true
        return false
    }

    private fun loadWhitelist(context: Context): Set<String> {
        return try {
            context.resources.getStringArray(
                context.resources.getIdentifier(
                    "default_game_whitelist",
                    "array",
                    context.packageName
                )
            ).toSet()
        } catch (e: Exception) {
            emptySet()
        }
    }
}