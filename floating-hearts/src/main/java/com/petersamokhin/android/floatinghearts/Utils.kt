package com.petersamokhin.android.floatinghearts

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.*
import android.support.v4.content.ContextCompat
import android.widget.SeekBar
import java.net.URL

/**
 * Get bitmap from byte array
 */
fun ByteArray.toBitmap() = BitmapFactory.decodeByteArray(this, 0, size)

/**
 * Get inscribed circle from image rectangle
 */
fun Bitmap.circleCrop(ds: Int): Bitmap {
    val output = Bitmap.createBitmap(ds, ds, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint()
    val rect = Rect(0, 0, ds, ds)

    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    canvas.drawCircle(ds / 2f, ds / 2f, ds / 2f, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(Bitmap.createScaledBitmap(this, ds, ds, false), rect, rect, paint)

    return output
}

/**
 * Get drawable's bitmap
 */
fun Drawable.toBitmap(): Bitmap? {
    if (this is BitmapDrawable) {
        if (bitmap != null) {
            return bitmap
        }
    }

    val bm = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
    } else {
        Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    }

    val canvas = Canvas(bm)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)

    return bm
}

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun Context.drawable(id: Int): Drawable? = ContextCompat.getDrawable(this, id)

val Int.dp
    get() = (this * (Resources.getSystem()?.displayMetrics?.density ?: 0f)).toInt()

val Float.dp
    get() = (this * (Resources.getSystem()?.displayMetrics?.density ?: 0f))

/**
 * React on seekbar user input
 */
inline fun SeekBar.onUserProgressChange(crossinline block: (Int) -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                block(progress)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

        override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
    })
}