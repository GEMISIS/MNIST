package gemisis.mnist.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class EaselView : View {
    private val paintColor: Int = Color.BLACK
    private val brushSize: Float = 50.0f
    private val drawPaint: Paint = Paint()
    private val path: Path = Path()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        focusable = FOCUSABLE
        isFocusableInTouchMode = true
        setupPaint()
    }

    private fun setupPaint() {
        drawPaint.color = paintColor
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = brushSize
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(path, drawPaint)
    }

    fun reset() {
        path.reset()
        postInvalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var touchX: Float = event!!.x
        var touchY: Float = event!!.y
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> path.moveTo(touchX, touchY)
            MotionEvent.ACTION_MOVE -> path.lineTo(touchX, touchY)
            else -> return false
        }
        postInvalidate()
        return true
    }
}