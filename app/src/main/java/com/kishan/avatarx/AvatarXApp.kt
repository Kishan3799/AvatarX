package com.kishan.avatarx

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kishan.avatarx.navigation.Screen
import com.kishan.avatarx.ui.camera.CameraTrackingScreen
import com.kishan.avatarx.ui.garment.GarmentSelectScreen
import com.kishan.avatarx.ui.home.HomeScreen
import com.kishan.avatarx.ui.permission.PermissionScreen
import com.kishan.avatarx.ui.tryon.TryOnScreen

@Composable
fun AvatarXApp(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var hasCameraPermission by remember { mutableStateOf(false) }

    if (!hasCameraPermission) {
        PermissionScreen(onGranted = { hasCameraPermission = true})
    }

    when(val screen = currentScreen) {
        is Screen.Home -> HomeScreen(
            onStart = {currentScreen = Screen.Camera}
        )
        is Screen.Camera -> CameraTrackingScreen(
            onContinue = {currentScreen = Screen.GarmentSelect}
        )
        is Screen.GarmentSelect -> GarmentSelectScreen (
            onGarmentChoose = {id -> currentScreen = Screen.TryOn(id)}
        )
        is Screen.TryOn -> TryOnScreen(
            initialGarmentId = screen.garmentId,
            onBack = {currentScreen = Screen.GarmentSelect}
        )
    }
}