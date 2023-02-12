package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors

@Composable
fun LoadableAsyncImage(
    imageRequest: Any?,
    placeholderError: Painter = painterResource(R.drawable.placeholder_error),
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    brushLoading: Brush = MaterialTheme.myCustomColors.shimmerBrush(),
) {
    var isLoading by rememberSaveable(imageRequest) { mutableStateOf(true) }
    var isError by rememberSaveable(imageRequest) { mutableStateOf(false) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = imageRequest,
            contentDescription = contentDescription,
            contentScale = contentScale,
            onSuccess = {
                isLoading = false
            },
            onError = {
                isLoading = false
                isError = true
            },
        )

        if (isLoading) {
            Spacer(
                modifier = Modifier
                    .background(brushLoading)
                    .then(modifier)
            )
        }

        if (isError) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = placeholderError,
                contentDescription = contentDescription,
                contentScale = contentScale,
            )
        }
    }
}

@Preview
@Composable
fun PreviewLoadableAsyncImageError(){
    CMU_07_8200398_8200591_8200592Theme {
        LoadableAsyncImage(
            imageRequest = null,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
        )
    }
}

@Preview
@Composable
fun PreviewLoadableAsyncImageSuccess(){
    CMU_07_8200398_8200591_8200592Theme {
        LoadableAsyncImage(
            imageRequest = "https://randomuser.me/api/portraits/men/34.jpg",
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
        )
    }
}