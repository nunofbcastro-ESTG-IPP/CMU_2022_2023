package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme

@Composable
fun MyEnergyConsumption(consume: Double, iconSize: Dp) {
    Row(
        modifier = Modifier.padding(end = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.charger_48),
            "charge", modifier = Modifier
                .size(iconSize)
        )
        TitleAndSubtitle(
            title = "$consume ${stringResource(id = R.string.power_unity)}",
            subtitle = stringResource(id = R.string.power_usage),
            sizeTitle = 15.sp,
            sizeSubtitle = 12.sp,
            maxLinesSubtitle = 2
        )
    }
}

@Preview
@Composable
fun PreviewMyEnergyConsumption() {
    CMU_07_8200398_8200591_8200592Theme {
        MyEnergyConsumption(
            consume = 10.0,
            iconSize = 10.dp
        )
    }
}