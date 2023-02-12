package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors

@Composable
fun TitleAndSubtitle(
    title: String,
    subtitle: String,
    colorTitle: Color = MaterialTheme.colors.onBackground,
    colorSubtitle: Color = MaterialTheme.myCustomColors.subtitle,
    sizeTitle: TextUnit = 15.sp,
    sizeSubtitle: TextUnit = 12.sp,
    maxLinesTitle: Int = 1,
    maxLinesSubtitle: Int = 1
) {
    Column(verticalArrangement = Arrangement.Center) {
        Text(
            text = title,
            fontSize = sizeTitle,
            color = colorTitle,
            maxLines = maxLinesTitle
        )
        Text(
            text = subtitle,
            fontSize = sizeSubtitle,
            color = colorSubtitle,
            maxLines = maxLinesSubtitle
        )
    }
}

@Composable
fun ShimmerTitleAndSubtitle(
    sizeTitle: TextUnit = 15.sp,
    sizeSubtitle: TextUnit = 12.sp,
    brush: Brush = MaterialTheme.myCustomColors.shimmerBrush(),
) {
    Column(verticalArrangement = Arrangement.Center) {
        Spacer(
            modifier = Modifier
                .height(
                    with(LocalDensity.current) {
                        sizeTitle.toDp()
                    }
                )
                .width(250.dp)
                .background(brush)
        )
        Spacer(
            modifier = Modifier.padding(4.dp)
        )
        Spacer(
            modifier = Modifier
                .height(
                    with(LocalDensity.current) {
                        sizeSubtitle.toDp()
                    }
                )
                .width(250.dp)
                .background(brush)
        )
    }
}

@Preview
@Composable
fun PreviewTitleAndSubtitle() {
    CMU_07_8200398_8200591_8200592Theme {
        TitleAndSubtitle(
            title = "PreviewTitle",
            subtitle = "PreviewSubtitle",
        )
    }
}

@Preview
@Composable
fun PreviewShimmerTitleAndSubtitle() {
    CMU_07_8200398_8200591_8200592Theme {
        ShimmerTitleAndSubtitle()
    }
}