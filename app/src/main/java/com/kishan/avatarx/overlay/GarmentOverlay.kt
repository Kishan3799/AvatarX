package com.kishan.avatarx.overlay

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import kotlin.math.abs


private const val LEFT_SHOULDER = 11
private const val RIGHT_SHOULDER = 12
private const val LEFT_HIP = 23
private const val  RIGHT_HIP = 24

@Composable
fun GarmentOverlay(
    landmarks: List<NormalizedLandmark>,
    imageWidth: Int,
    imageHeight: Int,
    garmentDrawableRes: Int,
    modifier: Modifier = Modifier
) {
    val garmentBitmap = ImageBitmap.imageResource(id = garmentDrawableRes)

    Canvas(modifier = modifier.fillMaxSize()) {
        if (landmarks.size <= RIGHT_HIP) {
            Log.d("AvatarX", "GarmentOverlay: not enough landmarks (${landmarks.size})")
            return@Canvas
        }

        val leftShoulderPt = mapLandmarkToCanvas(landmarks[LEFT_SHOULDER], imageWidth, imageHeight, size)
        val rightShoulderPt = mapLandmarkToCanvas(landmarks[RIGHT_SHOULDER], imageWidth, imageHeight, size)
        val leftHipPt = mapLandmarkToCanvas(landmarks[LEFT_HIP], imageWidth, imageHeight, size)
        val rightHipPt = mapLandmarkToCanvas(landmarks[RIGHT_HIP], imageWidth, imageHeight, size)

        val shoulderMidX = (leftShoulderPt.x + rightShoulderPt.x) / 2f
        val shoulderMidY = (leftShoulderPt.y + rightShoulderPt.y) / 2f
        val hipMidY = (leftHipPt.y + rightHipPt.y) / 2f

        val shoulderWidthPx = abs(rightShoulderPt.x - leftShoulderPt.x)
        val garmentWidth = shoulderWidthPx * 1.6f

        val torsoHeightPx = hipMidY - shoulderMidY
        val aspectRatio = garmentBitmap.height.toFloat() / garmentBitmap.width.toFloat()
        val garmentHeight = if (torsoHeightPx > 0f) torsoHeightPx * 1.3f else garmentWidth * aspectRatio

        val left = (shoulderMidX - garmentWidth / 2f).toInt()
        val top = (shoulderMidY - garmentHeight * 0.12f).toInt()

        Log.d("AvatarX", "GarmentOverlay draw: w=$garmentWidth h=$garmentHeight left=$left top=$top")

        drawImage(
            image = garmentBitmap,
            dstOffset = IntOffset(left, top),
            dstSize = IntSize(garmentWidth.toInt().coerceAtLeast(1), garmentHeight.toInt().coerceAtLeast(1))
        )
    }
}