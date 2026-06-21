package com.kishan.avatarx.ui.camera

import android.widget.TabWidget
import androidx.camera.core.CameraSelector
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import com.kishan.avatarx.tracking.BodyMeasurementCalculator
import com.kishan.avatarx.tracking.BodyMeasurements

class CameraTrackingViewModel : ViewModel() {
    var landmarks by mutableStateOf<List<NormalizedLandmark>>(emptyList())
        private set
    var imageWidth by mutableStateOf(0)
        private set
    var imageHeight by mutableStateOf(0)
        private set
    var mesaurements by mutableStateOf<BodyMeasurements?>(null)
        private set
    var lensFacing by mutableStateOf(CameraSelector.LENS_FACING_FRONT)
        private set

    fun onPoseResult(result: PoseLandmarkerResult, width: Int, height: Int) {
        val pose = result.landmarks().firstOrNull() ?: emptyList()
        landmarks = pose
        imageWidth = width
        imageHeight = height
        mesaurements = BodyMeasurementCalculator.calculate(pose,width, height)
    }

    fun toggleCamera() {
        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT)
            CameraSelector.LENS_FACING_BACK else CameraSelector.LENS_FACING_FRONT
    }


}