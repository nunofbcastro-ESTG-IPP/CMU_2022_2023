package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    readOnly: Boolean = false,
    shape: RoundedCornerShape = RoundedCornerShape(15.dp),
    backgroundColor: Color = MaterialTheme.myCustomColors.textFieldBackground,
    focusedIndicatorColor: Color = MaterialTheme.myCustomColors.textFieldFocusBorder,
    unfocusedIndicatorColor: Color = MaterialTheme.myCustomColors.textFieldUnFocusBorder,
    textColor: Color = MaterialTheme.myCustomColors.onTextFieldBackground,
    cursorColor: Color = MaterialTheme.myCustomColors.textFieldCursor,
    placeholderColor: Color = MaterialTheme.myCustomColors.textFieldPlaceholder,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
    enable: Boolean = true,
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = modifier,
        readOnly = readOnly,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = backgroundColor,
            cursorColor = cursorColor,
            focusedIndicatorColor = focusedIndicatorColor,
            unfocusedIndicatorColor = unfocusedIndicatorColor,
            textColor = textColor,
            trailingIconColor = MaterialTheme.myCustomColors.textFieldIcon
        ),
        shape = shape,
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = keyboardType
        ),
        placeholder = {
            Text(placeholder, color = placeholderColor)
        },
        trailingIcon = trailingIcon,
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        visualTransformation = if (!isPassword) VisualTransformation.None else PasswordVisualTransformation(),
        enabled = enable,
    )
}

@Composable
fun ShimmerMyTextField(
    brush: Brush = MaterialTheme.myCustomColors.shimmerBrush(),
    shape: RoundedCornerShape = RoundedCornerShape(15.dp),
    size: Dp = 60.dp
) {
    Spacer(
        modifier = Modifier.padding(2.dp)
    )
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(
                size
            )
            .clip(shape)
            .background(brush)
    )
}

@Preview
@Composable
fun PreviewMyTextField() {
    var previewInput by remember { mutableStateOf("") }

    CMU_07_8200398_8200591_8200592Theme {
        MyTextField(
            value = previewInput,
            onValueChange = { value -> previewInput = value },
            placeholder = "Preview"
        )
    }
}

@Preview
@Composable
fun PreviewShimmerMyTextField() {
    CMU_07_8200398_8200591_8200592Theme {
        ShimmerMyTextField()
    }
}