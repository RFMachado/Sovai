package com.rafael.sovai.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.rafael.sovai.R

class Piece(context: Context, attrs: AttributeSet, x: Float, y: Float): View(context, attrs) {

    private var xAux = x
    private var yAux = y
    var isMoving = false

    var bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_bitmap)!!
    var size: Int = 0

    fun updateAxis(x: Float, y: Float) {
        xAux = x
        yAux = y
    }

    fun getx(): Float = xAux
    fun gety(): Float = yAux
}