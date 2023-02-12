package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.division_details

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SmartHouseViewModels
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.AppBarState
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyButtonAndText
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.getId

data class NavigationsSensor(
    val onNavigateToEditDivision: (String, String) -> Unit,
    val onNavigateToRemoveDivisionBack: () -> Unit
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DivisionDetailsScreen(
    onComposing: (AppBarState) -> Unit,
    idHouse: String,
    idDivision: String,
    navigations: NavigationsSensor
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

    val smartHouseViewModels: SmartHouseViewModels = viewModel()
    val division = smartHouseViewModels.getDivisionById(idDivision = idDivision, idHouse = idHouse)
        .observeAsState()
    val divisionTypeSensors =
        smartHouseViewModels.getDivisionTypeSensor(idHouse = idHouse, idDivision = idDivision)
            .observeAsState()
    var content by remember { mutableStateOf("") }
    var dropDownOpen by remember { mutableStateOf(false) }
    var dialogAddOpen by remember { mutableStateOf(false) }
    var dialogRemoveOpen by remember { mutableStateOf(false) }
    var dialogDeleteDivisionOpen by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val devices = smartHouseViewModels.getTypesSensors().observeAsState()
    var activeType by remember { mutableStateOf(-1) }

    divisionTypeSensors.value?.let {
        if (it.isNotEmpty()) {
            content = it.first().name
            activeType = it.first().id
        } else {
            content = ""
            activeType = -1
        }
    }

    division.value?.let {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 15.dp, horizontal = 35.dp)
            ) {
                Text(text = it.name, color = Color.Gray, fontSize = 25.sp)

                Spacer(modifier = Modifier.height(10.dp))


                Row(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .horizontalScroll(state = scrollState)
                ) {
                    divisionTypeSensors.value?.let {
                        for (typeSensor in it) {
                            val icon: ImageVector? = remember(typeSensor.icon) {
                                try {
                                    val cl =
                                        Class.forName("androidx.compose.material.icons.filled.${typeSensor.icon}Kt")
                                    val method = cl.declaredMethods.first()
                                    method.invoke(null, Icons.Filled) as ImageVector
                                } catch (_: Throwable) {
                                    null
                                }
                            }
                            if (icon != null) {
                                MyButtonAndText(
                                    text = stringResource(
                                        id = R.string::class.java.getId(
                                            typeSensor.name
                                        )
                                    ),
                                    icon = icon,
                                    background = verifySelectedBackground(
                                        content,
                                        typeSensor.name
                                    ),
                                    iconColor = verifySelectedIcon(content, typeSensor.name),
                                    onClick = {
                                        content = typeSensor.name
                                        activeType = typeSensor.id
                                    }
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                            }

                        }
                    }
                    ExposedDropdownMenuBox(
                        expanded = dropDownOpen,
                        onExpandedChange = {
                            dropDownOpen = !dropDownOpen
                        }
                    ) {
                        MyButtonAndText(
                            icon = Icons.Filled.Settings,
                            background = Color.White,
                            iconColor = Color.Black,
                            onClick = { dropDownOpen = true })
                        ExposedDropdownMenu(
                            expanded = dropDownOpen,
                            onDismissRequest = {
                                dropDownOpen = false
                            },
                            modifier = Modifier.width(100.dp)
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    dropDownOpen = false
                                    navigations.onNavigateToEditDivision(idHouse, idDivision)
                                }
                            ) {
                                Text(text = stringResource(id = R.string.edit_division))
                            }
                            Divider(
                                modifier = Modifier.padding(
                                    horizontal = 10.dp
                                ), color = Color.LightGray, thickness = 1.dp
                            )
                            DropdownMenuItem(
                                onClick = {
                                    dropDownOpen = false
                                    dialogDeleteDivisionOpen = true
                                }
                            ) {
                                Text(text = stringResource(id = R.string.title_remove_division))
                            }
                            Divider(
                                modifier = Modifier.padding(
                                    horizontal = 10.dp
                                ), color = Color.LightGray, thickness = 1.dp
                            )
                            DropdownMenuItem(
                                onClick = {
                                    dropDownOpen = false
                                    dialogAddOpen = true
                                }
                            ) {
                                Text(text = stringResource(id = R.string.title_add_sensor))
                            }
                            Divider(
                                modifier = Modifier.padding(
                                    horizontal = 10.dp
                                ), color = Color.LightGray, thickness = 1.dp
                            )
                            DropdownMenuItem(
                                onClick = {
                                    dropDownOpen = false
                                    dialogRemoveOpen = true
                                }
                            ) {
                                Text(text = stringResource(id = R.string.title_remove_sensor))
                            }
                        }
                    }
                }


                when (content) {
                    "Light" -> {
                        LightComponent(
                            idHouse = idHouse,
                            idDivision = idDivision,
                            idTypeSensor = activeType
                        )
                    }
                    "Plug" -> {
                        PlugComponent(
                            idHouse = idHouse,
                            idDivision = idDivision,
                            idTypeSensor = activeType
                        )
                    }
                    "Blinds" -> {
                        BlindComponent(
                            idHouse = idHouse,
                            idDivision = idDivision,
                            idTypeSensor = activeType
                        )
                    }
                }

                if (dialogDeleteDivisionOpen) {
                    RemoveDivisionDialog(
                        title = stringResource(id = R.string.title_remove_division),
                        idHouse = idHouse,
                        idDivision = idDivision,
                        setDialog = { dialogDeleteDivisionOpen = false }
                    ) {
                        navigations.onNavigateToRemoveDivisionBack()
                    }
                }
                devices.value?.let {
                    if (dialogAddOpen) {
                        AddSensorDialog(
                            title = stringResource(id = R.string.title_add_sensor),
                            deviceList = it,
                            buttonText = stringResource(id = R.string.add),
                            idHouse = idHouse,
                            idDivision = idDivision
                        ) {
                            dialogAddOpen = false
                        }
                    } else if (dialogRemoveOpen) {
                        RemoveSensorDialog(
                            title = stringResource(id = R.string.title_remove_sensor),
                            buttonText = stringResource(id = R.string.remove),
                            idHouse = idHouse,
                            idDivision = idDivision
                        ) {
                            dialogRemoveOpen = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun verifySelectedBackground(
    text: String,
    textVerify: String
): Color {
    if (text == textVerify) {
        return MaterialTheme.colors.secondary
    }
    return Color.White
}

@Composable
private fun verifySelectedIcon(
    text: String,
    textVerify:
    String
): Color {
    if (text == textVerify) {
        return MaterialTheme.colors.onSecondary
    }
    return Color.Black
}

@Preview
@Composable
fun PreviewDivisionDetailsScreen() {
    CMU_07_8200398_8200591_8200592Theme {
        DivisionDetailsScreen(
            onComposing = {},
            idHouse = "SMZapyiMvR1xRnl6Q8Ff",
            idDivision = "61NVojOOCmlJj9hwk1zo",
            navigations = NavigationsSensor(
                onNavigateToEditDivision = { idHouse, idDivision ->
                },
                onNavigateToRemoveDivisionBack = {}
            )
        )
    }
}