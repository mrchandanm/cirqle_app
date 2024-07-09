package cirqle.com.customLayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CircularImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val clipPath = Path()
    private val rectF = RectF()

    override fun onDraw(canvas: Canvas) {
        canvas?.let {
            rectF.set(0f, 0f, width.toFloat(), height.toFloat())
            clipPath.reset()
            clipPath.addOval(rectF, Path.Direction.CW)
            canvas.clipPath(clipPath)
            super.onDraw(canvas)
        }
    }
}