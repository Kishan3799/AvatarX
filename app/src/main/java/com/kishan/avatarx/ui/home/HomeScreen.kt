package com.kishan.avatarx.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kishan.avatarx.ui.theme.AvatarXAccentPink
import com.kishan.avatarx.ui.theme.AvatarXMuted
import com.kishan.avatarx.ui.theme.AvatarXTeal

@Composable
fun HomeScreen(onStart: ()-> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(MaterialTheme.colorScheme.background, AvatarXTeal.copy(0.8f))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                "AvatarX",
                style = MaterialTheme.typography.headlineLarge,
                color = AvatarXTeal,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Real-time avatar tracking & virtual try-on",
                style = MaterialTheme.typography.bodyLarge,
                color = AvatarXMuted
            )
            Spacer(Modifier.height(40.dp))
            Button(
                onClick = onStart,
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AvatarXTeal),
                contentPadding = PaddingValues(horizontal = 40.dp, vertical = 14.dp)
            ) {
                Text("Get Started", color = androidx.compose.ui.graphics.Color.Black, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(12.dp))
            Text("Powered by MediaPipe Pose Tracking", style = MaterialTheme.typography.bodyMedium, color = AvatarXAccentPink)

        }
    }
}

@Preview
@Composable
private fun PreviewScreen() {
    HomeScreen({})
}