package gemisis.mnist

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.channels.FileChannel

class Predictor {
    private var mInterpreter: Interpreter? = null
    private var mInputWidth: Int = 28
    private var mInputHeight: Int = 28
    private var mInputPxSize: Int = 1

    constructor(modelFilePath: String) {
        mInterpreter = Interpreter(File(modelFilePath))
    }

    constructor(modelID: Int, activity: Activity) {
        val fileDescriptor = activity.resources.openRawResourceFd(modelID)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        mInterpreter = Interpreter(fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength))
    }

    fun predict(bmp: Bitmap?, labelProbArray: Array<FloatArray>): Float {
        mInterpreter?.run(bmpToBB(Bitmap.createScaledBitmap(bmp, mInputWidth, mInputHeight, false)), labelProbArray)
        return (labelProbArray[0].max() ?: -1.0f)
    }

    private fun bmpToBB(bmp: Bitmap?) : ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * mInputWidth * mInputHeight * mInputPxSize)
        byteBuffer.order(ByteOrder.nativeOrder())
        val floatBuffer = byteBuffer.asFloatBuffer()

        val intValues = IntArray(mInputWidth * mInputHeight)
        bmp?.getPixels(intValues, 0, bmp.width, 0, 0, bmp.width, bmp.height)
        var pixel = 0
        for (y: Int in 0..mInputHeight-1) {
            for(x: Int in 0..mInputWidth-1) {
                val p: Int = intValues[pixel++]
                // byteBuffer.put(((p shr 16) and 0xFF).toByte())
                // byteBuffer.put(((p shr 8) and 0xFF).toByte())
                var c = ((p and 0xFF).toFloat() / 255.0f)
                floatBuffer.put(c)
            }
        }
        return byteBuffer
    }
}