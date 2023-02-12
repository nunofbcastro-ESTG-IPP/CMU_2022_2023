package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors

data class NavigationHandlers(
    val onBackRequested: (() -> Unit)? = null,
    val drawerMenu: Boolean = false
)

@Composable
fun MyTopAppBarDefault(
    iconsColor: Color = MaterialTheme.colors.onBackground,
    navigation: NavigationHandlers = NavigationHandlers(),
    content: @Composable (() -> Unit)? = null,
    openDrawer: () -> Unit
) {
    val iconSize = 23.dp

    Row() {
        if (navigation.onBackRequested != null) {
            IconButton(onClick = navigation.onBackRequested) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = stringResource(id = R.string.top_bar_go_back),
                    modifier = Modifier
                        .size(iconSize)
                        .padding(2.dp),
                    tint = iconsColor,
                )
            }
        }
        Column(
            Modifier
                .weight(1f)
                .padding(7.dp)
                .defaultMinSize(minHeight = iconSize),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content?.invoke()
        }
        if (navigation.drawerMenu) {
            IconButton(onClick = { openDrawer() }) {
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    modifier = Modifier.size(iconSize),
                    contentDescription = stringResource(id = R.string.top_bar_go_back),
                    tint = iconsColor
                )
            }
        }
    }

}

@Composable
fun MyTopAppBar(
    navigation: NavigationHandlers = NavigationHandlers(),
    content: @Composable (() -> Unit)?,
    openDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .background(MaterialTheme.myCustomColors.backgroundSecondary)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        MyTopAppBarDefault(
            iconsColor = MaterialTheme.myCustomColors.onBackgroundSecondary,
            navigation = navigation,
            openDrawer = openDrawer
        )
        content?.invoke()
    }
}

@Preview
@Composable
fun PreviewMyTopAppBarNoIcons() {
    CMU_07_8200398_8200591_8200592Theme {
        MyTopAppBar(
            content = {
                Text(text = "Preview")
            },
            openDrawer = {}
        )
    }
}

@Preview
@Composable
fun PreviewMyTopAppBarDefaultNoIcons() {
    CMU_07_8200398_8200591_8200592Theme {
        MyTopAppBarDefault(
            content = {
                Text(text = "Preview")
            },
            openDrawer = {}
        )
    }
}

@Preview
@Composable
fun PreviewMyTopAppBar() {
    CMU_07_8200398_8200591_8200592Theme {
        MyTopAppBar(
            navigation = NavigationHandlers(
                onBackRequested = {},
                drawerMenu = true
            ),
            content = {
                Text(text = "Preview")
            },
            openDrawer = {}
        )
    }
}

@Preview
@Composable
fun PreviewMyTopAppBarDefault() {
    CMU_07_8200398_8200591_8200592Theme {
        MyTopAppBarDefault(
            navigation = NavigationHandlers(
                onBackRequested = {},
                drawerMenu = true
            ),
            content = {
                Text(text = "Preview")
            },
            openDrawer = {}
        )
    }
}