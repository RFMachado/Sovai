package com.rafael.sovai

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.rafael.sovai.models.Piece

class ActivityLayout(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val piece1 = Piece(context,0F, 0F)
    private val piece2 = Piece(context,140F, 140F)
    private val piece3 = Piece(context,0F, 140F)
    private val piece4 = Piece(context,140F, 0F)

    private val listObject: MutableList<Piece> = mutableListOf(piece1, piece2, piece3, piece4)
    var size: Int = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val sizeWidth = width / 8
        size = sizeWidth

        val paint = createCheckerBoard(sizeWidth)

        canvas.drawPaint(paint)
        canvas.drawBitmap(piece1.bitmap, piece1.getx(), piece1.gety(), null)
        canvas.drawBitmap(piece2.bitmap, piece2.getx(), piece2.gety(), null)
        canvas.drawBitmap(piece3.bitmap, piece3.getx(), piece3.gety(), null)
        canvas.drawBitmap(piece4.bitmap, piece4.getx(), piece4.gety(), null)

        piece1.updateAxis(piece1.getx(), piece1.gety())
        piece2.updateAxis(piece2.getx(), piece2.gety())
        piece3.updateAxis(piece3.getx(), piece3.gety())
        piece4.updateAxis(piece4.getx(), piece4.gety())
    }

    private fun createCheckerBoard(size: Int): Paint {
        val bitmap = Bitmap.createBitmap(size * 2, size * 2, Bitmap.Config.ARGB_8888)

        val fill = Paint(ANTI_ALIAS_FLAG)
        fill.style = Paint.Style.FILL
        fill.color = Color.BLACK

        val canvas = Canvas(bitmap)
        val rect = Rect(0, 0, size, size)

        canvas.drawRect(rect, fill)

        rect.offset(size, size)
        canvas.drawRect(rect, fill)

        val paint = Paint(ANTI_ALIAS_FLAG)
        paint.shader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        return paint
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        for(i in 0..(listObject.size-1)) {
            val piece = listObject[i]
            val x = piece.getx()
            val y = piece.gety()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    piece.isMoving = (event.x >= x && event.x <= x + piece.bitmap.width &&
                            event.y >= y && event.y <= y + piece.bitmap.height)

                }
                MotionEvent.ACTION_MOVE -> {
                    if (piece.isMoving) {
                        piece.updateAxis(event.x - piece.bitmap.width / 2, event.y - piece.bitmap.height / 2)
                        invalidate()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (piece.isMoving) {
                        piece.updateAxis(((event.x / size).toInt() * size).toFloat(), ((event.y / size).toInt() * size).toFloat())
                        piece.isMoving = false
                        invalidate()
                    }
                }
            }
        }

        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}