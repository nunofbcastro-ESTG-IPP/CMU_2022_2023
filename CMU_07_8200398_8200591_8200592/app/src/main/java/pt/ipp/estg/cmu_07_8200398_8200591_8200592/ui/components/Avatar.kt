package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors

private val defaultSize = 64.dp

@Composable
fun Avatar(
    imageRequest: Any?,
    size: Dp = defaultSize
) {
    LoadableAsyncImage(
        imageRequest = imageRequest,
        placeholderError = painterResource(R.drawable.avatar),
        contentDescription = "Avatar",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
    )
}

@Composable
fun ShimmerAvatar(
    brush: Brush = MaterialTheme.myCustomColors.shimmerBrush(),
    size: Dp = defaultSize
) {
    CMU_07_8200398_8200591_8200592Theme {
        Spacer(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(brush)
        )
    }
}

@Preview
@Composable
fun PreviewAvatarError() {
    CMU_07_8200398_8200591_8200592Theme {
        Avatar(
            imageRequest = null
        )
    }
}

@Preview
@Composable
fun PreviewAvatarSuccess() {
    CMU_07_8200398_8200591_8200592Theme {
        Avatar(
            imageRequest = "https://randomuser.me/api/portraits/men/34.jpg"
        )
    }
}

@Preview
@Composable
fun PreviewShimmerAvatar() {
    CMU_07_8200398_8200591_8200592Theme {
        ShimmerAvatar()
    }
}