package com.rafael.sovai

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class ActivityLayout(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var xAux = 0F
    private var yAux = 0F

    private var bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_bitmap)!!


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = createCheckerBoard(150)

        canvas.drawPaint(paint)
        canvas.drawBitmap(bitmap, xAux-80, yAux-80, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {
                xAux = event.x
                yAux = event.y
                invalidate()
            }
        }
        return true
    }

    private fun createCheckerBoard(pixelSize: Int): Paint {
        val bitmap = Bitmap.createBitmap(pixelSize * 2, pixelSize * 2, Bitmap.Config.ARGB_8888)

        val fill = Paint(ANTI_ALIAS_FLAG)
        fill.style = Paint.Style.FILL
        fill.color = Color.BLACK

        val canvas = Canvas(bitmap)
        val rect = Rect(0, 0, pixelSize, pixelSize)

        canvas.drawRect(rect, fill)

        rect.offset(pixelSize, pixelSize)
        canvas.drawRect(rect, fill)

        val paint = Paint(ANTI_ALIAS_FLAG)
        paint.shader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        return paint
    }
}