package com.rafael.sovai.models

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.rafael.sovai.R

class Piece(context: Context, attrs: AttributeSet): View(context, attrs) {

    private var xAux = 0F
    private var yAux = 0F

    private var bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_bitmap)!!
    var size: Int = 0

    private var isMoving = false

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isMoving = (event.x >= xAux && event.x <= xAux + bitmap.width &&
                        event.y >= yAux && event.y <= yAux + bitmap.height)
            }
            MotionEvent.ACTION_MOVE -> {
                if (isMoving) {
                    xAux = event.x - bitmap.width/2
                    yAux = event.y - bitmap.height/2
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isMoving) {
                    xAux = ((event.x/size).toInt()*size).toFloat()
                    yAux = ((event.y/size).toInt()*size).toFloat()
                    invalidate()
                }
            }
        }
        return true
    }
}