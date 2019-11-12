package util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat


object Util {

    fun getBitmap(context: Context, drawableRes: Int): Bitmap {
        val canvas = Canvas()
        val drawable = ContextCompat.getDrawable(context, drawableRes)
        val size = context.resources.displayMetrics.widthPixels / 8

        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)

        drawable!!.setBounds(0, 0, size, size)
        drawable.draw(canvas)

        return bitmap
    }

}