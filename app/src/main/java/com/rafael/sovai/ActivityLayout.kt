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
    lateinit var currentPiece: Piece
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
            piece.updateNextPosition(piece.getx() * multiply, piece.gety() * multiply)
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
            currentPiece = listPiece[i]
            val x = currentPiece.getx()
            val y = currentPiece.gety()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    currentPiece.updateLastPosition(currentPiece.movingX, currentPiece.movingY)
                    currentPiece.isMoving = (event.x >= x && event.x <= x + currentPiece.icon.width &&
                            event.y >= y && event.y <= y + currentPiece.icon.height)

                }
                MotionEvent.ACTION_MOVE -> {
                    if (currentPiece.isMoving) {
                        currentPiece.updateNextPosition(event.x - currentPiece.icon.width / 2, event.y - currentPiece.icon.height / 2)
                        invalidate()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (currentPiece.isMoving) {
                        currentPiece.xmatrix = (event.x / size).toInt()
                        currentPiece.ymatrix = (event.y / size).toInt()

                        if (canMoveOneStep()) {

                            if (needToJump()) {
                                jump()

                            } else {
                                currentPiece.updateNextPosition(currentPiece.xmatrix * size.toFloat(), currentPiece.ymatrix * size.toFloat())
                                updateMatrix(currentPiece.xmatrix, currentPiece.ymatrix)
                            }

                        } else {
                            moveBack()
                        }

                        currentPiece.isMoving = false
                        invalidate()
                    }
                }
            }
        }

        return true
    }

    private fun updateMatrix(x: Int, y: Int) = with(currentPiece) {
        matrix[x][y] = player
        matrix[lastPositionX.toInt()/size][lastPositionY.toInt()/size] = 0
    }

    private fun jump() = with(currentPiece) {
        if (matrix[xmatrix][ymatrix] == player) {
            val diffX = xmatrix - lastPositionX.toInt()/size
            val diffY = ymatrix - lastPositionY.toInt()/size

            val jumpX= xmatrix + diffX
            val jumpY= ymatrix + diffY

            if (jumpX in 0..7 && jumpY in 0..7) {

                if (matrix[jumpX][jumpY] == 0) {
                    updateNextPosition(jumpX * size.toFloat(), jumpY * size.toFloat())
                    updateMatrix(jumpX, jumpY)
                    return
                }
            }
        }

        moveBack()
    }

    private fun canMoveOneStep(): Boolean {
        val lastPositionX = currentPiece.lastPositionX.toInt()/size
        val lastPositionY = currentPiece.lastPositionY.toInt()/size

        val currentX = currentPiece.xmatrix
        val currentY = currentPiece.ymatrix

        return ( (currentX >= (lastPositionX - 1)) && (currentX <= (lastPositionX + 1)) &&
                (currentY >= (lastPositionY - 1)) && (currentY <= (lastPositionY + 1)) )

    }

    private fun needToJump() = matrix[currentPiece.xmatrix][currentPiece.ymatrix] != 0

    private fun moveBack() = with(currentPiece) {
        updateNextPosition(lastPositionX, lastPositionY)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}