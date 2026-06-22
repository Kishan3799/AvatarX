package com.kishan.avatarx

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kishan.avatarx.model.GarmentRepository
import com.kishan.avatarx.navigation.Routes
import com.kishan.avatarx.navigation.Screen
import com.kishan.avatarx.ui.camera.CameraTrackingScreen
import com.kishan.avatarx.ui.garment.GarmentSelectScreen
import com.kishan.avatarx.ui.home.HomeScreen
import com.kishan.avatarx.ui.permission.PermissionScreen
import com.kishan.avatarx.ui.tryon.TryOnScreen

@Composable
fun AvatarXApp(modifier: Modifier = Modifier) {

    var hasCameraPermission by remember { mutableStateOf(false) }

    if (!hasCameraPermission) {
        PermissionScreen(onGranted = { hasCameraPermission = true})
        return
    }

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        enterTransition = { fadeIn(animationSpec = tween(250)) },
        exitTransition = { fadeOut(animationSpec = tween(250)) }
    ){
        composable(Routes.HOME){
            HomeScreen(onStart = {navController.navigate(Routes.CAMERA)})
        }
        composable(Routes.CAMERA) {
            CameraTrackingScreen(onContinue = {navController.navigate(Routes.GARMENT_SELECT)})
        }
        composable(Routes.GARMENT_SELECT) {
            GarmentSelectScreen(onGarmentChoose = { id -> navController.navigate(Routes.tryOn(id))})
        }
        composable(
            route = Routes.TRY_ON,
            arguments = listOf(navArgument("garmentId") {type = NavType.StringType})
        ) {backStackEntry ->
            val garmentId = backStackEntry.arguments?.getString("garmentId")
                ?: GarmentRepository.garments.first().id
            TryOnScreen(
                initialGarmentId = garmentId,
                onBack = {navController.popBackStack()}
            )
        }
    }
}