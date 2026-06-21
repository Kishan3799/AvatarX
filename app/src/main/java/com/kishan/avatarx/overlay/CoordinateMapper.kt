package com.kishan.avatarx.overlay

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

fun mapLandmarkToCanvas(
    landmark: NormalizedLandmark,
    imageWidth:Int,
    imageHeight:Int,
    canvasSize: Size
): Offset {
    if (imageWidth == 0 || imageHeight == 0){
        return Offset(landmark.x() * canvasSize.width, landmark.y() * canvasSize.height)
    }

    val scale = maxOf(canvasSize.width/imageWidth, canvasSize.height/imageHeight)
    val scaledW = imageWidth * scale
    val scaledH = imageHeight * scale
    val offsetX = (canvasSize.width - scaledW) / 2f
    val offsetY = (canvasSize.height - scaledH) / 2f

    return Offset(
        x = landmark.x() *imageWidth * scale + offsetX,
        y = landmark.y() * imageHeight * scale + offsetY
    )
}
