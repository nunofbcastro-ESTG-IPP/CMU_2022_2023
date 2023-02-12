package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun StatusBarLayout(
    barColor: Color,
    darkIcons: Boolean
){
    val systemUiController = rememberSystemUiController()
    SideEffect {
        // navigation bar
        //systemUiController.isNavigationBarVisible = false

        // status bar
        //systemUiController.isStatusBarVisible = false

        // system bars
        //systemUiController.isSystemBarsVisible = false

        systemUiController.setSystemBarsColor(
            color = barColor,
            darkIcons = darkIcons,
        )
    }
}