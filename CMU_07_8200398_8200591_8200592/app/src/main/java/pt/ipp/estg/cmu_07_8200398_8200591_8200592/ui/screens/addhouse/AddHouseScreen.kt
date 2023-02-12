package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.addhouse

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SmartHouseViewModels
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.House
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.User
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.AppBarState
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.*
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.*

@Composable
fun AddHouseScreen(
    onComposing: (AppBarState) -> Unit,
    onNavigateToBack: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                topBar = true,
                transparentBackground = true,
                drawerMenu = true,
                navigateToBackScreen = true,
            )
        )
    }

    val context = LocalContext.current
    val smartHouseViewModels: SmartHouseViewModels = viewModel()

    var houseInput by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf("") }
    var addressInput by remember { mutableStateOf("") }
    var fileImage by remember {
        mutableStateOf<Uri?>(null)
    }

    var isHouseInputError by remember { mutableStateOf(false) }
    var isAddressInputError by remember { mutableStateOf(false) }
    var isUserError by remember { mutableStateOf<Int?>(null) }

    val houseUserList = remember { mutableStateListOf<User>() }

    var isClickedSave by remember { mutableStateOf(false) }
    val isLoading = smartHouseViewModels.isLoading.observeAsState()

    var verifyOnline by remember { mutableStateOf(false) }
    val userList = smartHouseViewModels.getUsers().observeAsState()

    var email by remember { mutableStateOf("") }

    if (email != "") {
        val newUser = smartHouseViewModels.getUser(email).observeAsState()
        newUser.value?.let {
            houseUserList.add(it)
            email = ""
        }
    }

    LaunchedEffect(key1 = isLoading.value) {
        if (isClickedSave && isLoading.value == false) {
            smartHouseViewModels.resetLoading()
            isClickedSave = false
            onNavigateToBack()
        } else if (verifyOnline && isLoading.value == false) {
            smartHouseViewModels.resetLoading()
            isUserError = R.string.email_not_registered

        }
    }

    LaunchedEffect(key1 = userList.value) {
        if (verifyOnline) {
            email = userInput
            userInput = ""
            isUserError = null
            verifyOnline = false
        }
    }

    var useCurrentlyLocation by remember { mutableStateOf(false) }
    var isPermittedReadLocation by remember { mutableStateOf(false) }

    SetPermissionsLocation(
        setIsPermitted = { isPermitted ->
            isPermittedReadLocation = isPermitted
        }
    )

    if (isPermittedReadLocation) {
        ReadLocation(
            setLatitudeAndLongitude = { latitude, longitude ->
                addressInput = getCoordinatesToAddress(context, latitude, longitude)
            },
            readLocation = useCurrentlyLocation
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.add_house),
                fontSize = 25.sp,
                color = MaterialTheme.myCustomColors.subtitle
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        MyImagePicker(
            setUri = { uri ->
                fileImage = uri
            }
        )
        MyTextField(
            value = houseInput,
            onValueChange = {
                houseInput = it
            },
            placeholder = stringResource(id = R.string.house_name),
            trailingIcon = {
                Icon(
                    Icons.Filled.Home,
                    modifier = Modifier.size(20.dp),
                    contentDescription = "House Icon",
                    tint = MaterialTheme.myCustomColors.textFieldIcon
                )
            },
            keyboardType = KeyboardType.Text,
        )
        if (isHouseInputError) {
            MyErrorMessage(text = stringResource(id = R.string.invalid_name_house))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyTextField(
                value = userInput,
                onValueChange = {
                    userInput = it
                },
                placeholder = stringResource(id = R.string.user_email),
                trailingIcon = {
                    Icon(
                        Icons.Filled.Person,
                        modifier = Modifier.size(20.dp),
                        contentDescription = "User Icon",
                        tint = MaterialTheme.myCustomColors.textFieldIcon
                    )
                },
                keyboardType = KeyboardType.Text,
                modifier = Modifier
                    .weight(0.7f)
                    .padding(end = 10.dp)
            )
            MyButton(
                text = stringResource(id = R.string.add_user),
                fontSize = 16.sp,
                onClick = {
                    isUserError = null

                    if (!emailValidation(userInput)) {
                        isUserError = R.string.invalid_email
                        return@MyButton
                    }

                    //Verificar se o utilizador existe no Room
                    if (!checkUserExists(userInput, userList.value)) {
                        verifyOnline = true

                        smartHouseViewModels.getUserOnline(userInput)

                        return@MyButton
                    }

                    //Verificar se o utilizador já está inserido na casa
                    if (checkUserExists(userInput, houseUserList)) {
                        isUserError = R.string.user_already_added
                        return@MyButton
                    }

                    email = userInput
                    userInput = ""
                },
                modifierButton = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .weight(0.3f)
            )
        }
        isUserError?.let { id ->
            MyErrorMessage(text = stringResource(id = id))
        }

        if (houseUserList.isNotEmpty()) {
            Column(modifier = Modifier.padding(5.dp)) {
                Text(
                    text = stringResource(id = R.string.user_list),
                    fontSize = 20.sp,
                    color = MaterialTheme.myCustomColors.subtitle
                )
                for (user in houseUserList) {
                    AvatarAndEmail(
                        user = user,
                        removeUser = {
                            houseUserList.remove(user)
                        },
                        context = context,
                    )
                }
            }
        }

        MyTextField(
            value = addressInput,
            onValueChange = {
                addressInput = it
            },
            placeholder = stringResource(id = R.string.address),
            trailingIcon = {
                Icon(
                    Icons.Filled.LocationOn,
                    modifier = Modifier.size(20.dp),
                    contentDescription = stringResource(id = R.string.address),
                    tint = MaterialTheme.myCustomColors.textFieldIcon
                )
            },
            keyboardType = KeyboardType.Text,
        )

        if (isAddressInputError) {
            MyErrorMessage(text = stringResource(id = R.string.invalid_address))
        }

        if (isPermittedReadLocation) {
            if (useCurrentlyLocation) {
                MyButton(
                    text = stringResource(id = R.string.not_use_phone_location),
                    onClick = {
                        useCurrentlyLocation = false
                    }
                )
            } else {
                MyButton(
                    text = stringResource(id = R.string.use_phone_location),
                    onClick = {
                        useCurrentlyLocation = true
                    }
                )
            }
        }

        MyButton(
            text = stringResource(id = R.string.save),
            onClick = {
                isClickedSave = true
                isHouseInputError = false
                isAddressInputError = false

                val addressLatLog = getAddressToCoordinates(context, addressInput)

                if (!houseAndDivisionNameVaidation(houseInput)) {
                    isHouseInputError = true
                }

                if (addressLatLog == null) {
                    isAddressInputError = true
                } else {
                    if (!latitudeValidation(addressLatLog.latitude)) {
                        isAddressInputError = true
                    }

                    if (!longitudeValidation(addressLatLog.longitude)) {
                        isAddressInputError = true
                    }
                }

                if (isHouseInputError || isAddressInputError) {
                    isClickedSave =  false
                    return@MyButton
                }

                smartHouseViewModels.addHouse(
                    house = House(
                        id = "",
                        name = houseInput,
                        photo = "",
                        latitude = addressLatLog!!.latitude,
                        longitude = addressLatLog.longitude,
                    ),
                    fileUri = fileImage,
                    userList = houseUserList
                )
            }
        )

        if (isLoading.value == true || isClickedSave) {
            DialogBoxLoading()
        }
    }
}

@Preview
@Composable
fun PreviewAddHouseScreen() {
    CMU_07_8200398_8200591_8200592Theme {
        AddHouseScreen(
            onComposing = {},
            onNavigateToBack = {},
        )
    }
}