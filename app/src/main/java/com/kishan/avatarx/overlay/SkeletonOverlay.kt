package com.kishan.avatarx.overlay

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

private val CONNECTION = listOf(
    11 to 12, 11 to 13, 13 to 15, 12 to 14 , 14 to 16,
    11 to 23, 12 to 24, 23 to 24, 23 to 25, 25 to 27, 24 to 26, 26 to 28,
)

@Composable
fun SkeletonOverlay(
    landmarks:List<NormalizedLandmark>,
    imageWidth:Int,
    imageHeight:Int,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        if (landmarks.isEmpty()) return@Canvas

        fun point(i: Int) = mapLandmarkToCanvas(landmarks[i], imageWidth, imageHeight, size)



        CONNECTION.forEach { (a, b) ->
            if (a < landmarks.size && b < landmarks.size) {
                drawLine(
                    color = Color(0xFF00E5FF),
                    start = point(a),
                    end = point(b),
                    strokeWidth = 5f
                )
            }
        }

        landmarks.indices.forEach { i ->
            drawCircle(Color(0xFFFF4081), radius = 7f, center = point(i))
        }
    }
    
}

@Preview
@Composable
private fun PreivewView() {
    SkeletonOverlay(emptyList(), 10, 5 )
}