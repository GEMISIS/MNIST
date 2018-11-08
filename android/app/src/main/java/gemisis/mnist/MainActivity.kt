package gemisis.mnist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import gemisis.mnist.views.EaselView

class MainActivity : AppCompatActivity() {
    var easel: EaselView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        easel = findViewById(R.id.easel_view)
    }

    fun reset(view: View) {
        easel?.reset()
    }
}
