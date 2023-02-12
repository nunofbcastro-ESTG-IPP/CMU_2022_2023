package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

val MaterialTheme.myCustomColors: MyColorPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current