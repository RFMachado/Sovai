package com.rafael.sovai.game.models

import android.graphics.Bitmap

class Piece(x: Float, y: Float, val player: Int, val icon: Bitmap) {

    var movingX = x
    var movingY = y
    var isMoving = false
    var xmatrix: Int = 0
    var ymatrix: Int = 0
    var lastPositionX: Float = 0f
    var lastPositionY: Float = 0f

    fun updateNextPosition(x: Float, y: Float) {
        movingX = x
        movingY = y
    }

    fun updateLastPosition(x: Float, y: Float) {
        lastPositionX = x
        lastPositionY = y
    }

    fun getx(): Float = movingX
    fun gety(): Float = movingY
}