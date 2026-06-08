package com.xongolab.hotellifyr.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.constraintlayout.widget.ConstraintLayout
import com.xongolab.hotellifyr.R

open class RoundableLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    /** corner radius */
    private var cornerLeftTop: Float = 0F
        set(value) {
            field = value
            postInvalidate()
        }

    private var cornerRightTop: Float = 0F
        set(value) {
            field = value
            postInvalidate()
        }

    var cornerLeftBottom: Float = 0F
        set(value) {
            field = value
            postInvalidate()
        }

    var cornerRightBottom: Float = 0F
        set(value) {
            field = value
            postInvalidate()
        }

    /** side option means top and bottom corner */
    private var cornerLeftSide: Float = 0F
        set(value) {
            field = value
            if (field != 0F) {
                cornerLeftTop = field
                cornerLeftBottom = field
            }
            postInvalidate()
        }

    private var cornerRightSide: Float = 0F
        set(value) {
            field = value
            if (field != 0F) {
                cornerRightTop = field
                cornerRightBottom = field
            }
            postInvalidate()
        }

    private var cornerAll: Float = 0F
        set(value) {
            field = value
            if (field != 0F) {
                cornerLeftSide = field
                cornerRightSide = field
            }
            postInvalidate()
        }

    /** background color */
    private var backGroundColor: Int = Color.WHITE
        set(value) {
            field = value
            postInvalidate()
        }

    /** stroke & dash options */
    private var strokeLineWidth: Float = 0F
        set(value) {
            field = value
            postInvalidate()
        }

    private var strokeLineColor: Int = Color.WHITE
        set(value) {
            field = value
            postInvalidate()
        }

    private var dashLineGap: Float = 0F
        set(value) {
            field = value
            postInvalidate()
        }

    private var dashLineWidth: Float = 0F
        set(value) {
            field = value
            postInvalidate()
        }

    init {
        render(attrs)
    }

    private fun render(attrs: AttributeSet?) {
        attrs?.let {
            with(context.obtainStyledAttributes(it, R.styleable.RoundableLayout)) {
                cornerLeftTop = getDimensionPixelSize(R.styleable.RoundableLayout_cornerLeftTop, 0).toFloat()
                cornerRightTop = getDimensionPixelSize(R.styleable.RoundableLayout_cornerRightTop, 0).toFloat()
                cornerLeftBottom = getDimensionPixelSize(R.styleable.RoundableLayout_cornerLeftBottom, 0).toFloat()
                cornerRightBottom = getDimensionPixelSize(R.styleable.RoundableLayout_cornerRightBottom, 0).toFloat()
                backGroundColor = getColor(R.styleable.RoundableLayout_backgroundColor, Color.WHITE)
                strokeLineWidth = getDimensionPixelSize(R.styleable.RoundableLayout_strokeLineWidth, 0).toFloat()
                strokeLineColor = getColor(R.styleable.RoundableLayout_strokeLineColor, Color.BLACK)
                dashLineWidth = getDimensionPixelSize(R.styleable.RoundableLayout_dashLineWidth, 0).toFloat()
                dashLineGap = getDimensionPixelSize(R.styleable.RoundableLayout_dashLineGap, 0).toFloat()
                cornerLeftSide = getDimensionPixelSize(R.styleable.RoundableLayout_cornerLeftSide, 0).toFloat()
                cornerRightSide = getDimensionPixelSize(R.styleable.RoundableLayout_cornerRightSide, 0).toFloat()
                cornerAll = getDimensionPixelSize(R.styleable.RoundableLayout_cornerAll, 0).toFloat()
                recycle()
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        val path = Path().apply {
            addRoundRect(
                RectF(0F, 0F, width.toFloat(), height.toFloat()),
                floatArrayOf(
                    cornerLeftTop, cornerLeftTop, cornerRightTop, cornerRightTop,
                    cornerRightBottom, cornerRightBottom, cornerLeftBottom, cornerLeftBottom
                ),
                Path.Direction.CW
            )
        }

        canvas.clipPath(path)

        with(GradientDrawable()) {
            cornerRadii = floatArrayOf(
                cornerLeftTop, cornerLeftTop, cornerRightTop, cornerRightTop,
                cornerRightBottom, cornerRightBottom, cornerLeftBottom, cornerLeftBottom
            )
            if (strokeLineWidth != 0F) {
                setStroke(strokeLineWidth.toInt(), strokeLineColor, dashLineWidth, dashLineGap)
            }
            setColor(backGroundColor)
            background = this
        }

        outlineProvider = createOutlineProvider(path)
        clipChildren = false

        super.dispatchDraw(canvas)
    }

    private fun createOutlineProvider(path: Path) = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setConvexPath(path)
        }
    }

    override fun setOutlineProvider(provider: ViewOutlineProvider?) {
        super.setOutlineProvider(provider)
    }

    override fun setElevation(elevation: Float) {
        super.setElevation(elevation)
    }

    override fun setTranslationZ(translationZ: Float) {
        super.setTranslationZ(translationZ)
    }
}
