package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme

@Composable
fun MyErrorMessage(
    text: String,
    textColor: Color = MaterialTheme.colors.error,
    textSize: TextUnit = 14.sp,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
) {
    Text(
        text = text,
        color = textColor,
        fontSize = textSize,
        modifier = modifier
    )
}

@Preview
@Composable
fun PreviewMyErrorMessage() {
    CMU_07_8200398_8200591_8200592Theme {
        MyErrorMessage(
            text = "Preview Error"
        )
    }
}