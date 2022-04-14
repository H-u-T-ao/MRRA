package com.mrra.utils

import android.content.ContentResolver
import android.net.Uri
import androidx.annotation.RawRes
import com.mrra.application.appContext

fun getRawUri(@RawRes rawId: Int): Uri {
    return Uri.parse(
        "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${appContext.packageName}/$rawId"
    )
}

fun getRawUriString(@RawRes rawId: Int): String {
    return "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${appContext.packageName}/$rawId"
}