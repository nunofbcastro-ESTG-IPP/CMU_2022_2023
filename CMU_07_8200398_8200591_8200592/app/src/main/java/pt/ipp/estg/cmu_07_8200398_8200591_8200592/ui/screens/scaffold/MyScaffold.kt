package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.scaffold

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.AppBarState
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.navigation.NavHost
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.navigation.Screen
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.ConnectivityObserver
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.*
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.drawer.Drawer
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.drawer.NavigationsDrawer
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyScaffold(
    networkStatus: ConnectivityObserver.NetworkStatus
) {
    val focusManager = LocalFocusManager.current

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    var appBarState by remember { mutableStateOf(AppBarState()) }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        topBar = generateTopBar(
            appBarState = appBarState,
            navController = navController,
            scaffoldState = scaffoldState
        ),
        bottomBar = {
            if (
                networkStatus != ConnectivityObserver.NetworkStatus.Available
            ) {
                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colors.error)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.no_connection),
                    color = MaterialTheme.colors.onError,
                )
            }
        },
        drawerBackgroundColor = MaterialTheme.colors.background,
        drawerShape = RoundedCornerShape(topEndPercent = 5, bottomEndPercent = 5),
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = generateDrawer(
            appBarState = appBarState,
            navController = navController,
            scaffoldState = scaffoldState
        ),
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }
    ) {
        NavHost(
            navController = navController,
            onComposing = {
                appBarState = it
            }
        )
    }
}

@Preview
@Composable
fun PreviewMyScaffoldNetworkSuccess() {
    CMU_07_8200398_8200591_8200592Theme {
        MyScaffold(
            networkStatus = ConnectivityObserver.NetworkStatus.Available
        )
    }
}

@Preview
@Composable
fun PreviewMyScaffoldNetworkError() {
    CMU_07_8200398_8200591_8200592Theme {
        MyScaffold(
            networkStatus = ConnectivityObserver.NetworkStatus.Unavailable
        )
    }
}

@Composable
private fun generateDrawer(
    appBarState: AppBarState,
    navController: NavController,
    scaffoldState: ScaffoldState
): @Composable() (ColumnScope.() -> Unit)? {
    val coroutineScope = rememberCoroutineScope()
    if (appBarState.drawerMenu) {
        return {
            Drawer(
                navigationsDrawer = NavigationsDrawer(
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route)
                    },
                    onNavigateToHouses = {
                        navController.navigate(Screen.Houses.route) {
                            popUpTo(Screen.Houses.route) {
                                inclusive = true
                            }
                        }
                    },
                    logout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Houses.route) {
                                inclusive = true
                            }
                        }
                    }
                ),
                closeDrawer = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        }
    }
    return null
}

@Composable
private fun generateTopBar(
    appBarState: AppBarState,
    navController: NavController,
    scaffoldState: ScaffoldState
): @Composable (() -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    if (!appBarState.topBar) {
        return {}
    }

    val navigation = generateTopBarRoutes(
        appBarState = appBarState,
        navController = navController
    )

    if (appBarState.transparentBackground) {
        return {
            MyTopAppBarDefault(
                navigation = navigation,
                content = { appBarState.content() },
                openDrawer = {
                    if (appBarState.drawerMenu) {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                },
            )
        }
    }

    return {
        MyTopAppBar(
            navigation = navigation,
            content = { appBarState.content() },
            openDrawer = {
                if (appBarState.drawerMenu) {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            }
        )
    }
}

private fun generateTopBarRoutes(
    appBarState: AppBarState,
    navController: NavController,
): NavigationHandlers {
    var onBackRequested: (() -> Unit)? = null

    if (appBarState.navigateToBackScreen) {
        onBackRequested = {
            navController.popBackStack()
        }
    }

    return NavigationHandlers(
        onBackRequested = onBackRequested,
        drawerMenu = appBarState.drawerMenu
    )
}