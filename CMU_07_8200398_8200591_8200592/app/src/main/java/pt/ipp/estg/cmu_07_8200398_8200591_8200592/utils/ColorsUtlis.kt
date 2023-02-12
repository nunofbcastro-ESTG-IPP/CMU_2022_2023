package pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.godaddy.android.colorpicker.HsvColor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.RGBB

fun getIntFromColor(
    Red: Int,
    Green: Int,
    Blue: Int
): Int {
    var Red = Red
    var Green = Green
    var Blue = Blue

    Red = Red shl 16 and 0x00FF0000 //Shift red 16-bits and mask out other stuff
    Green = Green shl 8 and 0x0000FF00 //Shift Green 8-bits and mask out other stuff
    Blue = Blue and 0x000000FF //Mask out anything not blue.

    return -0x1000000 or Red or Green or Blue //0xFF000000 for 100% Alpha. Bitwise OR everything together.
}

fun getRGBBToColor(
    RGBB: RGBB,
): Color {

    val color = HsvColor.from(
        Color(
            getIntFromColor(
                Red = RGBB.Red,
                Green = RGBB.Green,
                Blue = RGBB.Blue,
            )
        )
    )

    return HsvColor(
        hue = color.hue,
        saturation = color.saturation,
        value = RGBB.Brightness / 100f,
        alpha = color.alpha
    ).toColor()
}

fun getHsvColorToRGBB(
    HsvColor : HsvColor
): RGBB{
    val myColor = HsvColor(
        hue = HsvColor.hue,
        saturation = HsvColor.saturation,
        value = 1f,
        alpha = HsvColor.alpha
    ).toColor().toArgb()

    return RGBB(
        Red = myColor.red,
        Green = myColor.green,
        Blue = myColor.blue,
        Brightness = (HsvColor.value * 100).toInt()
    )
}