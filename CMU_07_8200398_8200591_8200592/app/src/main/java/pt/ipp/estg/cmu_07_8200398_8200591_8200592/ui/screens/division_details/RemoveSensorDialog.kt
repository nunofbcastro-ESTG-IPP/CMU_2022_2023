package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.division_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.Sensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.TypeSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.DialogBoxLoading
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyButton
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyTextField
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.getId

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RemoveSensorDialog(
    title: String,
    buttonText: String,
    idHouse: String,
    idDivision: String,
    setDialog: () -> Unit
) {
    val smartHouseViewModels: SmartHouseViewModels = viewModel()
    val divisionSensors =
        smartHouseViewModels.getDivisionSensors(idHouse = idHouse, idDivision = idDivision)
            .observeAsState()
    val divisionTypeSensors =
        smartHouseViewModels.getDivisionTypeSensor(idHouse = idHouse, idDivision = idDivision)
            .observeAsState()

    var expanded by remember { mutableStateOf(false) }
    var expanded1 by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<TypeSensor?>(null) }
    var selectedSensor by remember { mutableStateOf<Sensor?>(null) }

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
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = title, fontSize = 20.sp)

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
                                expanded = expanded
                            )
                        },
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        divisionTypeSensors.value?.forEach { selectionOption ->
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
                if (selectedOption == null) {
                    MyTextField(
                        value = "",
                        onValueChange = { },
                        placeholder = stringResource(id = R.string.device_ip),
                        readOnly = true,
                    )
                } else {
                    ExposedDropdownMenuBox(
                        expanded = expanded1,
                        onExpandedChange = {
                            expanded1 = !expanded1
                        }
                    ) {
                        MyTextField(
                            value = selectedSensor?.ip ?: "",
                            onValueChange = { },
                            placeholder = stringResource(id = R.string.device_ip),
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded1
                                )
                            },
                        )
                        ExposedDropdownMenu(
                            expanded = expanded1,
                            onDismissRequest = {
                                expanded1 = false
                            }
                        ) {
                            divisionSensors.value?.let {
                                it.forEach { selectionSensor ->
                                    if (selectedOption != null && selectionSensor.idTypeSensor == selectedOption?.id) {
                                        DropdownMenuItem(
                                            onClick = {
                                                selectedSensor = selectionSensor
                                                expanded1 = false
                                            }
                                        ) {
                                            Text(text = selectionSensor.ip)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(0.dp, 5.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    MyButton(
                        text = buttonText,
                        onClick = {
                            selectedOption?.let {
                                selectedSensor?.let { sensor ->
                                    smartHouseViewModels.removeSensor(
                                        sensor
                                    )
                                    isClickedSave = true
                                }
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

@Preview
@Composable
fun PreviewRemoveSensorDialog() {
    CMU_07_8200398_8200591_8200592Theme {
        RemoveSensorDialog(
            title = stringResource(id = R.string.title_remove_sensor),
            buttonText = stringResource(id = R.string.remove),
            idHouse = "SMZapyiMvR1xRnl6Q8Ff",
            idDivision = "61NVojOOCmlJj9hwk1zo",
            setDialog = {},
        )
    }
}