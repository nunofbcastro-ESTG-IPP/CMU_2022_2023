package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.login

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.AppBarState
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.firebase.AuthStatus
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.services.UpdateRoomService
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.UserPreferencesViewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.DialogBoxLoading
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyButton
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyErrorMessage
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyTextField
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.shadow
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.emailValidation
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.passwordValidation
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.AuthenticationViewModel

@Composable
fun LoginScreen(
    onComposing: (AppBarState) -> Unit,
    onNavigateRegister: () -> Unit,
    onNavigateHouses: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                topBar = false
            )
        )
    }
    val context = LocalContext.current

    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    val userPreferencesViewModel: UserPreferencesViewModel = viewModel()
    val authenticationViewModel: AuthenticationViewModel =
        viewModel(LocalContext.current as ComponentActivity)

    val isLoadingUser = userPreferencesViewModel.isLoading.observeAsState()
    val authentication = authenticationViewModel.authState.observeAsState()

    var isReady by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var isClickLogin by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = authentication.value) {
        if (authentication.value == AuthStatus.LOGGED && isClickLogin) {
            userPreferencesViewModel.updateUser()

            isClickLogin = false
            isReady = true
        }else if(authentication.value == AuthStatus.INVALID_LOGIN){
            isError = true
        }
    }

    if (
        isLoadingUser.value == false &&
        isReady
    ) {
        LaunchedEffect(key1 = true) {
            isReady = false
            val serviceIntent = Intent(context, UpdateRoomService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
            onNavigateHouses()
        }
    }


    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .shadow(borderRadius = 50.dp, blurRadius = 30.dp)
                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                .background(MaterialTheme.myCustomColors.backgroundSecondary)
                .padding(8.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .fillMaxHeight(0.55f)
                        .background(Color.Transparent)
                        .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(30.dp)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.home),
                        modifier = Modifier.size(100.dp),
                        contentDescription = stringResource(id = R.string.home_screen_image),
                        tint = MaterialTheme.myCustomColors.onBackgroundSecondary
                    )
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.myCustomColors.onBackgroundSecondary,
                        fontSize = 35.sp
                    )
                    Text(
                        text = stringResource(id = R.string.welcome_mensage_login),
                        modifier = Modifier.padding(horizontal = 30.dp),
                        color = MaterialTheme.myCustomColors.subtitleBackground,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                ) {
                    MyTextField(
                        value = emailInput,
                        onValueChange = {
                            emailInput = it
                        },
                        placeholder = stringResource(id = R.string.email),
                        trailingIcon = {
                            Icon(
                                Icons.Filled.Email,
                                modifier = Modifier.size(20.dp),
                                contentDescription = stringResource(id = R.string.email) + " Icon",
                                tint = MaterialTheme.myCustomColors.textFieldIcon
                            )
                        },
                        keyboardType = KeyboardType.Email,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .testTag("emailInput")
                    )

                    MyTextField(
                        value = passwordInput,
                        onValueChange = {
                            passwordInput = it
                        },
                        placeholder = stringResource(id = R.string.password),
                        trailingIcon = {
                            Icon(
                                Icons.Filled.Lock,
                                modifier = Modifier.size(20.dp),
                                contentDescription = stringResource(id = R.string.password) + " Icon",
                                tint = MaterialTheme.myCustomColors.textFieldIcon
                            )
                        },
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .testTag("passwordInput")
                    )

                    if (isError) {
                        MyErrorMessage(
                            text = stringResource(id = R.string.invalid_login),
                            textColor = Color.White,
                            modifier = Modifier
                                .testTag("errorLogin")
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .offset(y = (-45).dp)
                .fillMaxWidth(0.45f)
                .align(Alignment.CenterHorizontally)
        ) {
            MyButton(
                text = stringResource(id = R.string.log_in),
                onClick = {
                    if(!emailValidation(emailInput)){
                        isError = true
                        return@MyButton
                    }

                    if(!passwordValidation(passwordInput)){
                        isError = true
                        return@MyButton
                    }

                    isClickLogin = true
                    isError = false
                    authenticationViewModel.login(emailInput, passwordInput)
                },
                modifierButton = Modifier
                    .testTag("loginButton")
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 5.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(15.dp))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-27).dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.without_account),
                color = MaterialTheme.myCustomColors.subtitle
            )
            ClickableText(
                text = AnnotatedString(stringResource(id = R.string.create_account)),
                style = TextStyle(
                    color = MaterialTheme.myCustomColors.subtitle,
                    fontSize = 15.sp,
                    textDecoration = TextDecoration.Underline
                ),
                onClick = {
                    onNavigateRegister()
                }
            )
        }

        if (authentication.value == AuthStatus.LOADING || isReady) {
            DialogBoxLoading()
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    CMU_07_8200398_8200591_8200592Theme {
        LoginScreen(
            onComposing = {},
            onNavigateRegister = {},
            onNavigateHouses = {},
        )
    }
}