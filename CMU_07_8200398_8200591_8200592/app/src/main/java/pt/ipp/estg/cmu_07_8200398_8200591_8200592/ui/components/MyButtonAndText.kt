package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors

@Composable
fun MyButtonAndText(
    text: String? = null,
    textColor: Color = MaterialTheme.myCustomColors.subtitle,
    textSize: TextUnit = 15.sp,
    size: Dp = 55.dp,
    icon: ImageVector,
    iconSize: Float = 0.85f,
    iconColor: Color = MaterialTheme.colors.onBackground,
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(15.dp),
    borderWidth: Dp = 1.dp,
    borderColor: Color = MaterialTheme.myCustomColors.subtitle,
    background: Color = Color.White,
    padding: Dp = 5.dp,
) {
    var selected by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { selected = !selected; onClick() },
            modifier = Modifier
                .width(size)
                .height(size),
            shape = shape,
            border = BorderStroke(borderWidth, borderColor),
            colors = ButtonDefaults.buttonColors(background),
            contentPadding = PaddingValues(padding)
        ) {
            Icon(
                icon,
                modifier = Modifier.fillMaxSize(iconSize),
                contentDescription = "Icon Description",
                tint = iconColor
            )
        }
        if (text != null) {
            Text(text = text, color = textColor, fontSize = textSize)
        }
    }
}

@Preview
@Composable
fun PreviewMyButtonAndText() {

    CMU_07_8200398_8200591_8200592Theme {
        MyButtonAndText(
            text = "Preview",
            icon = Icons.Filled.Delete,
            onClick = {},
        )
    }
}

@Preview
@Composable
fun PreviewMyButtonAndTextNoText() {
    CMU_07_8200398_8200591_8200592Theme {
        MyButtonAndText(
            icon = Icons.Filled.Delete,
            onClick = {},
        )
    }
}