package com.rafael.sovai

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class ActivityLayout(context: Context, attrs: AttributeSet) : View(context, attrs) {

    val greenPaintStroke = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 10F
    }

    val rectangule = Rect().apply {
        set(210, 125, 250,175)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(rectangule, greenPaintStroke)
    }
}