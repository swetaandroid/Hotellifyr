package com.xongolab.hotellifyr.custom

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import com.xongolab.hotellifyr.R

class ReadMoreTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var readMoreMaxLine = DEFAULT_MAX_LINE
    private var readMoreText = context.getString(R.string.read_more)
    private var readLessText = context.getString(R.string.read_less)
    private var readMoreColor = ContextCompat.getColor(context, R.color.colorSecondary)

    private var state: State = State.COLLAPSED
        private set(value) {
            field = value
            updateText()
            changeListener?.onStateChange(value)
        }

    private val isExpanded get() = state == State.EXPANDED
    private val isCollapsed get() = state == State.COLLAPSED

    private var originalText: CharSequence = ""
    private var changeListener: ChangeListener? = null

    init {
        setupAttributes(context, attrs, defStyleAttr)
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }

    private fun setupAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView, defStyleAttr, 0)
        readMoreMaxLine =
            typedArray.getInt(R.styleable.ReadMoreTextView_readMoreMaxLine, DEFAULT_MAX_LINE)
        readMoreText =
            typedArray.getString(R.styleable.ReadMoreTextView_readMoreText) ?: readMoreText
        readLessText =
            typedArray.getString(R.styleable.ReadMoreTextView_readLessText) ?: readLessText
        readMoreColor =
            typedArray.getColor(R.styleable.ReadMoreTextView_readMoreColor, readMoreColor)
        typedArray.recycle()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        originalText = text ?: ""
        super.setText(originalText, type)
        doOnLayout {
            post { updateText() }
        }
    }

    private fun updateText() {
        if (layout == null || originalText.isEmpty()) return

        if (isExpanded) {
            val spannable = SpannableStringBuilder().apply {
                append(originalText)
                append(" ")
                val start = length
                append(readLessText)
                setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        collapse()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }
                }, start, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                setSpan(
                    ForegroundColorSpan(readMoreColor),
                    start,
                    length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            super.setText(spannable, BufferType.SPANNABLE)
        } else {
            if (layout.lineCount <= readMoreMaxLine) {
                super.setText(originalText, BufferType.NORMAL)
                return
            }

            val adjustCutCount = getAdjustCutCount(readMoreMaxLine, readMoreText)
            val maxTextIndex = layout.getLineVisibleEnd(readMoreMaxLine - 1)
            val originalSubText = originalText.substring(0, maxTextIndex - 1 - adjustCutCount)

            val spannable = SpannableStringBuilder().apply {
                append(originalSubText)
                append(" ")
                val start = length
                append(readMoreText)
                setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        expand()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }
                }, start, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                setSpan(
                    ForegroundColorSpan(readMoreColor),
                    start,
                    length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            super.setText(spannable, BufferType.SPANNABLE)
        }
    }

    private fun getAdjustCutCount(maxLine: Int, readMoreText: String): Int {
        val lastLineStartIndex = layout.getLineVisibleEnd(maxLine - 2) + 1
        val lastLineEndIndex = layout.getLineVisibleEnd(maxLine - 1)
        val lastLineText = text.substring(lastLineStartIndex, lastLineEndIndex)

        val bounds = Rect()
        paint.getTextBounds(lastLineText, 0, lastLineText.length, bounds)

        var adjustCutCount = -1
        do {
            adjustCutCount++
            val subText = lastLineText.substring(0, lastLineText.length - adjustCutCount)
            val replacedText = subText + readMoreText
            paint.getTextBounds(replacedText, 0, replacedText.length, bounds)
        } while (bounds.width() > width)

        return adjustCutCount
    }

    fun expand() {
        if (!isExpanded) state = State.EXPANDED
    }

    fun collapse() {
        if (!isCollapsed) state = State.COLLAPSED
    }

    override fun setOnClickListener(l: OnClickListener?) {
        throw UnsupportedOperationException("Use read more/less span to toggle")
    }

    fun setChangeListener(listener: ChangeListener) {
        this.changeListener = listener
    }

    interface ChangeListener {
        fun onStateChange(state: State)
    }

    enum class State {
        COLLAPSED, EXPANDED
    }

    companion object {
        private const val DEFAULT_MAX_LINE = 4
    }
}
