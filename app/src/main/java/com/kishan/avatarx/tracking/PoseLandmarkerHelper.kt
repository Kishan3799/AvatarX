package com.kishan.avatarx.tracking

import android.content.Context
import android.graphics.Bitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

class PoseLandmarkerHelper(
    context: Context,
    private val listener: LandmarkListener
) {
    private var poseLandmarker: PoseLandmarker? = null

    interface LandmarkListener {
        fun onError(error: String)
        fun onResults(result: PoseLandmarkerResult, inputWidth:Int, inputHeight: Int)
    }

    init {
        try {
            val baseOption = BaseOptions.builder()
                .setModelAssetPath("pose_landmarker_lite.task")
                .build()

            val options = PoseLandmarker.PoseLandmarkerOptions.builder()
                .setBaseOptions(baseOption)
                .setRunningMode(RunningMode.LIVE_STREAM)
                .setNumPoses(1)
                .setMinPoseDetectionConfidence(0.5f)
                .setMinPosePresenceConfidence(0.5f)
                .setMinTrackingConfidence(0.5f)
                .setResultListener(::onResult)
                .setErrorListener {e -> listener.onError(e.message ?: "Unknown Pose Error")}
                .build()

            poseLandmarker = PoseLandmarker.createFromOptions(context, options)
        }catch (e: Exception){
            listener.onError("Init failed: ${e.message}")
        }
    }

    fun detectAsync(bitmap: Bitmap, frameTimeMs:Long){
        val mpImage: MPImage = BitmapImageBuilder(bitmap).build()
        poseLandmarker?.detectAsync(mpImage, frameTimeMs)
    }

    fun onResult(result: PoseLandmarkerResult, input: MPImage){
        listener.onResults(result,input.width, input.height)
    }

    fun close(){
        poseLandmarker?.close()
        poseLandmarker = null
    }


}