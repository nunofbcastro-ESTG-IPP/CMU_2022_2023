package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SmartHouseViewModels
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.preferences.ThemeColors
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SettingsPreferencesViewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.UserPreferencesViewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.SegmentedControl
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.ShimmerUserContent
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.UserContent
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.AuthenticationViewModel

private data class NavigationDrawerItem(
    val image: ImageVector,
    val label: String,
    val onClick: () -> Unit,
    val showUnreadBubble: Boolean = false
)

data class NavigationsDrawer(
    val onNavigateToSettings: () -> Unit,
    val onNavigateToHouses: () -> Unit,
    val logout: () -> Unit
)

@Composable
fun Drawer(
    navigationsDrawer: NavigationsDrawer,
    closeDrawer: ()->Unit
) {
    val itemsList = prepareNavigationDrawerItems(
        navigationsDrawer = navigationsDrawer,
        closeDrawer = closeDrawer
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.Start,
    ) {
        item {
            User()
        }

        item {
            ChangeThemeColor()
        }

        items(itemsList) { item ->
            NavigationListItem(item = item)
        }
    }
}

@Composable
private fun NavigationListItem(
    item: NavigationDrawerItem,
    unreadBubbleColor: Color = MaterialTheme.myCustomColors.unreadBubbleColor
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                item.onClick()
            }
            .padding(horizontal = 24.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // icon and unread bubble
        Box {

            Icon(
                imageVector = item.image,
                contentDescription = item.label + " Icon",
                tint = MaterialTheme.colors.onBackground
            )

            // unread bubble
            if (item.showUnreadBubble) {
                Box(
                    modifier = Modifier
                        .size(size = 6.dp)
                        .align(alignment = Alignment.TopEnd)
                        .background(color = unreadBubbleColor, shape = CircleShape)
                )
            }
        }

        // label
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = item.label,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
private fun prepareNavigationDrawerItems(
    navigationsDrawer: NavigationsDrawer,
    closeDrawer: ()->Unit
): List<NavigationDrawerItem> {
    val context = LocalContext.current
    val smartHouseViewModels: SmartHouseViewModels = viewModel()
    val userPreferencesViewModel: UserPreferencesViewModel = viewModel()
    val authenticationViewModel: AuthenticationViewModel = viewModel()

    val isLoading = smartHouseViewModels.isLoading.observeAsState()
    var isLogout by remember { mutableStateOf(false) }

    val itemsList = arrayListOf<NavigationDrawerItem>()

    if(isLoading.value==false && isLogout){
        LaunchedEffect(key1 = true) {
            navigationsDrawer.logout()
        }
    }

    itemsList.add(
        NavigationDrawerItem(
            image = Icons.Filled.Settings,
            label = stringResource(id = R.string.settings),
            onClick = {
                closeDrawer()

                navigationsDrawer.onNavigateToSettings()
            }
        )
    )

    itemsList.add(
        NavigationDrawerItem(
            image = Icons.Filled.Home,
            label = stringResource(id = R.string.houses),
            onClick = {
                closeDrawer()

                navigationsDrawer.onNavigateToHouses()
            }
        )
    )

    itemsList.add(
        NavigationDrawerItem(
            image = Icons.Filled.Logout,
            label = stringResource(id = R.string.logout),
            onClick = {
                closeDrawer()

                //clear data saved
                authenticationViewModel.logout()
                userPreferencesViewModel.deleteUser()
                smartHouseViewModels.clearDatabase()
                context.cacheDir.deleteRecursively()

                isLogout = true
            }
        )
    )

    return itemsList
}

@Composable
private fun ChangeThemeColor() {
    val theme: SettingsPreferencesViewModel = viewModel()
    val colorTheme = theme.readSettings().observeAsState()

    val modes = mutableListOf<String>()

    enumValues<ThemeColors>().forEach { modes.add(stringResource(id = it.string))  }

    Column(
        Modifier.padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = stringResource(id = R.string.select_theme),
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.onBackground
        )
        SegmentedControl(
            items = modes,
            defaultSelectedItemIndex = colorTheme.value!!.data!!.ordinal
        ) { position ->
            theme.saveColorSettings(ThemeColors.values()[position])
        }
    }
}

@Composable
private fun User(){
    val userPreferencesViewModel: UserPreferencesViewModel = viewModel()
    val userAndToken = userPreferencesViewModel.readUser().observeAsState()

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .background(MaterialTheme.myCustomColors.backgroundSecondary)
    ){
        if(userAndToken.value!!.loading || userAndToken.value!!.data == null){
            ShimmerUserContent()
        }else{
            UserContent(
                imageRequest = userAndToken.value!!.data!!.avatar!!,
                name = userAndToken.value!!.data!!.name,
                email = userAndToken.value!!.data!!.email,
            )
        }
        Spacer(modifier = Modifier.padding(15.dp))
    }
}

@Preview
@Composable
fun PreviewDrawer() {
    CMU_07_8200398_8200591_8200592Theme {
        Drawer(
            navigationsDrawer = NavigationsDrawer(
                onNavigateToSettings = {},
                onNavigateToHouses = {},
                logout = {},
            ),
            closeDrawer = {}
        )
    }
}

@Preview
@Composable
fun PreviewNavigationListItem() {
    CMU_07_8200398_8200591_8200592Theme {
        NavigationListItem(
            item = NavigationDrawerItem(
                image = Icons.Filled.Settings,
                label = stringResource(id = R.string.settings),
                onClick = {
                }
            )
        )
    }
}

@Preview
@Composable
fun PreviewUser() {
    CMU_07_8200398_8200591_8200592Theme {
        User()
    }
}