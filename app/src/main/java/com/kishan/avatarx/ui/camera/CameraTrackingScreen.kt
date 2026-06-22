package com.kishan.avatarx.ui.camera

import android.os.SystemClock
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import com.kishan.avatarx.overlay.SkeletonOverlay
import com.kishan.avatarx.tracking.PoseLandmarkerHelper
import com.kishan.avatarx.util.toBitmap

@Composable
fun CameraTrackingScreen(
    onContinue: () -> Unit,
    viewModel: CameraTrackingViewModel = viewModel()
) {
    val context = LocalContext.current

    val poseHelper = remember {
        PoseLandmarkerHelper(
            context = context,
            listener = object : PoseLandmarkerHelper.LandmarkListener {
                override fun onError(error: String) {
                    Log.e("AvatarX" , "Pose error $error")
                }

                override fun onResults(
                    result: PoseLandmarkerResult,
                    inputWidth: Int,
                    inputHeight: Int,
                ) {
                    viewModel.onPoseResult(result,inputWidth,inputHeight)
                }
            }
        )
    }

   

    DisposableEffect(Unit) {
        onDispose {  poseHelper.close() }
    }

    Box(modifier = Modifier.fillMaxSize()){
        CameraPreview(
            lensFacing = viewModel.lensFacing,
            onFrame = {imageProxy ->
                val isFront = viewModel.lensFacing == CameraSelector.LENS_FACING_FRONT
                val bitmap = imageProxy.toBitmap(isFront)
                poseHelper.detectAsync(bitmap, SystemClock.uptimeMillis())
                imageProxy.close()
            },
            modifier = Modifier.fillMaxSize()
        )

        SkeletonOverlay(landmarks = viewModel.landmarks, imageWidth = viewModel.imageWidth, imageHeight = viewModel.imageHeight, modifier = Modifier.fillMaxSize())

        viewModel.mesaurements?.let { m ->
            Card(
                modifier = Modifier.align(Alignment.TopStart).windowInsetsPadding(WindowInsets.statusBars).padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(0.6f)))
            {
                Column(Modifier.padding(12.dp)) {
                    Text("Shoulder width: ${m.shoulderWidthPx.toInt()}px", color = Color.White)
                    Text("Shoulder:Hip ratio: %.2f".format(m.shoulderToHipRatio), color = Color.White)
                }
            }
        }

        IconButton(
            { viewModel.toggleCamera()},
            modifier = Modifier.align(Alignment.TopEnd).windowInsetsPadding(WindowInsets.statusBars).padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Cameraswitch, contentDescription = "Switch Camera", tint = Color.White)
        }

        Button(onClick = onContinue, modifier = Modifier.align(Alignment.BottomCenter).windowInsetsPadding(WindowInsets.navigationBars).padding(24.dp)) {
            Text("Continue to Try-On")
        }
    }
}