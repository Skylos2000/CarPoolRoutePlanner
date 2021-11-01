package com.devs.carpoolrouteplanner.utils

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.devs.carpoolrouteplanner.R
import java.io.IOException
import java.util.*

fun Context.getConfigValue(name: String): String? {
    try {
        val rawResource = resources.openRawResource(R.raw.dev_config)
        val properties = Properties()
        properties.load(rawResource)
        return properties.getProperty(name)
    } catch (e: Resources.NotFoundException) {
        // TODO: Come up with a better tag than Helper
        Log.e("Helper", "Unable to find the config file: " + e.message)
    } catch (e: IOException) {
        Log.e("Helper", "Failed to open config file.");
    }

    return null
}
