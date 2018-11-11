package gemisis.mnist.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class EaselView : View {
    private val mPaintColor: Int = Color.WHITE
    private val mBrushSize: Float = 50.0f
    private val mDrawPaint: Paint = Paint()
    private val mPath: Path = Path()
    var mActionEventListeners: MutableList<ActionEventListener> = ArrayList()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        focusable = FOCUSABLE
        isFocusableInTouchMode = true
        setBackgroundColor(Color.BLACK)
        setupPaint()
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