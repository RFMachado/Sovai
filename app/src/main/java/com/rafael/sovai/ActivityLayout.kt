package com.rafael.sovai

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.rafael.sovai.models.Piece

class ActivityLayout(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val iconPlayerOne = BitmapFactory.decodeResource(context.resources, R.drawable.blue_android)!!
    private val iconPlayerTwo = BitmapFactory.decodeResource(context.resources, R.drawable.red_android)!!

    private val piece1 = Piece(0F, 0F, 1, iconPlayerOne)
    private val piece2 = Piece(1F, 0F,1, iconPlayerOne)
    private val piece3 = Piece(0F, 1F,1, iconPlayerOne)
    private val piece4 = Piece(1F, 1F, 1, iconPlayerOne)

    private val piece5 = Piece(6F, 6F, 2, iconPlayerTwo)
    private val piece6 = Piece(6F, 7F, 2, iconPlayerTwo)
    private val piece7 = Piece(7F, 6F, 2, iconPlayerTwo)
    private val piece8 = Piece(7F, 7F, 2, iconPlayerTwo)

    private var matrix = arrayOf(
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var multiply = 1
        val sizeWidth = width / 8
        val paint = createCheckerBoard(sizeWidth)

        canvas.drawPaint(paint)

        if (size == 0)
            multiply = sizeWidth

        listPiece.forEach { piece ->
            canvas.drawBitmap(piece.icon, piece.getx() * multiply, piece.gety() * multiply, null)
            piece.updateAxis(piece.getx() * multiply, piece.gety() * multiply)
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
                    piece.isMoving = (event.x >= x && event.x <= x + piece.icon.width &&
                            event.y >= y && event.y <= y + piece.icon.height)

                }
                MotionEvent.ACTION_MOVE -> {
                    if (piece.isMoving) {
                        piece.updateAxis(event.x - piece.icon.width / 2, event.y - piece.icon.height / 2)
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