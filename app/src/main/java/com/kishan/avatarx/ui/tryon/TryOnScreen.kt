package com.kishan.avatarx.ui.tryon

import android.os.SystemClock
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import com.kishan.avatarx.model.GarmentRepository
import com.kishan.avatarx.overlay.GarmentOverlay
import com.kishan.avatarx.overlay.SkeletonOverlay
import com.kishan.avatarx.tracking.PoseLandmarkerHelper
import com.kishan.avatarx.ui.camera.CameraPreview
import com.kishan.avatarx.ui.camera.CameraTrackingViewModel
import com.kishan.avatarx.util.toBitmap

@Composable
fun TryOnScreen(
    initialGarmentId: String,
    onBack: () -> Unit,
    viewModel: CameraTrackingViewModel = viewModel()
) {
    val context = LocalContext.current
    var selectedGarmentId by remember { mutableStateOf(initialGarmentId) }
    var showSkeleton by remember { mutableStateOf(false) }

    val poseHelper = remember {
        PoseLandmarkerHelper(
            context = context,
            listener = object : PoseLandmarkerHelper.LandmarkListener{
                override fun onResults(result: PoseLandmarkerResult, w: Int, h: Int) {
                    viewModel.onPoseResult(result, w, h)
                }
                override fun onError(error: String) {
                    Log.e("AvatarX", "Pose error: $error")
                }
            }
        )
    }
    DisposableEffect(Unit) { onDispose { poseHelper.close() } }

    val garment = GarmentRepository.getById(selectedGarmentId)
    Log.d("AvatarX", "TryOnScreen: selectedGarmentId=$selectedGarmentId resolvedId=${garment.id} res=${garment.drawableRes}")

    Box(Modifier.fillMaxSize()) {
        CameraPreview(
            lensFacing = viewModel.lensFacing,
            onFrame = { imageProxy ->
                val isFront = viewModel.lensFacing == CameraSelector.LENS_FACING_FRONT
                val bitmap = imageProxy.toBitmap(isFront)
                poseHelper.detectAsync(bitmap, SystemClock.uptimeMillis())
                imageProxy.close()
            },
            modifier = Modifier.fillMaxSize()
        )

        GarmentOverlay(
            landmarks = viewModel.landmarks,
            imageWidth = viewModel.imageWidth,
            imageHeight = viewModel.imageHeight,
            garmentDrawableRes = garment.drawableRes,
            modifier = Modifier.fillMaxSize()
        )

        if (showSkeleton) {
            SkeletonOverlay(
                landmarks = viewModel.landmarks,
                imageWidth = viewModel.imageWidth,
                imageHeight = viewModel.imageHeight,
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Row {
                TextButton(onClick = { showSkeleton = !showSkeleton }) {
                    Text(if (showSkeleton) "Hide Skeleton" else "Show Skeleton", color = Color.White)
                }
                IconButton(onClick = { viewModel.toggleCamera() }) {
                    Icon(Icons.Default.Cameraswitch, contentDescription = "Switch camera", tint = Color.White)
                }
            }
        }

        LazyRow(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(GarmentRepository.garments) { g ->
                Image(
                    painter = painterResource(g.drawableRes),
                    contentDescription = g.name,
                    modifier = Modifier.size(56.dp).clickable { selectedGarmentId = g.id }
                )
            }
        }
    }
}