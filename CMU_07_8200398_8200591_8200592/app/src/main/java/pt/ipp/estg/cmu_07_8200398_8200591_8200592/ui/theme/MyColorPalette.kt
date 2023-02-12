package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Stable
class MyColorPalette(
    val material: Colors,
    val backgroundSecondary: Brush,
    val onBackgroundSecondary: Color,
    val button: Brush,
    val onButton: Color,
    val subtitle: Color,
    val subtitleBackground: Color,
    val unreadBubbleColor: Color,
    val textFieldBackground: Color,
    val onTextFieldBackground: Color,
    val textFieldUnFocusBorder: Color,
    val textFieldFocusBorder: Color,
    val textFieldCursor: Color,
    val textFieldPlaceholder: Color,
    val textFieldIcon: Color,
    val shimmerBrush: @Composable ()-> Brush,
)