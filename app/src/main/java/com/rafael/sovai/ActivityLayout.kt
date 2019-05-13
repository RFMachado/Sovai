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

    var matrix = arrayOf(
            intArrayOf(1, 1, 0, 0, 0, 0, 0, 0),
            intArrayOf(1, 1, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 1, 1),
            intArrayOf(0, 0, 0, 0, 0, 0, 1, 1)
    )

    private val listPiece: MutableList<Piece> = mutableListOf(piece1, piece2, piece3, piece4, piece5, piece6, piece7, piece8)
    var size: Int = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val sizeWidth = width / 8
        val paint = createCheckerBoard(sizeWidth)
        var mulitply = 1

        if (size == 0) {
            mulitply = sizeWidth
        }

        canvas.drawPaint(paint)

        listPiece.forEach { piece ->
            canvas.drawBitmap(piece.bitmap, piece.getx() * mulitply, piece.gety() * mulitply, null)
            piece.updateAxis(piece.getx() * mulitply, piece.gety() * mulitply)
        }

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

        for(i in 0 until listPiece.size) {
            val piece = listPiece[i]
            val x = piece.getx()
            val y = piece.gety()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    piece.updateLastPosition(piece.movingX, piece.movingY)
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
                        piece.xmatrix = (event.x / size).toInt()
                        piece.ymatrix = (event.y / size).toInt()

                        if (matrix[piece.xmatrix][piece.ymatrix] == 1) {
                            piece.updateAxis(piece.lastPositionX, piece.lastPositionY )
                        } else {
                            piece.updateAxis(piece.xmatrix * size.toFloat(), piece.ymatrix * size.toFloat())
                            matrix[piece.xmatrix][piece.ymatrix] = 1
                            matrix[piece.lastPositionX.toInt()/size][piece.lastPositionY.toInt()/size] = 0
                        }

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