package com.inlacou.inkbetterandroidviews.dividers

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.inlacou.inkbetterandroidviews.R

class DottedDividerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    companion object {
        var ORIENTATION_HORIZONTAL = 0
        var ORIENTATION_VERTICAL = 1
    }

    private val mPaint: Paint
    private var orientation = 0
    override fun onDraw(canvas: Canvas) {
        if (orientation == ORIENTATION_HORIZONTAL) {
            val center = height * .5f
            canvas.drawLine(0f, center, width.toFloat(), center, mPaint)
        } else {
            val center = width * .5f
            canvas.drawLine(center, 0f, center, height.toFloat(), mPaint)
        }
    }

    init {
        val dashGap: Int
        val dashLength: Int
        val dashThickness: Int
        val color: Int
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.DottedDividerView, 0, 0)
        try {
            dashGap = a.getDimensionPixelSize(R.styleable.DottedDividerView_dashGap, 5)
            dashLength = a.getDimensionPixelSize(R.styleable.DottedDividerView_dashLength, 5)
            dashThickness = a.getDimensionPixelSize(R.styleable.DottedDividerView_dashThickness, 3)
            color = a.getColor(R.styleable.DottedDividerView_color, -0x1000000)
            orientation = a.getInt(R.styleable.DottedDividerView_dividerOrientation, ORIENTATION_HORIZONTAL)
        } finally {
            a.recycle()
        }
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = color
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = dashThickness.toFloat()
        mPaint.pathEffect = DashPathEffect(floatArrayOf(dashLength.toFloat(), dashGap.toFloat()), 0f)
    }
}