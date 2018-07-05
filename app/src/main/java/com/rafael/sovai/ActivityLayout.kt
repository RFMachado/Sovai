package com.rafael.sovai

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class ActivityLayout(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var xAux = 0F
    private var yAux = 0F

    private var bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_bitmap)!!


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(bitmap, xAux-80, yAux-80, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                xAux = event.x
                yAux = event.y
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                xAux = event.x
                yAux = event.y
                invalidate()
            }
        }
        return true
    }
}