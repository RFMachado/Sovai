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
            intArrayOf(0, 0, 0, 0, 0, 0, 2, 2),
            intArrayOf(0, 0, 0, 0, 0, 0, 2, 2)
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

                        if (canMoveOneStep(piece)) {

                            if (needToJump(piece)) {

                                canJump(piece)

                            } else {
                                piece.updateAxis(piece.xmatrix * size.toFloat(), piece.ymatrix * size.toFloat())
                                updateMatrix(piece, piece.xmatrix, piece.ymatrix)
                            }

                        } else {
                            moveBack(piece)
                        }

                        piece.isMoving = false
                        invalidate()
                    }
                }
            }
        }

        return true
    }

    private fun updateMatrix(piece: Piece, x: Int, y: Int) = with(piece) {
        matrix[x][y] = player
        matrix[lastPositionX.toInt()/size][lastPositionY.toInt()/size] = 0
    }

    private fun canJump(piece: Piece) {

        if (matrix[piece.xmatrix][piece.ymatrix] == piece.player) {
            val diffX = piece.xmatrix - piece.lastPositionX.toInt()/size
            val diffY = piece.ymatrix - piece.lastPositionY.toInt()/size

            val jumpX= piece.xmatrix + diffX
            val jumpY= piece.ymatrix + diffY

            if (jumpX in 0..7 && jumpY in 0..7) {

                if (matrix[jumpX][jumpY] == 0) {
                    piece.updateAxis(jumpX * size.toFloat(), jumpY * size.toFloat())
                    updateMatrix(piece, jumpX, jumpY)
                } else {
                    moveBack(piece)
                }

            } else {
                moveBack(piece)
            }

        } else {
            moveBack(piece)
        }

    }

    private fun canMoveOneStep(piece: Piece): Boolean {
        val lastPositionX = piece.lastPositionX.toInt()/size
        val lastPositionY = piece.lastPositionY.toInt()/size

        val currentX = piece.xmatrix
        val currentY = piece.ymatrix

        return ( (currentX >= (lastPositionX - 1)) && (currentX <= (lastPositionX + 1)) &&
                (currentY >= (lastPositionY - 1)) && (currentY <= (lastPositionY + 1)) )

    }

    private fun needToJump(piece: Piece) = matrix[piece.xmatrix][piece.ymatrix] != 0

    private fun moveBack(piece: Piece) = with(piece) {
        updateAxis(lastPositionX, lastPositionY)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}