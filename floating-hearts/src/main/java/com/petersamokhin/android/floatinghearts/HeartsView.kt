package com.petersamokhin.android.floatinghearts

import android.content.Context
import android.graphics.*
import android.util.*
import org.rajawali3d.view.SurfaceView
import kotlin.concurrent.thread

class HeartsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : SurfaceView(context, attrs) {

    private val cache = SparseArray<Bitmap>()
    private val heartWidth = 50.dp

    private val emitter = HeartsRenderer(context)

    init {
        setFrameRate(60.0)
        setZOrderOnTop(true)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        holder.setFormat(PixelFormat.TRANSLUCENT)
        setTransparent(true)
        setSurfaceRenderer(emitter)
    }

    @Synchronized
    fun emitHeart(heartModel: HeartModel, maxY: Float = MAX_Y_FULL) {
        val cached = cache[heartModel.id]

        if (cached != null) {
            val h = (cached.height.toFloat() / (cached.width.toFloat() / heartWidth.toFloat())).toInt()
            emitter.emitHeart(cached, heartWidth, h, heartModel.id, maxY)
        } else {
            thread {
                val height = (heartModel.bitmap.height.toFloat() / (heartModel.bitmap.width.toFloat() / heartWidth.toFloat())).toInt()
                val resultBitmap = Bitmap.createScaledBitmap(heartModel.bitmap, heartWidth, height, true)

                cache.put(heartModel.id, resultBitmap)

                post {
                    emitter.emitHeart(resultBitmap, heartWidth, height, heartModel.id, maxY)
                }
            }
        }
    }

    fun getConfig() = emitter.getConfig()

    fun applyConfig(config: HeartsRenderer.Config) = emitter.applyConfig(config)

    companion object {
        const val MAX_Y_FULL = 1.5f
    }
}