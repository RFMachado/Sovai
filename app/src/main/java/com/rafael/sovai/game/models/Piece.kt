package com.rafael.sovai.game.models

import android.graphics.Bitmap

class Piece(
    var movingX: Float,
    var movingY: Float,
    val player: Int,
    val icon: Bitmap,
    var isMoving: Boolean = false,
    var xmatrix: Int = 0,
    var ymatrix: Int = 0,
    var lastPositionX: Float = 0f,
    var lastPositionY: Float = 0f
)