package com.kishan.avatarx.navigation

sealed class Screen {
    data object Home: Screen()
    data object Camera: Screen()
    data object GarmentSelect: Screen()
    data class TryOn(val garmentId:String): Screen()
}