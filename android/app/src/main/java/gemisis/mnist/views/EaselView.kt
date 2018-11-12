package gemisis.mnist.views

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import gemisis.mnist.R

class EaselView : View {
    private val mPaintColor: Int = Color.WHITE
    private val mBrushSize: Float = 50.0f
    private val mDrawPaint: Paint = Paint()
    private val mPath: Path = Path()
    private var mAspectRatio: Int = 0
    var mActionEventListeners: MutableList<ActionEventListener> = ArrayList()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        focusable = FOCUSABLE
        isFocusableInTouchMode = true
        setBackgroundColor(Color.BLACK)
        setupPaint()

        context.theme.obtainStyledAttributes(attrs, R.styleable.EaselView, 0, 0).apply {
            try {
                mAspectRatio = getInt(R.styleable.EaselView_aspect_ratio, 0)
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mAspectRatio == 0) {
            return super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
        var originalWidth = MeasureSpec.getSize(widthMeasureSpec)
        var originalHeight = MeasureSpec.getSize(heightMeasureSpec)
        var finalWidth = originalWidth
        var finalHeight = originalHeight
        var displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        when (mAspectRatio) {
            0 -> {
                // Do nothing by default.
            }
            1 -> {
                // Square aspect ratio.
                if (originalHeight > displayMetrics.widthPixels) {
                    finalHeight = finalWidth
                } else {
                    finalWidth = finalHeight
                }
            }
            2 -> {
                // Landscape aspect ratio.
                var calculatedHeight = originalWidth * 9 / 16
                if (calculatedHeight > originalHeight) {
                    finalWidth = originalHeight * 16 / 9
                    finalHeight = originalHeight
                } else {
                    finalWidth = originalWidth
                    finalHeight = calculatedHeight
                }
            }
            3 -> {
                // Portrait aspect ratio.
                var calculatedHeight = originalWidth * 16 / 9
                if (calculatedHeight > originalHeight) {
                    finalWidth = originalHeight * 9 / 16
                    finalHeight = originalHeight
                } else {
                    finalWidth = originalWidth
                    finalHeight = calculatedHeight
                }
            }
        }
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY))
    }

    private fun setupPaint() {
        mDrawPaint.color = mPaintColor
        mDrawPaint.isAntiAlias = true
        mDrawPaint.strokeWidth = mBrushSize
        mDrawPaint.style = Paint.Style.STROKE
        mDrawPaint.strokeJoin = Paint.Join.ROUND
        mDrawPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(mPath, mDrawPaint)
    }

    val bitmap: Bitmap
        get() {
            var bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            var canvas = Canvas(bmp)
            layout(left, top, right, bottom)
            draw(canvas)
            return bmp
        }

    fun reset() {
        mPath.reset()
        postInvalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var touchX: Float = event?.x ?: 0.0f
        var touchY: Float = event?.y ?: 0.0f
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mPath.moveTo(touchX, touchY)
                for (eventListener: ActionEventListener? in mActionEventListeners) {
                    eventListener?.actionDown()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                mPath.lineTo(touchX, touchY)
                for (eventListener: ActionEventListener? in mActionEventListeners) {
                    eventListener?.actionMove()
                }
            }
            MotionEvent.ACTION_UP -> {
                for (eventListener: ActionEventListener? in mActionEventListeners) {
                    eventListener?.actionUp()
                }
            }
            else -> return false
        }
        postInvalidate()
        return true
    }

    interface ActionEventListener {
        fun actionDown()
        fun actionUp()
        fun actionMove()
    }
}