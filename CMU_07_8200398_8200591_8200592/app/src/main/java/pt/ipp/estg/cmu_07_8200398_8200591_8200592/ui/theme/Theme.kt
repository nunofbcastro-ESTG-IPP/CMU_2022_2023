package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme

import android.content.pm.ActivityInfo
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CMU_07_8200398_8200591_8200592Theme(
    content: @Composable () -> Unit
) {
    val colorPallet = MyPallettes()

    val colors = if (colorPallet) {
        DarkColorPalette
    } else {
        LightColorPalette
    }


    MaterialTheme(
        colors = colors.material,
        typography = Typography,
        shapes = Shapes,
        content = content
    )

    StatusBarLayout(
        barColor = colors.material.background,
        darkIcons = !colorPallet,
    )

    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
}