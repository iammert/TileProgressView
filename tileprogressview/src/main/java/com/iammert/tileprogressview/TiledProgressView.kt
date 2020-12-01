package com.iammert.tileprogressview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat

class TiledProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val viewRect: RectF = RectF()

    /**
     * Backgrounds
     */
    private val backgroundProgressRect = RectF()

    private var backgroundProgressRadius: Float = 0f

    private val backgroundProgressPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.white)
    }

    /**
     * Foregrounds
     */
    private val foregroundProgressRectMin = RectF()

    private val foregroundProgressRectMax = RectF()

    private val foregroundProgressRectCurrent = RectF()

    private var foregroundProgressRadius = 0f

    private val foregroundProgressPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.purple)
    }

    private var borderWidth = resources.getDimension(R.dimen.progress_border)

    private var tileParticleBitmap: Bitmap? = null

    private val tileShaderPaint = Paint(Paint.FILTER_BITMAP_FLAG)

    private var tileShader: Shader? = null

    private val tileShaderLocalMatrix = Matrix()

    private val tileShaderMatrixAnimator = ValueAnimator.ofFloat()

    private val progressAnimator = ValueAnimator.ofFloat()

    private var currentProgressValue = 0.0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewRect.set(0f, 0f, w.toFloat(), h.toFloat())
        initializeProgressRectFs()
        initializeTiledBitmapShader()
        initializeTileAnimator()
        initializeProgressAnimator()
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRoundRect(
            backgroundProgressRect,
            backgroundProgressRadius,
            backgroundProgressRadius,
            backgroundProgressPaint
        )

        canvas?.drawRoundRect(
            foregroundProgressRectCurrent,
            foregroundProgressRadius,
            foregroundProgressRadius,
            foregroundProgressPaint
        )
        canvas?.drawRoundRect(
            foregroundProgressRectCurrent,
            foregroundProgressRadius,
            foregroundProgressRadius,
            tileShaderPaint
        )
    }

    fun setProgress(@FloatRange(from = 0.0, to = 100.0) progressValue: Float) {
        progressAnimator.setFloatValues(currentProgressValue, progressValue)
        progressAnimator.start()
        currentProgressValue = progressValue
    }

    fun setLoadingColor(@ColorInt progressColor: Int) {
        foregroundProgressPaint.color = progressColor
        invalidate()
    }

    fun setLoadingColorRes(@ColorRes progressColorRes: Int) {
        foregroundProgressPaint.color = ContextCompat.getColor(context, progressColorRes)
        invalidate()
    }

    fun setColor(@ColorInt color: Int) {
        backgroundProgressPaint.color = color
        invalidate()
    }

    fun setColorRes(@ColorRes colorRes: Int) {
        backgroundProgressPaint.color = ContextCompat.getColor(context, colorRes)
        invalidate()
    }

    private fun initializeProgressRectFs() {
        if (viewRect.isEmpty) return
        backgroundProgressRect.set(viewRect)
        backgroundProgressRadius = backgroundProgressRect.height() / 2f

        foregroundProgressRectMax.set(
            backgroundProgressRect.left + borderWidth,
            backgroundProgressRect.top + borderWidth,
            backgroundProgressRect.right - borderWidth,
            backgroundProgressRect.bottom - borderWidth
        )
        foregroundProgressRadius = foregroundProgressRectMax.height() / 2f
        foregroundProgressRectMin.set(
            backgroundProgressRect.left + borderWidth,
            backgroundProgressRect.top + borderWidth,
            backgroundProgressRect.left + borderWidth + 2 * foregroundProgressRadius,
            backgroundProgressRect.bottom - borderWidth
        )
        foregroundProgressRectCurrent.set(foregroundProgressRectMin)
    }

    private fun initializeTiledBitmapShader() {
        if (viewRect.isEmpty) return

        val tileBitmap = BitmapFactory.decodeResource(resources, R.drawable.tile_progress)

        val matrix = Matrix()
        val scale = foregroundProgressRectCurrent.height() / tileBitmap.height
        matrix.setScale(scale, scale)

        tileParticleBitmap = Bitmap.createBitmap(
            tileBitmap,
            0,
            0,
            tileBitmap.width,
            tileBitmap.height,
            matrix,
            true
        )

        tileShader =
            BitmapShader(tileParticleBitmap!!, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        tileShaderLocalMatrix.setTranslate(0f, foregroundProgressRectMax.top)
        tileShader?.setLocalMatrix(tileShaderLocalMatrix)
        tileShaderPaint.shader = tileShader
    }

    private fun initializeTileAnimator() {
        val maxTranslate = tileParticleBitmap?.width?.toFloat() ?: 0f
        tileShaderMatrixAnimator.setFloatValues(0f, maxTranslate)
        tileShaderMatrixAnimator.duration = 500
        tileShaderMatrixAnimator.interpolator = LinearInterpolator()
        tileShaderMatrixAnimator.addUpdateListener {
            tileShaderLocalMatrix.setTranslate(
                it.animatedValue as Float,
                foregroundProgressRectMax.top
            )
            tileShader?.setLocalMatrix(tileShaderLocalMatrix)
            tileShaderPaint.shader = tileShader
            invalidate()
        }
        tileShaderMatrixAnimator.addListener(onEnd = {
            tileShaderMatrixAnimator.start()
        })
        tileShaderMatrixAnimator.start()
    }

    private fun initializeProgressAnimator() {
        progressAnimator.duration = 300
        progressAnimator.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            val min = foregroundProgressRectMin.width()
            val max = foregroundProgressRectMax.width()
            val maxProgress = max - min
            val currentProgress = maxProgress * animatedValue / 100f
            foregroundProgressRectCurrent.right =
                foregroundProgressRectCurrent.left +
                        (foregroundProgressRadius * 2f) +
                        currentProgress
            invalidate()
        }
        tileShaderMatrixAnimator.start()
    }
}