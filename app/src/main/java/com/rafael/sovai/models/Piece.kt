package com.rafael.sovai.models

import android.content.Context
import android.graphics.BitmapFactory
import com.rafael.sovai.R

class Piece(context: Context, x: Float, y: Float) {

    var movingX = x
    var movingY = y
    var isMoving = false
    var player: Int = 0
    var xmatrix: Int = 0
    var ymatrix: Int = 0
    var lastPositionX: Float = 0f
    var lastPositionY: Float = 0f

    var bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_bitmap)!!
    var size: Int = 0

    fun updateAxis(x: Float, y: Float) {
        movingX = x
        movingY = y
    }

    fun getx(): Float = movingX
    fun gety(): Float = movingY

    fun updateLastPosition(x: Float, y: Float) {
        lastPositionX = x
        lastPositionY = y
    }

}