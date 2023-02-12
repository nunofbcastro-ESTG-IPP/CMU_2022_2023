package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models

import androidx.compose.runtime.Composable

data class AppBarState(
    val content: @Composable (() -> Unit) = {},
    val topBar: Boolean = false,
    val transparentBackground: Boolean = false,
    val navigateToBackScreen: Boolean = false,
    val drawerMenu: Boolean = false
)