package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors

@Composable
fun MyButton(
    text: String,
    onClick: () -> Unit,
    textColor: Color = MaterialTheme.myCustomColors.onButton,
    fontSize: TextUnit = 17.sp,
    gradient: Brush = MaterialTheme.myCustomColors.button,
    modifierButton: Modifier = Modifier
        .padding(5.dp)
        .clip(RoundedCornerShape(15.dp)),
    modifierText: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp, horizontal = 5.dp)
) {
    val focusManager = LocalFocusManager.current

    Button(
        modifier = modifierButton,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues(),
        onClick = {
            focusManager.clearFocus()
            onClick()
        },
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .then(modifierText),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = text, color = textColor, fontSize = fontSize)
        }
    }
}

@Preview
@Composable
fun PreviewMyButton(){
    CMU_07_8200398_8200591_8200592Theme {
        MyButton(
            text = "Preview",
            onClick = {},
        )
    }
}