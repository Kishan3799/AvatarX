package com.kishan.avatarx.tracking

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

class PoseLandmarkerHelper(
    private val context: Context,
    private val listener: LandmarkListener
) {
    private var poseLandmarker: PoseLandmarker? = null

    var activeDelegate: Delegate = Delegate.GPU
        private set

    interface LandmarkListener {
        fun onError(error: String)
        fun onResults(result: PoseLandmarkerResult, inputWidth:Int, inputHeight: Int)
    }

    init {
        setupPoseLandmarker(Delegate.GPU)
    }

    private fun setupPoseLandmarker(delegate: Delegate) {
        try {
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath("pose_landmarker_lite.task")
                .setDelegate(delegate)
                .build()

            val options = PoseLandmarker.PoseLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setRunningMode(RunningMode.LIVE_STREAM)
                .setNumPoses(1)
                .setMinPoseDetectionConfidence(0.5f)
                .setMinPosePresenceConfidence(0.5f)
                .setMinTrackingConfidence(0.5f)
                .setResultListener(::onResult)
                .setErrorListener { e -> listener.onError(e.message ?: "Unknown pose error") }
                .build()

            poseLandmarker = PoseLandmarker.createFromOptions(context, options)
            activeDelegate = delegate
            Log.d("AvatarX", "PoseLandmarker initialized with delegate=$delegate")
        } catch (e: Exception) {
            if (delegate == Delegate.GPU) {
                // GPU not supported on this device — fall back to CPU automatically
                Log.w("AvatarX", "GPU delegate failed (${e.message}), falling back to CPU")
                setupPoseLandmarker(Delegate.CPU)
            } else {
                listener.onError("Init failed on CPU too: ${e.message}")
            }
        }
    }

    fun detectAsync(bitmap: Bitmap, frameTimeMs: Long) {
        try {
            val mpImage: MPImage = BitmapImageBuilder(bitmap).build()
            poseLandmarker?.detectAsync(mpImage, frameTimeMs)
        } catch (e: Exception) {
            if (activeDelegate == Delegate.GPU) {
                Log.w("AvatarX", "GPU inference failed at runtime, falling back to CPU")
                poseLandmarker?.close()
                setupPoseLandmarker(Delegate.CPU)
            } else {
                listener.onError("Detection failed: ${e.message}")
            }
        }
    }

    private fun onResult(result: PoseLandmarkerResult, input: MPImage) {
        listener.onResults(result, input.width, input.height)
    }

    fun close() {
        poseLandmarker?.close()
        poseLandmarker = null
    }


}