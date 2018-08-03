package com.rafael.sovai.models

import android.content.Context
import android.graphics.BitmapFactory
import com.rafael.sovai.R

class Piece(context: Context, x: Float, y: Float) {

    private var currentX = x
    private var currentY = y
    var isMoving = false
    val player: Int = 0
    var xmatrix: Int = 0
    var ymatrix: Int = 0

    var bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_bitmap)!!
    var size: Int = 0

    fun updateAxis(x: Float, y: Float) {
        currentX = x
        currentY = y
    }

    fun getx(): Float = currentX
    fun gety(): Float = currentY


}