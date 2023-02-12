package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun ShimmerBrush() : Brush {
    //These colors will be used on the brush. The lightest color should be in the middle

    val gradient = listOf(
        Color.LightGray.copy(alpha = 0.9f), //darker grey (90% opacity)
        Color.LightGray.copy(alpha = 0.3f), //lighter grey (30% opacity)
        Color.LightGray.copy(alpha = 0.9f)
    )

    val transition = rememberInfiniteTransition() // animate infinite times

    val translateAnimation = transition.animateFloat( //animate the transition
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000, // duration for the animation
                easing = FastOutLinearInEasing
            )
        )
    )

    return Brush.linearGradient(
        colors = gradient,
        start = Offset(200f, 200f),
        end = Offset(
            x = translateAnimation.value,
            y = translateAnimation.value
        )
    )
}

val BackgroundWhiteMode = Color(0xFFFFFFFF)
val BackgroundDarkMode = Color(0xFF121212)
val OnBackgroundWhiteMode = Color(0xFF000000)
val OnBackgroundDarkMode = Color(0xFFFFFFFF)
val Error = Color(0xFFA30808)
val OnError = Color(0xFFFFFFFF)
val BackgroundSecondary = Brush.verticalGradient(listOf(Color(0xFFCD4FE0), Color(0xFFB044E4), Color(0xFF8836E7)))
val white = Color.White
val Button = Brush.horizontalGradient(listOf(Color(0xFFF29365), Color(0xFFDC698D), Color(0xFFD447AB)))
val SubtitleDarkMode = Color.LightGray
val SubtitleWhiteMode = Color.Gray
val SubtitleBackground = Color.LightGray
val UnreadBubbleColor = Color(0xFFE70606)
val TextFieldBackground = Color.White
val OnTextFieldBackground = Color.Black
val TextFieldUnFocusBorder = Color.LightGray
val TextFieldFocusBorder = Color.LightGray
val TextFieldCursor = Color.Black
val TextFieldPlaceholder = Color.Gray
val TextFieldIcon = Color.LightGray
val Primary = Color(0xFFB044E4)
val OnPrimary = Color.White
val PrimaryVariant = Color(0xFF7F0694)
val Secondary = Color(0xFFFF8205)
val OnSecondary = Color.White