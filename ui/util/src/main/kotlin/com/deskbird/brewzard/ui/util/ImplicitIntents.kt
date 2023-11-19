package com.deskbird.brewzard.ui.util

import android.content.Context
import android.content.Intent
import android.content.Intent.CATEGORY_BROWSABLE
import android.net.Uri

fun Context.call(phoneNumber: String): Boolean {
    val uri = Uri.parse("tel:$phoneNumber")
    val intent = Intent(Intent.ACTION_CALL, uri)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
        return true
    }
    return false
}

fun Context.openWebsite(url: String): Boolean {
    val uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri).also { it.addCategory(CATEGORY_BROWSABLE) }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
        return true
    }
    return false
}

fun Context.openMap(latitude: Float, longitude: Float): Boolean {
    val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
        return true
    }
    return false
}
