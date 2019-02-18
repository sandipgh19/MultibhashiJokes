package sandip.example.com.multibhashijokes.binding

import android.content.res.ColorStateList
import android.databinding.BindingAdapter
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.TextView
import sandip.example.com.multibhashijokes.R
import java.util.*

class BindingAdapters {

    @BindingAdapter(value = ["subString"], requireAll = true)
    fun setFirstCharacter(view: TextView, value: String?) {
        value?.let {
            view.text = value.substring(0,1)
        }

    }


    @BindingAdapter(value = ["randomTint"], requireAll = true)
    fun setRandomTint(view: View, otherView: View?) {
        val colorList = view.context.resources.getIntArray(R.array.color_scheme_200)
        val randomColor = colorList[Random().nextInt(colorList.size)]
        val color = ColorStateList.valueOf(randomColor)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.backgroundTintList = color
        } else
            view.setBackgroundResource(randomColor)

        if (view is TextView) {
            view.background.alpha = 80
            view.setTextColor(manipulateColor(color = randomColor, factor = 0.8f))
        }


        if (otherView != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                otherView.backgroundTintList = color
            } else
                view.setBackgroundResource(randomColor)
    }

    private fun manipulateColor(color: Int, factor: Float): Int {
        val a = Color.alpha(color)
        val r = Math.round(Color.red(color) * factor)
        val g = Math.round(Color.green(color) * factor)
        val b = Math.round(Color.blue(color) * factor)
        return Color.argb(
            a,
            Math.min(r, 255),
            Math.min(g, 255),
            Math.min(b, 255)
        )
    }
}