package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.division_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SmartHouseViewModels
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.*
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.DialogBoxLoading
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyButton
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyErrorMessage
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyTextField
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.getId
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.ipValidation

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddSensorDialog(
    title: String,
    deviceList: List<TypeSensor>,
    buttonText: String,
    idHouse: String,
    idDivision: String,
    setDialog: () -> Unit
) {
    val smartHouseViewModels: SmartHouseViewModels = viewModel()
    var deviceIP by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<TypeSensor?>(null) }
    var isError by remember { mutableStateOf(false) }
    var groupOption by remember { mutableStateOf(0) }
    var groupId by remember { mutableStateOf("") }
    var expandedGroup by remember { mutableStateOf(false) }
    var isErrorGroup by remember { mutableStateOf(false) }
    var isErrorGroupName by remember { mutableStateOf(false) }

    var isClickedSave by remember { mutableStateOf(false) }
    val isLoading = smartHouseViewModels.isLoading.observeAsState()


    LaunchedEffect(key1 = isLoading.value) {
        if (isClickedSave && isLoading.value == false) {
            setDialog()
        }
    }

    Dialog(
        onDismissRequest = { setDialog() },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(370.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp)
                    .clip(RoundedCornerShape(size = 15.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = title, fontSize = 20.sp)

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1f)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {
                        var selected = ""
                        selectedOption?.name?.let { name ->
                            selected = stringResource(
                                id = R.string::class.java.getId(
                                    name
                                )
                            )
                        }
                        MyTextField(
                            value = selected,
                            onValueChange = { },
                            placeholder = stringResource(id = R.string.type_of_device),
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded,
                                )
                            },
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                            deviceList.forEach { selectionOption ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedOption = selectionOption
                                        expanded = false
                                    }
                                ) {
                                    Text(
                                        text = stringResource(
                                            id = R.string::class.java.getId(
                                                selectionOption.name
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
                    MyTextField(
                        value = deviceIP,
                        onValueChange = {
                            deviceIP = it
                        },
                        placeholder = stringResource(id = R.string.device_ip)
                    )
                    if (isError) {
                        MyErrorMessage(text = stringResource(id = R.string.invalid_ip))
                    }

                    selectedOption?.let {
                        val groupList = smartHouseViewModels.getGroups(
                            idHouse = idHouse,
                            idDivision = idDivision,
                            idTypeSensor = it.id
                        ).observeAsState()

                        ExposedDropdownMenuBox(
                            expanded = expandedGroup,
                            onExpandedChange = {
                                expandedGroup = !expandedGroup
                            }
                        ) {
                            MyTextField(
                                value = when (groupOption) {
                                    0 -> stringResource(id = R.string.choose_an_option)
                                    1 -> stringResource(id = R.string.choose_existing_group)
                                    else -> stringResource(id = R.string.add_new_group)
                                },
                                onValueChange = { },
                                placeholder = stringResource(id = R.string.existing_or_new),
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = expandedGroup,
                                    )
                                },
                            )
                            ExposedDropdownMenu(
                                expanded = expandedGroup,
                                onDismissRequest = {
                                    expandedGroup = false
                                }
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        groupOption = 1
                                        expandedGroup = false
                                    }
                                ) {
                                    Text(text = stringResource(id = R.string.choose_existing_group))
                                }
                                DropdownMenuItem(
                                    onClick = {
                                        groupOption = 2
                                        expandedGroup = false
                                    }
                                ) {
                                    Text(text = stringResource(id = R.string.add_new_group))
                                }
                            }
                        }
                        if (isErrorGroup) {
                            MyErrorMessage(text = stringResource(id = R.string.choose_an_existing_group_or_add_a_new_one))
                        }
                        if (groupOption > 0) {
                            groupList.value?.let { list ->
                                GroupInfo(
                                    id = groupId,
                                    type = groupOption,
                                    groupList = list,
                                    setId = { name -> groupId = name },
                                    setError = { boolean -> isErrorGroupName = boolean }
                                )
                            }
                        }
                        if (isErrorGroupName) {
                            MyErrorMessage(text = stringResource(id = R.string.group_name_already_exists))
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(0.dp, 5.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    MyButton(
                        text = buttonText,
                        onClick = {
                            selectedOption?.let {
                                if (!ipValidation(deviceIP)) {
                                    isError = true
                                    return@MyButton
                                }

                                if (groupId == "") {
                                    isErrorGroup = true
                                    return@MyButton
                                }

                                if (isErrorGroupName) {
                                    return@MyButton
                                }


                                var group: Group? = null
                                if (groupOption == 2) {
                                    group = Group(
                                        id = "",
                                        idHouse = idHouse,
                                        idDivision = idDivision,
                                        idTypeSensor = it.id,
                                        name = groupId
                                    )
                                }

                                val sensor = Sensor(
                                    id = "",
                                    ip = deviceIP,
                                    idHouse = idHouse,
                                    idDivision = idDivision,
                                    idTypeSensor = it.id,
                                    consumption = 0.0,
                                    idGroup = groupId
                                )
                                when (it.name) {
                                    "Light" -> {
                                        smartHouseViewModels.addLightSensor(
                                            group,
                                            sensor,
                                            LightSensor(
                                                idSensor = "",
                                                red = 0,
                                                green = 0,
                                                blue = 0,
                                                brightness = 100,
                                            )
                                        )
                                    }
                                    "Plug" -> {
                                        smartHouseViewModels.addPlugSensor(
                                            group,
                                            sensor,
                                            PlugSensor(
                                                idSensor = "",
                                                status = false
                                            )
                                        )
                                    }
                                    "Blinds" -> {
                                        smartHouseViewModels.addBlindSensor(
                                            group,
                                            sensor,
                                            BlindSensor(
                                                idSensor = "",
                                                position = 100
                                            )
                                        )
                                    }
                                }
                                isClickedSave = true
                            }

                        },
                        modifierButton = Modifier
                            .weight(1f)
                            .padding(5.dp)
                            .clip(RoundedCornerShape(15.dp))
                    )
                    Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                    MyButton(
                        text = stringResource(id = R.string.cancel),
                        onClick = { setDialog() },
                        modifierButton = Modifier
                            .weight(1f)
                            .padding(5.dp)
                            .clip(RoundedCornerShape(15.dp))
                    )
                }
            }
        }

        if (isLoading.value == true) {
            DialogBoxLoading()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupInfo(
    id: String,
    type: Int,
    groupList: List<Group>,
    setId: (String) -> Unit,
    setError: (Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = type) {
        setId("")
    }

    if (type == 1) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            MyTextField(
                value = if (id == "") stringResource(id = R.string.choose_group) else getNameById(
                    id = id,
                    groupList = groupList
                ),
                onValueChange = { },
                placeholder = stringResource(id = R.string.group),
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded,
                    )
                },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                groupList.forEach { group ->
                    DropdownMenuItem(
                        onClick = {
                            setId(group.id)
                            expanded = false
                        }
                    ) {
                        Text(text = group.name)
                    }
                }
            }
        }
    } else {
        MyTextField(
            value = id,
            onValueChange = {
                setId(it)
                groupList.forEach { group ->
                    if (group.name == it) {
                        setError(true)
                        return@MyTextField
                    }
                }
                setError(false)
            },
            placeholder = stringResource(id = R.string.group_name)
        )
    }
}

private fun getNameById(
    id: String,
    groupList: List<Group>
): String {
    for (group in groupList) {
        if (group.id == id) {
            return group.name
        }
    }
    return ""
}

@Preview
@Composable
fun PreviewAddSensorDialog() {
    CMU_07_8200398_8200591_8200592Theme {
        AddSensorDialog(
            title = stringResource(id = R.string.title_add_sensor),
            deviceList = listOf<TypeSensor>(
                TypeSensor(
                    id = 0,
                    name = "Light",
                    icon = "EmojiObjects"
                ),
                TypeSensor(
                    id = 1,
                    name = "Plug",
                    icon = "Power"
                ),
                TypeSensor(
                    id = 2,
                    name = "Blinds",
                    icon = "Blinds"
                )
            ),
            buttonText = stringResource(id = R.string.add),
            idHouse = "SMZapyiMvR1xRnl6Q8Ff",
            idDivision = "61NVojOOCmlJj9hwk1zo",
            setDialog = {}
        )
    }
}

@Preview
@Composable
fun PreviewGroupInfo() {
    CMU_07_8200398_8200591_8200592Theme {
        GroupInfo(
            id = "",
            type = 0,
            groupList = listOf(
                Group(
                    id = "vCw9guYbGIOIjlUZQ2LR",
                    idHouse = "SMZapyiMvR1xRnl6Q8Ff",
                    idDivision = "61NVojOOCmlJj9hwk1zo",
                    idTypeSensor = 0,
                    name = "Grupo 1",
                )
            ),
            setId = { id ->

            },
            setError = { isError ->

            }
        )
    }
}