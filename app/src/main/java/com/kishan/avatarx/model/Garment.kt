package com.kishan.avatarx.model

import com.kishan.avatarx.R

data class Garment(
    val id: String,
    val name: String,
    val drawableRes:Int,
)

object GarmentRepository {
    val garments = listOf(
        Garment("t_shirt-1", "Garment T-Shirt 1", R.drawable.garment_tshirt1),
        Garment("t_shirt-2", "Garment T-Shirt 2", R.drawable.garment_tshirt2),
        Garment("t_shirt-3", "Garment T-Shirt 3", R.drawable.garment_tshirt3),
        Garment("t_shirt-4", "Garment T-Shirt 4", R.drawable.garment_tshirt4),
        Garment("t_shirt-5", "Garment T-Shirt 5", R.drawable.garment_tshirt5),
        Garment("t_shirt-6", "Garment T-Shirt 6", R.drawable.garment_tshirt6),
        Garment("t_shirt-7", "Garment T-Shirt 7", R.drawable.garment_tshirt7),
        Garment("t_shirt-8", "Garment T-Shirt 8", R.drawable.garment_tshirt8),
        Garment("t_shirt-9", "Garment T-Shirt 9", R.drawable.garment_tshirt9),
    )

    fun getById(id:String) = garments.find {it.id == id} ?: garments.first()
}