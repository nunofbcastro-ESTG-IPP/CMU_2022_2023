package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme

@Composable
fun MyImagePicker(
    savedImage: Any? = null,
    setUri: (Uri?) -> Unit,
) {
    var hasImage by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            setUri(uri)
            hasImage = uri != null
            imageUri = uri
        }
    )

    val defaultImage = savedImage ?: R.drawable.image_placeholder

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = if (hasImage && imageUri != null) imageUri else defaultImage,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(bottom = 15.dp),
            contentDescription = "House image",
            contentScale = ContentScale.Crop
        )
        MyButton(
            text = stringResource(id = R.string.select_image),
            onClick = {
                imagePicker.launch("image/*")
            },
        )
    }
}

@Preview
@Composable
fun PreviewMyImagePicker() {
    CMU_07_8200398_8200591_8200592Theme {
        MyImagePicker(
            setUri = {}
        )
    }
}