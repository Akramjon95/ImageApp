package maxcoders.uz.imageapp


import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var generate: TextView? = null
    private var textSize: TextView? = null
    private var spinner1: Spinner? = null
    private var spinner2: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iv_FloodFillActivity_image.setVisibility(View.GONE)
        iv_FloodFillActivity_image2.setVisibility(View.GONE)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.algorithm,
            R.layout.support_simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner1?.setAdapter(adapter)
        spinner2?.setAdapter(adapter)
        generate?.setOnClickListener(View.OnClickListener {
            iv_FloodFillActivity_image.setVisibility(View.VISIBLE)
            iv_FloodFillActivity_image2.setVisibility(View.VISIBLE)
        })
        iv_FloodFillActivity_image.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                floodColor(event.x, event.y)
            }
            true
        })
        iv_FloodFillActivity_image2.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                floodColorSecond(event.x, event.y)
            }
            true
        })
        textSize?.setOnClickListener(View.OnClickListener { showCustomDialog() })

    }

    private fun showCustomDialog() {
        val dialog = AlertDialog.Builder(this@MainActivity)
        val inflater = layoutInflater
        val view1 = inflater.inflate(R.layout.custom_dialog, null)
        dialog.setView(view1)
            .setNegativeButton(
                "Cancel"
            ) { dialoginterface, i -> dialoginterface.dismiss() }
            .setPositiveButton(
                "Ok"
            ) { dialoginterface, i -> dialoginterface.dismiss() }.show()
    }

    private fun floodColor(x: Float, y: Float) {
        val p1 = Point()
        p1.x = x.toInt() // X and y are co - ordinates when user clicks on the screen
        p1.y = y.toInt()
        var bitmap = getBitmapFromVectorDrawable(iv_FloodFillActivity_image!!.drawable)
        //bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        val pixel = bitmap!!.getPixel(x.toInt(), y.toInt())
        val sourceColorRGB = Color.red(pixel)
        //                Color.green(pixel),
//                Color.blue(pixel)
//        };
        val targetColor = Color.parseColor("#FF2200")
        //        int toleranceHex = Color.parseColor("#545454");
        val queueLinearFloodFiller = QueueLinearFloodFiller(bitmap, sourceColorRGB, targetColor)
        val toleranceHex = Color.parseColor("#545454")
        val toleranceRGB = Color.green(toleranceHex)
//            intArrayOf(
//            Color.red(toleranceHex),
//            Color.green(toleranceHex),
//            Color.blue(toleranceHex)
//        )
        queueLinearFloodFiller.setTolerance(toleranceRGB)
        queueLinearFloodFiller.setFillColor(targetColor)
        queueLinearFloodFiller.setTargetColor(sourceColorRGB)
        queueLinearFloodFiller.floodFill(p1.x, p1.y)
        bitmap = queueLinearFloodFiller.getImage()
        iv_FloodFillActivity_image!!.setImageBitmap(bitmap)
    }

    private fun floodColorSecond(x: Float, y: Float) {
        val p1 = Point()
        p1.x = x.toInt() // X and y are co - ordinates when user clicks on the screen
        p1.y = y.toInt()
        var bitmap = getBitmapFromVectorDrawable(iv_FloodFillActivity_image2!!.drawable)
        //bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        val pixel = bitmap!!.getPixel(x.toInt(), y.toInt())
        val sourceColorRGB = Color.red(pixel)
        //                Color.green(pixel),
//                Color.blue(pixel)
//        };
        val targetColor = Color.parseColor("#FF2200")

        val queueLinearFloodFiller = QueueLinearFloodFiller(bitmap, sourceColorRGB, targetColor)
        val toleranceHex = Color.parseColor("#545454")
        val toleranceRGB = Color.green(toleranceHex)
//            intArrayOf(
//            Color.red(toleranceHex),
//            Color.green(toleranceHex),
//            Color.blue(toleranceHex)
//        )
        queueLinearFloodFiller.setTolerance(toleranceRGB)
        queueLinearFloodFiller.setFillColor(targetColor)
        queueLinearFloodFiller.setTargetColor(sourceColorRGB)
        queueLinearFloodFiller.floodFill(p1.x, p1.y)
        bitmap = queueLinearFloodFiller.getImage()
        iv_FloodFillActivity_image2!!.setImageBitmap(bitmap)
    }

    private fun getBitmapFromVectorDrawable(drawable: Drawable): Bitmap? {
        return try {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: OutOfMemoryError) {
            null
        }
    }
}