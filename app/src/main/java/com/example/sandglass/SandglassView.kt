package com.example.sandglass

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.abs

class SandglassView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    init {
        initAnimator()
    }

    private val paint by lazy {
        Paint().apply {
            strokeWidth = 10f
        }
    }

    private val sandGlassPath by lazy {
        Path().apply {
            moveTo(halfWidth, halfHeight)
            lineTo(TRANSLATE_FROM_START, height - TRANSLATE_FROM_START)
            lineTo(width - TRANSLATE_FROM_START, height - TRANSLATE_FROM_START)
            lineTo(halfWidth, halfHeight)
            lineTo(width - TRANSLATE_FROM_START, TRANSLATE_FROM_START)
            lineTo(TRANSLATE_FROM_START, TRANSLATE_FROM_START)
        }
    }

    private var delta = 0f
    private var halfHeight = 0f
    private var halfWidth = 0f
    private var animator: ValueAnimator? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        halfHeight = height / 2.0f
        halfWidth = width / 2.0f
        paint.color = Color.GRAY
        canvas.drawPath(sandGlassPath, paint)
        canvas.clipPath(sandGlassPath)
        paint.color = Color.YELLOW
        canvas.drawRect(
            TRANSLATE_FROM_START,
            TRANSLATE_FROM_START + delta,
            width - TRANSLATE_FROM_START,
            halfHeight,
            paint
        )
        canvas.drawRect(
            TRANSLATE_FROM_START,
            height - TRANSLATE_FROM_START - delta,
            width - TRANSLATE_FROM_START,
            height - TRANSLATE_FROM_START,
            paint
        )
    }

    override fun onSaveInstanceState(): Parcelable {
        return SavedState(super.onSaveInstanceState()).apply {
            currentPlayTime = animator!!.currentPlayTime
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            animator!!.currentPlayTime = state.currentPlayTime
            super.onRestoreInstanceState(state.superState)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun onDetachedFromWindow() {
        if (animator != null && animator!!.isRunning) {
            animator!!.cancel()
        }
        super.onDetachedFromWindow()
    }

    fun startAnimation() {
        if (animator != null && !animator!!.isRunning) {
            animator!!.start()
        }
    }

    private fun initAnimator() {
        animator = ValueAnimator.ofFloat(0.0f, 1.0f).apply {
            duration = DEFAULT_DURATION
            interpolator = LinearInterpolator()
            addUpdateListener {
                delta = abs(halfHeight - TRANSLATE_FROM_START) * (it.animatedValue as Float)
                invalidate()
            }
        }
    }

    companion object {
        private const val TRANSLATE_FROM_START = 150f
        private const val DEFAULT_DURATION = 5000L
    }
}