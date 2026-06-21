package com.kishan.avatarx.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onStart: ()-> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("AvatarX", style = MaterialTheme.typography.headlineLarge)
            Text("Real Time avatar & virtual try - on")
            Spacer(Modifier.height(24.dp))
            Button(onStart) {
                Text("Get Started")
            }

        }
    }
}

@Preview
@Composable
private fun PreviewScreen() {
    HomeScreen({})
}