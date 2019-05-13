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
    private val piece2 = Piece(context,1F, 0F)
    private val piece3 = Piece(context,0F, 1F)
    private val piece4 = Piece(context,1F, 1F)

    private val piece5 = Piece(context,6F, 6F)
    private val piece6 = Piece(context,6F, 7F)
    private val piece7 = Piece(context,7F, 6F)
    private val piece8 = Piece(context,7F, 7F)

    private val listObject: MutableList<Piece> = mutableListOf(piece1, piece2, piece3, piece4, piece5, piece6, piece7, piece8)
    var size: Int = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val sizeWidth = width / 8
        val paint = createCheckerBoard(sizeWidth)
        var multply = 1

        if (size == 0) {
            multply = sizeWidth
        }

        canvas.drawPaint(paint)
        canvas.drawBitmap(piece1.bitmap, piece1.getx() * multply, piece1.gety() * multply, null)
        canvas.drawBitmap(piece2.bitmap, piece2.getx() * multply, piece2.gety() * multply, null)
        canvas.drawBitmap(piece3.bitmap, piece3.getx() * multply, piece3.gety() * multply, null)
        canvas.drawBitmap(piece4.bitmap, piece4.getx() * multply, piece4.gety() * multply, null)

        canvas.drawBitmap(piece5.bitmap, piece5.getx() * multply, piece5.gety() * multply, null)
        canvas.drawBitmap(piece6.bitmap, piece6.getx() * multply, piece6.gety() * multply, null)
        canvas.drawBitmap(piece7.bitmap, piece7.getx() * multply, piece7.gety() * multply, null)
        canvas.drawBitmap(piece8.bitmap, piece8.getx() * multply, piece8.gety() * multply, null)

        piece1.updateAxis(piece1.getx() * multply, piece1.gety() * multply)
        piece2.updateAxis(piece2.getx() * multply, piece2.gety() * multply)
        piece3.updateAxis(piece3.getx() * multply, piece3.gety() * multply)
        piece4.updateAxis(piece4.getx() * multply, piece4.gety() * multply)

        piece5.updateAxis(piece5.getx() * multply, piece5.gety() * multply)
        piece6.updateAxis(piece6.getx() * multply, piece6.gety() * multply)
        piece7.updateAxis(piece7.getx() * multply, piece7.gety() * multply)
        piece8.updateAxis(piece8.getx() * multply, piece8.gety() * multply)

        size = sizeWidth

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

        for(i in 0 until listObject.size) {
            val piece = listObject[i]
            val x = piece.getx()
            val y = piece.gety()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    println("ACTION_DOWN")
                    piece.updateLastPosition(piece.movingX, piece.movingY)
                    piece.isMoving = (event.x >= x && event.x <= x + piece.bitmap.width &&
                            event.y >= y && event.y <= y + piece.bitmap.height)

                }
                MotionEvent.ACTION_MOVE -> {
                    println("ACTION_MOVE")
                    if (piece.isMoving) {
                        piece.updateAxis(event.x - piece.bitmap.width / 2, event.y - piece.bitmap.height / 2)
                        invalidate()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    println("ACTION_UP")
                    if (piece.isMoving) {
                        piece.xmatrix = (event.x / size).toInt()
                        piece.ymatrix = (event.y / size).toInt()
                        piece.updateAxis(piece.xmatrix * size.toFloat(), piece.ymatrix * size.toFloat() )

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