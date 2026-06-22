package com.kishan.avatarx.navigation

object Routes {
    const val HOME = "home"
    const val CAMERA = "camera"
    const val GARMENT_SELECT = "garment_select"
    const val TRY_ON = "try_on/{garmentId}"
    fun tryOn(garmentId: String) = "try_on/$garmentId"
}