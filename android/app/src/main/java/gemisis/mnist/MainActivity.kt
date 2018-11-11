package gemisis.mnist

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import gemisis.mnist.views.EaselView
import java.util.*

class MainActivity : AppCompatActivity(), EaselView.ActionEventListener {
    var mEasel: EaselView? = null
    var mPredictor: Predictor? = null
    var mPredictionLabel: TextView? = null
    var mConfidenceLabel: TextView? = null
    var mSubmitTimer: Timer = Timer()
    var mLastImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEasel = findViewById(R.id.easel_view)
        mEasel?.mActionEventListeners?.add(this)

        mPredictionLabel = findViewById(R.id.prediction_label)
        mConfidenceLabel = findViewById(R.id.confidence_label)

        mPredictor = Predictor(R.raw.model, this)
    }

    fun predict() {
        mLastImage = mEasel?.bitmap
        var labelProbArray: Array<FloatArray> = Array(1, {j -> FloatArray(10, {i -> 0.0f})})
        var confidence = mPredictor?.predict(mLastImage, labelProbArray)!!
        var result = labelProbArray[0].indexOf(confidence)
        runOnUiThread(object : Runnable {
            override fun run() {
                mPredictionLabel?.text = result.toString()
                mConfidenceLabel?.text = Math.round(confidence * 100).toString()
            }
        })
        Log.i("GTEST", "Value: $result; Confidence: $confidence;")
        mEasel?.reset()
    }

    override fun actionDown() {
        mSubmitTimer.cancel()
    }

    override fun actionUp() {
        mSubmitTimer = Timer()
        mSubmitTimer.schedule(object : TimerTask() {
            override fun run() {
                predict()
            }
        }, 250)
    }

    override fun actionMove() {
    }
}
