package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors

@Composable
fun UserContent(
    imageRequest: String,
    name: String,
    email: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .background(MaterialTheme.myCustomColors.backgroundSecondary),
        contentAlignment = Alignment.Center

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Avatar(
                imageRequest = imageRequest
            )
            Spacer(modifier = Modifier.padding(10.dp))
            TitleAndSubtitle(
                title = name,
                subtitle = email,
                colorTitle = MaterialTheme.myCustomColors.onBackgroundSecondary,
                colorSubtitle = MaterialTheme.myCustomColors.subtitleBackground
            )
        }
    }
}

@Composable
fun ShimmerUserContent(
    brush: Brush = MaterialTheme.myCustomColors.shimmerBrush()
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ShimmerAvatar(
                brush = brush
            )
            Spacer(modifier = Modifier.padding(10.dp))
            ShimmerTitleAndSubtitle(
                brush = brush
            )
        }
    }
}

@Preview
@Composable
fun PreviewUserContentErrorImage() {
    CMU_07_8200398_8200591_8200592Theme {
        UserContent(
            imageRequest = "",
            email = "preview@example.com",
            name = "Preview",
        )
    }
}

@Preview
@Composable
fun PreviewUserContentSuccessImage() {
    CMU_07_8200398_8200591_8200592Theme {
        UserContent(
            imageRequest = "",
            email = "preview@example.com",
            name = "Preview",
        )
    }
}

@Preview
@Composable
fun PreviewShimmerUserContent() {
    CMU_07_8200398_8200591_8200592Theme {
        ShimmerUserContent()
    }
}