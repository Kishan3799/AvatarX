package com.kishan.avatarx.util

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageProxy
import androidx.core.graphics.createBitmap

fun ImageProxy.toBitmap(isFrontCamera: Boolean): Bitmap {
    val plane = planes[0]
    val pixelStride = plane.pixelStride
    val rowStride = plane.rowStride
    val rowPadding = rowStride - pixelStride * width

    // Create bitmap wide enough to absorb the row padding, then crop to actual size
    val paddedBitmap = createBitmap(width + rowPadding / pixelStride, height)
    paddedBitmap.copyPixelsFromBuffer(plane.buffer)

    val cropped = if (rowPadding == 0) paddedBitmap
    else Bitmap.createBitmap(paddedBitmap, 0, 0, width, height)

    val matrix = Matrix().apply {
        postRotate(imageInfo.rotationDegrees.toFloat())
        if (isFrontCamera) postScale(-1f, 1f, cropped.width.toFloat(), cropped.height.toFloat())
    }
    return Bitmap.createBitmap(cropped, 0, 0, cropped.width, cropped.height, matrix, true)
}
