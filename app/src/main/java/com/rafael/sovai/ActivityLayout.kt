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

    var isMoving = false

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = createCheckerBoard(150)

        canvas.drawPaint(paint)
        canvas.drawBitmap(bitmap, xAux, yAux, null)
    }

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
                    xAux = ((event.x/150).toInt()*150).toFloat()
                    yAux = ((event.y/150).toInt()*150).toFloat()
                    invalidate()
                }
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