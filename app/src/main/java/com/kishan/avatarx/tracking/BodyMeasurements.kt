package com.kishan.avatarx.tracking


import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import kotlin.math.sqrt

data class BodyMeasurements(
    val shoulderWidthPx: Float,
    val hipWidthPx: Float,
    val shoulderToHipRatio: Float
)

object BodyMeasurementCalculator{
    private const val LEFT_SHOULDER = 11
    private const val RIGHT_SHOULDER = 12
    private const val LEFT_HIP = 23
    private const val RIGHT_HIP = 24

    fun calculate(landmarks:List<NormalizedLandmark>, imageWidth: Int, imageHeight:Int): BodyMeasurements? {
        if (landmarks.size <= RIGHT_HIP ) return  null

        val shoulderWidth = distance(landmarks[LEFT_SHOULDER], landmarks[RIGHT_SHOULDER], imageWidth, imageHeight)
        val hipWidth = distance(landmarks[LEFT_HIP], landmarks[RIGHT_HIP], imageWidth, imageHeight)

        return BodyMeasurements(
            shoulderWidthPx = shoulderWidth,
            hipWidthPx = hipWidth,
            shoulderToHipRatio = if (hipWidth > 0f) shoulderWidth / hipWidth else 0f
        )
    }

    private fun distance(a: NormalizedLandmark, b: NormalizedLandmark, w:Int, h:Int): Float {
        val dx = (a.x() - b.x()) * w
        val dy = (a.y() - b.y()) * w
        return sqrt(dx*dx + dy*dy)
    }
}

