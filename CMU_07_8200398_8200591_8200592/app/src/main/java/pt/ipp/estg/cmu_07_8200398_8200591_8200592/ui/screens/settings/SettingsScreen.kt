package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.settings

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.dmoral.toasty.Toasty
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.User
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.AppBarState
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.UserPreferencesViewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.*
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.emailValidation
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.nameValidation
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.passwordValidation

@Composable
fun SettingsScreen(
    onComposing: (AppBarState) -> Unit,
) {
    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                topBar = true,
                transparentBackground = true,
                drawerMenu = false,
                navigateToBackScreen = true,
                content = {}
            )
        )
    }

    val userPreferencesViewModel: UserPreferencesViewModel = viewModel()
    val user = userPreferencesViewModel.readUser().observeAsState()

    if (user.value!!.loading || user.value!!.data == null) {
        SettingsScreenIsLoading()
    } else {
        SettingsScreenNotLoading(
            user = user.value!!.data!!,
            userPreferencesViewModel = userPreferencesViewModel,
        )
    }
}

@Composable
private fun SettingsScreenIsLoading() {
    val brush = MaterialTheme.myCustomColors.shimmerBrush()
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ShimmerAvatar(
            size = 150.dp
        )
        Spacer(modifier = Modifier.padding(8.dp))
        ShimmerMyTextField(
            brush = brush
        )
        Spacer(modifier = Modifier.padding(5.dp))
        ShimmerMyTextField(
            brush = brush
        )
        Spacer(modifier = Modifier.padding(5.dp))
        ShimmerMyTextField(
            brush = brush
        )
        Spacer(modifier = Modifier.padding(5.dp))
        ShimmerMyTextField(
            brush = brush
        )
        Spacer(modifier = Modifier.padding(5.dp))
        MyButton(
            stringResource(id = R.string.save),
            onClick = {}
        )
    }

}

@Composable
private fun SettingsScreenNotLoading(
    user: User,
    userPreferencesViewModel: UserPreferencesViewModel
) {
    val context = LocalContext.current
    val messageSuccess = stringResource(id = R.string.success_change_user)
    val messageError = stringResource(id = R.string.error_change_user)

    var emailInput by remember { mutableStateOf(user.email) }
    var nameInput by remember { mutableStateOf(user.name) }
    var oldPasswordInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var confirmPasswordInput by remember { mutableStateOf("") }

    val isLoading = userPreferencesViewModel.isLoading.observeAsState()
    val isError = userPreferencesViewModel.isError.observeAsState()

    var isClickSave by remember {
        mutableStateOf<Boolean>(false)
    }

    var errorNameMessage by remember {
        mutableStateOf<Boolean>(false)
    }
    var errorEmailMessage by remember {
        mutableStateOf<Boolean>(false)
    }
    var errorOldPasswordMessage by remember {
        mutableStateOf<Boolean>(false)
    }
    var errorPasswordMessage by remember {
        mutableStateOf<Boolean>(false)
    }
    var errorConfirmPasswordMessage by remember {
        mutableStateOf<Boolean>(false)
    }

    LaunchedEffect(key1 = isLoading.value) {
        if (isClickSave && isLoading.value == false) {
            isClickSave = false

            if (isError.value == true) {
                Toasty.success(
                    context,
                    messageSuccess,
                    Toast.LENGTH_SHORT,
                    true
                ).show()
            } else {
                Toasty.error(
                    context,
                    messageError,
                    Toast.LENGTH_SHORT,
                    true
                ).show()
            }
        }
    }

    var fileImage by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            fileImage = uri
        }
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.settings),
                fontSize = 25.sp,
                color = MaterialTheme.myCustomColors.subtitle
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Box {
                val avatarSize = 150.dp
                if (fileImage == null) {
                    Avatar(
                        imageRequest = user.avatar,
                        size = avatarSize
                    )
                } else {
                    Avatar(
                        imageRequest = fileImage,
                        size = avatarSize
                    )
                }
                FloatingActionButton(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomEnd),
                    onClick = {
                        imagePicker.launch("image/*")
                    },
                    backgroundColor = MaterialTheme.colors.secondary
                ) {
                    Icon(
                        Icons.Filled.Create,
                        modifier = Modifier
                            .size(45.dp)
                            .padding(all = 5.dp),
                        contentDescription = "Pencil Icon",
                        tint = MaterialTheme.colors.onSecondary
                    )
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            MyTextField(
                value = nameInput,
                onValueChange = {
                    nameInput = it
                },
                trailingIcon = {
                    Icon(
                        Icons.Filled.Person,
                        modifier = Modifier.size(20.dp),
                        contentDescription = stringResource(id = R.string.name) + " Icon",
                        tint = MaterialTheme.myCustomColors.textFieldIcon
                    )
                },
                placeholder = stringResource(id = R.string.name)
            )
            if (errorNameMessage) {
                MyErrorMessage(text = stringResource(id = R.string.invalid_name))
            }
            MyTextField(
                value = emailInput,
                onValueChange = {
                    emailInput = it
                },
                enable = false,
                trailingIcon = {
                    Icon(
                        Icons.Filled.Email,
                        modifier = Modifier.size(20.dp),
                        contentDescription = stringResource(id = R.string.email) + " Icon",
                        tint = MaterialTheme.myCustomColors.textFieldIcon
                    )
                },
                placeholder = stringResource(id = R.string.email)
            )
            if (errorEmailMessage) {
                MyErrorMessage(text = stringResource(id = R.string.invalid_email))
            }
            MyTextField(
                value = oldPasswordInput,
                onValueChange = {
                    oldPasswordInput = it
                },
                trailingIcon = {
                    Icon(
                        Icons.Filled.Lock,
                        modifier = Modifier.size(20.dp),
                        contentDescription = stringResource(id = R.string.old_password) + " Icon",
                        tint = MaterialTheme.myCustomColors.textFieldIcon
                    )
                },
                keyboardType = KeyboardType.Password,
                isPassword = true,
                placeholder = stringResource(id = R.string.old_password),
            )
            if (errorOldPasswordMessage) {
                MyErrorMessage(text = stringResource(id = R.string.invalid_password))
            }
            MyTextField(
                value = passwordInput,
                onValueChange = {
                    passwordInput = it
                },
                trailingIcon = {
                    Icon(
                        Icons.Filled.Lock,
                        modifier = Modifier.size(20.dp),
                        contentDescription = stringResource(id = R.string.new_password) + " Icon",
                        tint = MaterialTheme.myCustomColors.textFieldIcon
                    )
                },
                keyboardType = KeyboardType.Password,
                isPassword = true,
                placeholder = stringResource(id = R.string.new_password),
            )
            if (errorPasswordMessage) {
                MyErrorMessage(text = stringResource(id = R.string.invalid_password))
            }
            MyTextField(
                value = confirmPasswordInput,
                onValueChange = {
                    confirmPasswordInput = it
                },
                trailingIcon = {
                    Icon(
                        Icons.Filled.Lock,
                        modifier = Modifier.size(20.dp),
                        contentDescription = stringResource(id = R.string.confirm_new_password) + " Icon",
                        tint = MaterialTheme.myCustomColors.textFieldIcon
                    )
                },
                keyboardType = KeyboardType.Password,
                isPassword = true,
                placeholder = stringResource(id = R.string.confirm_new_password),
            )
            if (errorConfirmPasswordMessage) {
                MyErrorMessage(text = stringResource(id = R.string.invalid_confirm_password))
            }
            Spacer(modifier = Modifier.padding(5.dp))
            MyButton(
                stringResource(id = R.string.save),
                onClick = {
                    isClickSave = true

                    errorNameMessage = false
                    errorEmailMessage = false
                    errorOldPasswordMessage = false
                    errorPasswordMessage = false
                    errorConfirmPasswordMessage = false

                    if (!nameValidation(nameInput)) {
                        errorNameMessage = true
                    }

                    if (!emailValidation(emailInput)) {
                        errorEmailMessage = true
                    }

                    if (oldPasswordInput != "" && !passwordValidation(oldPasswordInput)) {
                        errorOldPasswordMessage = true
                    }

                    if (passwordInput != "" || confirmPasswordInput != "") {
                        if (!passwordValidation(passwordInput)) {
                            errorPasswordMessage = true
                        }

                        if (passwordInput != confirmPasswordInput) {
                            errorConfirmPasswordMessage = true
                        }
                    }

                    if (
                        errorNameMessage ||
                        errorEmailMessage ||
                        errorOldPasswordMessage ||
                        errorPasswordMessage ||
                        errorConfirmPasswordMessage
                    ) {
                        isClickSave = false
                        return@MyButton
                    }

                    user.email = emailInput
                    user.name = nameInput

                    userPreferencesViewModel.changeUser(
                        user = user,
                        fileUri = fileImage,
                        newPassword = passwordInput,
                        oldPassword = oldPasswordInput
                    )

                }
            )
        }
        if (isClickSave) {
            DialogBoxLoading()
        }

    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    CMU_07_8200398_8200591_8200592Theme {
        SettingsScreen(
            onComposing = {},
        )
    }
}