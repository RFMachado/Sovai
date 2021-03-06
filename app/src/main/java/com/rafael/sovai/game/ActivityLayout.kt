package com.rafael.sovai.game

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.rafael.sovai.R
import com.rafael.sovai.game.models.Piece
import com.rafael.sovai.main.OnCustomEventListener
import util.Util

class ActivityLayout(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val congrats by lazy { context.getString(R.string.congratulations) }
    private val playAgain by lazy { context.getString(R.string.play_again) }
    private val backMenu by lazy { context.getString(R.string.back_menu) }

    private val iconPlayerOne = Util.getBitmap(context, R.drawable.circle_player_one)
    private val iconPlayerTwo = Util.getBitmap(context, R.drawable.circle_player_two)

    private val piece1 = Piece(0F, 0F, 1, iconPlayerOne)
    private val piece2 = Piece(1F, 0F, 1, iconPlayerOne)
    private val piece3 = Piece(0F, 1F, 1, iconPlayerOne)
    private val piece4 = Piece(1F, 1F, 1, iconPlayerOne)

    private val piece5 = Piece(6F, 6F, 2, iconPlayerTwo)
    private val piece6 = Piece(6F, 7F, 2, iconPlayerTwo)
    private val piece7 = Piece(7F, 6F, 2, iconPlayerTwo)
    private val piece8 = Piece(7F, 7F, 2, iconPlayerTwo)

    private var matrix = initializeTable()

    private var listPiece: MutableList<Piece> = mutableListOf(piece1, piece2, piece3, piece4, piece5, piece6, piece7, piece8)
    lateinit var currentPiece: Piece
    var playerTurn = 1
    var mListener: OnCustomEventListener? = null
    var size: Int = 0

    var lastX = 0f
    var lastY = 0f

    var showNextMove: Boolean = false
    var isMovingPiece: Boolean = false
    private val nextMovePaint = Paint()
    private val canMovePaint = Paint()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var multiply = 1
        val sizeWidth = width / 8
        val paint = createCheckerBoard(sizeWidth)

        canvas.drawPaint(paint)

        if (showNextMove)
            nextMove(sizeWidth, canvas)

        if (size == 0)
            multiply = sizeWidth

        if (!isMovingPiece)
            showWhatPieceCanMove(sizeWidth, canvas)

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

    private fun createNotAllowedMoveBoard(size: Int): Paint {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

        val fill = Paint()
        fill.style = Paint.Style.FILL
        fill.color = Color.RED
        fill.alpha = 120
        fill.strokeWidth = 3f

        val canvas = Canvas(bitmap)
        val rect = Rect(0, 0, size, size)

        canvas.drawRect(rect, fill)

        rect.offset(size, size)
        canvas.drawRect(rect, fill)

        val paint = Paint()
        paint.shader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        return paint
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        for (i in 0 until listPiece.size) {
            currentPiece = listPiece[i]
            val x = currentPiece.getx()
            val y = currentPiece.gety()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (playerTurn == currentPiece.player) {
                        currentPiece.updateLastPosition(currentPiece.movingX, currentPiece.movingY)
                        currentPiece.isMoving = (event.x >= x && event.x <= x + currentPiece.icon.width &&
                                event.y >= y && event.y <= y + currentPiece.icon.height)

                        isMovingPiece = true
                        showNextMove = true
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (currentPiece.isMoving && playerTurn == currentPiece.player) {
                        currentPiece.updateNextPosition(event.x - currentPiece.icon.width / 2, event.y - currentPiece.icon.height / 2)

                        lastX = currentPiece.lastPositionX
                        lastY = currentPiece.lastPositionY
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
                                playerTurn = updatePlayerTurn()
                            }
                        } else {
                            moveBack()
                        }

                        checkWin()

                        isMovingPiece = false
                        currentPiece.isMoving = false
                        showNextMove = false

                        invalidate()
                    }
                }
            }
        }

        return true
    }

    private fun checkWin() {
        if (matrix[6][6] == 1 && matrix[6][7] == 1 && matrix[7][6] == 1 && matrix[7][7] == 1)
            showWinDialog("1")

        if (matrix[0][0] == 2 && matrix[0][1] == 2 && matrix[1][0] == 2 && matrix[1][1] == 2)
            showWinDialog("2")
    }

    private fun updateMatrix(x: Int, y: Int) = with(currentPiece) {
        matrix[x][y] = player
        matrix[lastPositionX.toInt() / size][lastPositionY.toInt() / size] = 0
    }

    private fun jump() = with(currentPiece) {
        if (matrix[xmatrix][ymatrix] == player) {
            val jumpX = 2 * xmatrix - lastPositionX.toInt() / size
            val jumpY = 2 * ymatrix - lastPositionY.toInt() / size

            if (jumpX in 0..7 && jumpY in 0..7) {

                if (matrix[jumpX][jumpY] == 0) {
                    updateNextPosition(jumpX * size.toFloat(), jumpY * size.toFloat())
                    updateMatrix(jumpX, jumpY)
                    playerTurn = updatePlayerTurn()
                    return
                }
            }
        }

        moveBack()
    }

    private fun canMoveOneStep(): Boolean {
        val lastPositionX = currentPiece.lastPositionX.toInt() / size
        val lastPositionY = currentPiece.lastPositionY.toInt() / size

        val currentX = currentPiece.xmatrix
        val currentY = currentPiece.ymatrix

        return ((currentX >= (lastPositionX - 1)) && (currentX <= (lastPositionX + 1)) &&
                (currentY >= (lastPositionY - 1)) && (currentY <= (lastPositionY + 1)))
    }

    private fun needToJump() = matrix[currentPiece.xmatrix][currentPiece.ymatrix] != 0

    private fun moveBack() = with(currentPiece) {
        updateNextPosition(lastPositionX, lastPositionY)
    }

    private fun showWinDialog(player: String) = AlertDialog.Builder(context).apply {
        setTitle(congrats)
        setMessage(context.getString(R.string.win, player))
        setPositiveButton(playAgain) { _, _ ->
            restartGame()
        }
        setNegativeButton(backMenu) { _, _ ->
            mListener?.onBackMenu()
        }

        create()
        show()
    }

    private fun restartGame() {
        matrix = initializeTable()

        listPiece.clear()
        listPiece = initializePieces()

        size = 0

        invalidate()
    }

    private fun initializePieces(): MutableList<Piece> {
        val piece1 = Piece(0F, 0F, 1, iconPlayerOne)
        val piece2 = Piece(1F, 0F, 1, iconPlayerOne)
        val piece3 = Piece(0F, 1F, 1, iconPlayerOne)
        val piece4 = Piece(1F, 1F, 1, iconPlayerOne)

        val piece5 = Piece(6F, 6F, 2, iconPlayerTwo)
        val piece6 = Piece(6F, 7F, 2, iconPlayerTwo)
        val piece7 = Piece(7F, 6F, 2, iconPlayerTwo)
        val piece8 = Piece(7F, 7F, 2, iconPlayerTwo)

        return mutableListOf(piece1, piece2, piece3, piece4, piece5, piece6, piece7, piece8)
    }

    private fun initializeTable() = arrayOf(
            intArrayOf(1, 1, 0, 0, 0, 0, 0, 0),
            intArrayOf(1, 1, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 2, 2),
            intArrayOf(0, 0, 0, 0, 0, 0, 2, 2)
    )

    private fun updatePlayerTurn() = if (playerTurn == 1) 2 else 1

    private fun showWhatPieceCanMove(sizeWidth: Int, canvas: Canvas) {
        val width = sizeWidth.toFloat()

        canMovePaint.color = Color.GREEN
        canMovePaint.strokeWidth = 4f
        canMovePaint.style = Paint.Style.STROKE

        matrix.forEachIndexed { i, line ->
            line.forEachIndexed { j, item ->
                if (item == playerTurn) {
                    canvas.drawRect(i * width, j * width, i * width + width, j * width + width, canMovePaint)
                }
            }
        }
    }

    private fun nextMove(sizeWidth: Int, canvas: Canvas) {
        val boardNotAllowedMove = createNotAllowedMoveBoard(sizeWidth)
        canvas.drawPaint(boardNotAllowedMove)

        val xmatrix = (lastX / size).toInt()
        val ymatrix = (lastY / size).toInt()

        nextMovePaint.color = Color.GREEN
        nextMovePaint.strokeWidth = 4f
        nextMovePaint.alpha = 150
        nextMovePaint.style = Paint.Style.FILL

        val width = sizeWidth.toFloat()

        val right = matrix.getOrNull(xmatrix + 1)?.getOrNull(ymatrix)
        val left = matrix.getOrNull(xmatrix - 1)?.getOrNull(ymatrix)
        val top = matrix.getOrNull(xmatrix)?.getOrNull(ymatrix - 1)
        val bottom = matrix.getOrNull(xmatrix)?.getOrNull(ymatrix + 1)

        val topRight = matrix.getOrNull(xmatrix + 1)?.getOrNull(ymatrix - 1)
        val bottomRight = matrix.getOrNull(xmatrix + 1)?.getOrNull(ymatrix + 1)
        val topLeft = matrix.getOrNull(xmatrix - 1)?.getOrNull(ymatrix - 1)
        val bottomLeft = matrix.getOrNull(xmatrix - 1)?.getOrNull(ymatrix + 1)

        val jumpRight = matrix.getOrNull(xmatrix + 2)?.getOrNull(ymatrix)
        val jumpLeft = matrix.getOrNull(xmatrix - 2)?.getOrNull(ymatrix)
        val jumpTop = matrix.getOrNull(xmatrix)?.getOrNull(ymatrix - 2)
        val jumpBottom = matrix.getOrNull(xmatrix)?.getOrNull(ymatrix + 2)

        val jumpTopRight = matrix.getOrNull(xmatrix + 2)?.getOrNull(ymatrix - 2)
        val jumpBottomRight = matrix.getOrNull(xmatrix + 2)?.getOrNull(ymatrix + 2)
        val jumpTopLeft = matrix.getOrNull(xmatrix - 2)?.getOrNull(ymatrix - 2)
        val jumpBottomLeft = matrix.getOrNull(xmatrix - 2)?.getOrNull(ymatrix + 2)

        if (right == 0 || (right == playerTurn && jumpRight == 0))
            canvas.drawRect(lastX + width, lastY, lastX + width * 2f, lastY + width, nextMovePaint)

        if (left == 0 || (left == playerTurn && jumpLeft == 0))
            canvas.drawRect(lastX - width, lastY, lastX, lastY + width, nextMovePaint)

        if (top == 0 || (top == playerTurn && jumpTop == 0))
            canvas.drawRect(lastX, lastY - width, lastX + width, lastY, nextMovePaint)

        if (bottom == 0 || (bottom == playerTurn && jumpBottom == 0))
            canvas.drawRect(lastX, lastY + width, lastX + width, lastY + width * 2f, nextMovePaint)

        if (topRight == 0 || (topRight == playerTurn && jumpTopRight == 0))
            canvas.drawRect(lastX + width, lastY - width, lastX + width * 2f, lastY, nextMovePaint)

        if (bottomRight == 0 || (bottomRight == playerTurn && jumpBottomRight == 0))
            canvas.drawRect(lastX + width, lastY + width, lastX + width * 2f, lastY + width * 2f, nextMovePaint)

        if (topLeft == 0 || (topLeft == playerTurn && jumpTopLeft == 0))
            canvas.drawRect(lastX - width, lastY - width, lastX, lastY, nextMovePaint)

        if (bottomLeft == 0 || (bottomLeft == playerTurn && jumpBottomLeft == 0))
            canvas.drawRect(lastX - width, lastY + width, lastX, lastY + width * 2f, nextMovePaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}