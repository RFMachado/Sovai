package com.rafael.sovai.models

import android.content.Context
import android.graphics.BitmapFactory
import com.rafael.sovai.R

class Piece(context: Context, x: Float, y: Float) {

    private var xAux = x
    private var yAux = y
    var isMoving = false
    val player: Int = 0

    var bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_bitmap)!!
    var size: Int = 0

    fun updateAxis(x: Float, y: Float) {
        xAux = x
        yAux = y
    }

    fun getx(): Float = xAux
    fun gety(): Float = yAux
}