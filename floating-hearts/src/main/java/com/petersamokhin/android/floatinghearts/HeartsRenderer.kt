package com.petersamokhin.android.floatinghearts

import android.content.Context
import android.graphics.Bitmap
import android.util.*
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import org.rajawali3d.animation.*
import org.rajawali3d.curves.CubicBezierCurve3D
import org.rajawali3d.materials.Material
import org.rajawali3d.materials.methods.DiffuseMethod.Lambert
import org.rajawali3d.materials.textures.ATexture.TextureException
import org.rajawali3d.materials.textures.Texture
import org.rajawali3d.math.vector.Vector3
import org.rajawali3d.primitives.PointSprite
import org.rajawali3d.renderer.Renderer
import java.util.Random

/**
 * Rajawali renderer wrapper.
 *
 * @author PeterSamokhin, https://petersamokhin.com/
 */
class HeartsRenderer(context: Context) : Renderer(context) {

    private var config = DEFAULT_CONFIG

    private val cache = SparseArray<Material>()
    private val random = Random()

    init {
        mContext = context
        frameRate = 60.0
    }

    fun emitHeart(bitmap: Bitmap, width: Int, height: Int, id: Int, yMax: Float) {
        val mat = cache[id] ?: initMaterial(bitmap, id)
        val randF = random.nextFloat() * 0.3f + 0.7f
        val randSign = if (random.nextBoolean()) 1 else -1

        val cubicBezierCurve3D = CubicBezierCurve3D(
            Vector3(0.0, -1.5, 0.0),
            Vector3((-randSign * config.xMax * randF / 6.0), (yMax / 3.0 * randF), 0.0),
            Vector3((randSign * config.xMax * randF / 3.0), (yMax * randF / 1.5), 0.0),
            Vector3((-randSign * config.xMax * randF / 5.0), (yMax * randF / 1.0), 0.0)
        )
        val w = randF * 0.3f * config.sizeCoeff

        val dimenW: Float
        val dimenH: Float
        if (width >= height) {
            dimenW = w
            dimenH = height.toFloat() / width.toFloat() * dimenW
        } else {
            dimenH = w
            dimenW = width.toFloat() / height.toFloat() * dimenH
        }

        val pointSprite = PointSprite(dimenW, dimenH).apply {
            scaleX = 1.0
            scaleY = 1.0
            material = mat
            isTransparent = true
        }

        currentScene.addChild(pointSprite)

        AnimationGroup().apply {
            addAnimation(ScaleAnimation3D(Vector3(1.0, 1.0, 1.0)).apply {
                durationMilliseconds = (600.0f * randF).toLong()
                transformable3D = pointSprite
            })
            addAnimation(SplineTranslateAnimation3D(cubicBezierCurve3D).apply {
                durationMilliseconds = (randF * 5000.0f * config.floatingTimeCoeff).toLong()
                transformable3D = pointSprite
                interpolator = DecelerateInterpolator()
                registerListener(object : IAnimationListener {
                    override fun onAnimationEnd(animation: Animation) {
                        currentScene.removeChild(pointSprite)
                        animation.unregisterListener(this)
                    }

                    override fun onAnimationRepeat(animation: Animation) = Unit

                    override fun onAnimationStart(animation: Animation) = Unit

                    override fun onAnimationUpdate(animation: Animation, interpolatedTime: Double) = Unit
                })
            })
            addAnimation(ScaleAnimation3D(Vector3(0.0, 0.0, 0.0)).apply {
                delayMilliseconds = ((5000.0f * randF * config.floatingTimeCoeff).toInt() - 800).toLong()
                durationMilliseconds = 300
                transformable3D = pointSprite
            })
            currentScene.registerAnimation(this)
        }.play()
    }

    fun applyConfig(newConfig: Config) { config = newConfig }

    fun getConfig() = config

    private fun initMaterial(bitmap: Bitmap, id: Int): Material {
        val mat = Material().apply {
            diffuseMethod = Lambert()
            color = 0
        }
        try {
            mat.addTexture(Texture("Earth$id", Bitmap.createBitmap(bitmap)))
        } catch (e: TextureException) {
            Log.e(TAG, e.toString())
        }

        cache.put(id, mat)
        return mat
    }

    override fun initScene() { currentCamera.z = 4.2 }

    override fun onOffsetsChanged(xOffset: Float, yOffset: Float, xOffsetStep: Float, yOffsetStep: Float, xPixelOffset: Int, yPixelOffset: Int) = Unit

    override fun onTouchEvent(event: MotionEvent) = Unit

    /**
     * Scene configuration.
     *
     * @property xMax The max amplitude of the flight along the X axis. For 420dpi and 1920x1080 value 5 is optimal maximum.
     * @property floatingTimeCoeff Duration of the flying animation will be multiplied by this value.
     * @property sizeCoeff Heart size coefficient.
     */
    data class Config(
        val xMax: Float,
        val floatingTimeCoeff: Float,
        val sizeCoeff: Float
    )

    companion object {
        private const val TAG = "HEARTS_RENDERER"
        val DEFAULT_CONFIG = Config(5f, 2f, 1f)
    }
}

