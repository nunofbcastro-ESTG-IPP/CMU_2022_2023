package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.preferences.ThemeColors
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SettingsPreferencesViewModel

val LocalColors = staticCompositionLocalOf { LightColorPalette }

val DarkColorPalette = MyColorPalette(
    material = darkColors(
        primary = Primary,
        onPrimary = OnPrimary,
        primaryVariant = PrimaryVariant,
        secondary = Secondary,
        onSecondary= OnSecondary,
        background = BackgroundDarkMode,
        onBackground = OnBackgroundDarkMode,
        error = Error,
        onError = OnError
    ),
    backgroundSecondary = BackgroundSecondary,
    onBackgroundSecondary = white,
    button = Button,
    onButton = white,
    subtitle = SubtitleDarkMode,
    subtitleBackground = SubtitleBackground,
    unreadBubbleColor = UnreadBubbleColor,
    textFieldBackground = TextFieldBackground,
    onTextFieldBackground = OnTextFieldBackground,
    textFieldUnFocusBorder = TextFieldUnFocusBorder,
    textFieldFocusBorder = TextFieldFocusBorder,
    textFieldCursor = TextFieldCursor,
    textFieldPlaceholder = TextFieldPlaceholder,
    textFieldIcon = TextFieldIcon,
    shimmerBrush = {
        ShimmerBrush()
    },
)


val LightColorPalette = MyColorPalette(
    material = lightColors(
        primary = Primary,
        onPrimary = OnPrimary,
        primaryVariant = PrimaryVariant,
        secondary = Secondary,
        onSecondary= OnSecondary,
        background = BackgroundWhiteMode,
        onBackground = OnBackgroundWhiteMode,
        error = Error,
        onError = OnError
    ),
    backgroundSecondary = BackgroundSecondary,
    onBackgroundSecondary = white,
    button = Button,
    onButton = white,
    subtitle = SubtitleWhiteMode,
    subtitleBackground = SubtitleBackground,
    unreadBubbleColor = UnreadBubbleColor,
    textFieldBackground = TextFieldBackground,
    onTextFieldBackground = OnTextFieldBackground,
    textFieldUnFocusBorder = TextFieldUnFocusBorder,
    textFieldFocusBorder = TextFieldFocusBorder,
    textFieldCursor = TextFieldCursor,
    textFieldPlaceholder = TextFieldPlaceholder,
    textFieldIcon = TextFieldIcon,
    shimmerBrush = {
        ShimmerBrush()
    },
)

@Composable
fun MyPallettes() : Boolean{
    val theme : SettingsPreferencesViewModel = viewModel()

    val colorTheme = theme.readSettings().observeAsState()
    var colorSelected = colorTheme.value!!.data

    if(colorSelected == null){
        colorSelected = ThemeColors.SystemMode
    }

    if(colorSelected == ThemeColors.SystemMode){
        return isSystemInDarkTheme()
    }

    return colorTheme.value!!.data == ThemeColors.DarkMode
}