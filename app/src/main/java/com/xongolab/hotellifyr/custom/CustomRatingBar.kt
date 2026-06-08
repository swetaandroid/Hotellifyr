package com.xongolab.hotellifyr.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.xongolab.hotellifyr.R

class CustomRatingBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var rating = 0f
    private var maxRating = 5
    private var starSize = 20
    private var starSpacing = 0
    private var isRate = false
    private var filledStar: Drawable? = null
    private var emptyStar: Drawable? = null
    private var starClickListener: ((Float) -> Unit)? = null

    init {
        // Extract attributes
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomRatingBar)
            rating = typedArray.getFloat(R.styleable.CustomRatingBar_rating, rating)
            maxRating = typedArray.getInt(R.styleable.CustomRatingBar_maxRating, maxRating)
            starSize = typedArray.getDimensionPixelSize(R.styleable.CustomRatingBar_starSize, starSize)
            starSpacing = typedArray.getDimensionPixelSize(R.styleable.CustomRatingBar_starSpacing, starSpacing)
            isRate = typedArray.getBoolean(R.styleable.CustomRatingBar_rate, true)
            filledStar = typedArray.getDrawable(R.styleable.CustomRatingBar_filledStar)
            emptyStar = typedArray.getDrawable(R.styleable.CustomRatingBar_emptyStar)
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = starSize * maxRating + starSpacing * (maxRating - 1)
        val height = starSize
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var currentX = 0

        for (i in 0 until maxRating) {
            val drawable = if (i < rating) filledStar else emptyStar
            drawable?.let {
                it.setBounds(currentX, 0, currentX + starSize, starSize)
                it.draw(canvas)
            }
            currentX += starSize + starSpacing
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isRate) {
            val touchX = event.x
            var newRating = 0f
            var currentX = 0

            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                    for (i in 0 until maxRating) {
                        currentX += starSize + starSpacing
                        if (touchX <= currentX) {
                            newRating = (i + 1).toFloat()
                            break
                        }
                    }

                    if (newRating != rating) {
                        rating = newRating
                        invalidate()
                        starClickListener?.invoke(rating)
                    }
                }
            }
        }
        return true
    }


    fun setOnRatingClickListener(listener: (Float) -> Unit) {
        starClickListener = listener
    }
}
