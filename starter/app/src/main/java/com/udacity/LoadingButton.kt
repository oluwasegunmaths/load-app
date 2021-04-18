package com.udacity

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var loadingBackgroundColor: Int = 0
    private var loadingTextColor: Int = 0
    private var loadingPrimaryDarkColor: Int = 0
    private var loadingAccentColor: Int = 0
    private var downloadString: String
    private var weAreLoadingString: String
    var isLoading: Boolean = false

    private var arcAngle: Float = 0f
        set(value) {
            field = value
        }

    private var widthSize: Int = 0
        set(value) {
            field = value
            //forced a call to onDraw
            invalidate()
        }

    private var heightSize = 0

    private val textWidth: Float by lazy { paint.measureText("We are loading") }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        // Paint styles used for rendering are initialized here. This
        // is a performance optimization, since onDraw() is called
        // for every screen refresh.
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.default_text_size)
    }

    init {

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            loadingBackgroundColor = getColor(R.styleable.LoadingButton_loadingBackgroundColor, 0)
            loadingTextColor = getColor(R.styleable.LoadingButton_loadingTextColor, 0)
        }
        loadingPrimaryDarkColor = context.getColor(R.color.colorPrimaryDark)
        loadingAccentColor = context.getColor(R.color.colorAccent)
        downloadString = context.getString(R.string.download)
        weAreLoadingString = context.getString(R.string.we_are_loading)

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isLoading) {
            paint.color = loadingPrimaryDarkColor

            canvas?.drawRect(0f, 0f, widthSize.toFloat(), (heightSize).toFloat(), paint)
            paint.color = loadingBackgroundColor

            canvas?.drawRect(widthSize.toFloat(), 0f, (width).toFloat(), (height).toFloat(), paint)
            paint.color = loadingTextColor

            canvas?.drawText(
                weAreLoadingString,
                width.toFloat() / 2,
                height.toFloat() / 2 + paint.textSize / 2,
                paint
            )
            paint.color = loadingAccentColor

            canvas?.drawArc(
                width / 2 + textWidth / 2.toFloat(),
                (height - paint.textSize) / 2,
                width / 2 + textWidth / 2 + paint.textSize,
                (height - paint.textSize) / 2 + (paint.textSize),
                0f,
                arcAngle,
                true,
                paint
            )

        } else {
            paint.color = loadingBackgroundColor

            canvas?.drawRect(0f, 0f, (width).toFloat(), (height).toFloat(), paint)
            paint.color = loadingTextColor

            canvas?.drawText(
                downloadString,
                width.toFloat() / 2,
                height.toFloat() / 2 + paint.textSize / 2,
                paint
            )

        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}