package com.petersamokhin.android.floatinghearts

import android.graphics.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.SparseArray
import android.view.*
import android.widget.FrameLayout
import org.rajawali3d.view.SurfaceView
import kotlin.concurrent.thread

class HeartsView : Fragment() {

    private val cache = SparseArray<Bitmap>()
    private val heartWidth = 50.dp

    private lateinit var emitter: HeartsRenderer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return (inflater.inflate(R.layout.hearts, container, false) as FrameLayout).also {
            val renderer = HeartsRenderer(context!!)

            it.addView(SurfaceView(context).apply {
                setFrameRate(60.0)
                setZOrderOnTop(true)
                setEGLConfigChooser(8, 8, 8, 8, 16, 0)
                holder.setFormat(PixelFormat.TRANSLUCENT)
                setTransparent(true)
                setSurfaceRenderer(renderer)
            })

            emitter = renderer
        }
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

                view?.post {
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