package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.splash

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startForegroundService
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.AppBarState
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.firebase.AuthStatus
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.services.UpdateRoomService
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.AuthenticationViewModel

data class NavigationsSpash(
    val onNavigateToLogin: () -> Unit,
    val onNavigateToHome: () -> Unit,
)

@Composable
fun SplashScreen(
    onComposing: (AppBarState) -> Unit,
    navigationsSpash: NavigationsSpash,
) {
    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                topBar = false
            )
        )
    }
    val context = LocalContext.current
    val authenticationViewModel: AuthenticationViewModel =
        viewModel(LocalContext.current as ComponentActivity)
    val authentication = authenticationViewModel.authState.observeAsState()

    if (authentication.value != AuthStatus.LOADING) {
        LaunchedEffect(key1 = true) {
            if (authentication.value == AuthStatus.LOGGED) {
                val serviceIntent = Intent(context, UpdateRoomService::class.java)
                startForegroundService(context, serviceIntent)
                navigationsSpash.onNavigateToHome()
            } else {
                navigationsSpash.onNavigateToLogin()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ContextCompat.getDrawable(LocalContext.current, R.mipmap.ic_launcher)?.let {
            Image(
                bitmap = it.toBitmap().asImageBitmap(),
                contentDescription = "Icon",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Preview
@Composable
fun PreviewSplashScreen() {
    CMU_07_8200398_8200591_8200592Theme {
        SplashScreen(
            onComposing = {},
            navigationsSpash = NavigationsSpash(
                onNavigateToHome = {},
                onNavigateToLogin = {}
            )
        )
    }
}