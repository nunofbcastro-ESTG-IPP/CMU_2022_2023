package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.division_details

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.dmoral.toasty.Toasty
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SmartHouseViewModels
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.PlugSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.Sensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.DialogBoxLoading
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyButtonAndText
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyEnergyConsumption
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme

@Composable
fun PlugComponent(
    idHouse: String,
    idDivision: String,
    idTypeSensor: Int
) {
    var isUpdatedOnline by remember { mutableStateOf(false) }

    val smartHouseViewModels: SmartHouseViewModels = viewModel()
    val groups = smartHouseViewModels.getGroups(idHouse, idDivision, idTypeSensor).observeAsState()

    groups.value?.let { groupList ->
        val groupIdList = mutableListOf<String>()

        for (group in groupList) {
            groupIdList.add(group.id)
        }
        val sensorsAndPlugSensors = smartHouseViewModels.getSensorsAndPlugSensorsByGroup(
            idHouse = idHouse,
            idDivision = idDivision,
            groupList = groupIdList
        ).observeAsState()

        if (!isUpdatedOnline
            && sensorsAndPlugSensors.value != null
        ) {
            isUpdatedOnline = true
            sensorsAndPlugSensors.value?.let { sensorList ->
                sensorList.map { sensor ->
                    smartHouseViewModels.updatePlugSensorOnline(
                        sensor = sensor.sensor!!,
                        plugSensor = sensor.plugSensor!!
                    )
                }
            }
        }

        val sensors = mutableListOf<Sensor>()
        val plugSensors = mutableListOf<PlugSensor>()
        sensorsAndPlugSensors.value?.let { sensorList ->
            sensorList.map { sensor ->
                sensors.add(sensor.sensor!!)
                plugSensors.add(sensor.plugSensor!!)
            }

            val groupStates = groupList.map { group ->
                val isActive = remember { mutableStateOf(true) }
                ActiveGroups(group, isActive.value) { isActive.value = it }
            }
            PlugComponent(
                groupList = groupStates,
                sensors = sensors,
                plugSensors = plugSensors,
                smartHouseViewModels = smartHouseViewModels
            )
        }
    }
}

@Composable
private fun PlugComponent(
    groupList: List<ActiveGroups>,
    sensors: List<Sensor>,
    plugSensors: List<PlugSensor>,
    smartHouseViewModels: SmartHouseViewModels,
) {
    val messageError = stringResource(id = R.string.error_accessing_sensor)
    val context = LocalContext.current
    val loading = smartHouseViewModels.networkAndRoomResult.observeAsState()
    var dialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = loading.value) {
        loading.value!!.sucess.let { success ->
            if (success == false) {
                Toasty.error(
                    context,
                    messageError,
                    Toast.LENGTH_SHORT,
                    true
                ).show()
            }
        }

        loading.value!!.loading.let { loading ->
            if (loading == false) {
                smartHouseViewModels.resetNetworkAndRoomResult()
            }
        }
    }

    var plugState by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = plugSensors) {
        if (plugSensors.isNotEmpty()) {
            plugState = plugSensors.first().status
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .size(100.dp),
            shape = CircleShape,
            border = BorderStroke(4.dp, Color.DarkGray),
            colors = if (plugState) ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
            else ButtonDefaults.buttonColors(Color.LightGray),
            onClick = {
                plugState = !plugState

                for (i in sensors.indices) {
                    groupList.forEach { group ->
                        if (group.state && group.group.id == sensors[i].idGroup) {
                            smartHouseViewModels.turnPlug(
                                sensor = sensors[i],
                                plugSensor = plugSensors[i],
                                status = plugState,
                            )
                        }
                    }
                }
            }) {
            if (plugState) {
                Icon(
                    Icons.Filled.Power,
                    modifier = Modifier.fillMaxSize(0.85f),
                    tint = Color.White,
                    contentDescription = "Power"
                )
            } else {
                Icon(
                    Icons.Filled.PowerOff,
                    modifier = Modifier.fillMaxSize(0.85f),
                    tint = Color.Black,
                    contentDescription = "Power"
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MyEnergyConsumption(consume = getConsume(sensors), 75.dp)
            MyButtonAndText(
                icon = Icons.Filled.Tune,
                background = Color.White,
                iconColor = Color.Black,
                onClick = { dialogOpen = true })
        }

        if (dialogOpen) {
            GroupDialog(groupList = groupList) {
                dialogOpen = false
            }
        }

        if (loading.value!!.loading == true) {
            DialogBoxLoading()
        }
    }
}

private fun getConsume(sensors: List<Sensor>): Double {
    var consume = 0.0

    sensors.forEach { sensor ->
        consume += sensor.consumption
    }
    return consume
}

@Preview
@Composable
fun PreviewPlugComponent() {
    CMU_07_8200398_8200591_8200592Theme {
        PlugComponent(
            idHouse = "SMZapyiMvR1xRnl6Q8Ff",
            idDivision = "61NVojOOCmlJj9hwk1zo",
            idTypeSensor = 1,
        )
    }
}

@Preview
@Composable
fun PreviewPrivatePlugComponent() {
    val smartHouseViewModels: SmartHouseViewModels = viewModel()
    CMU_07_8200398_8200591_8200592Theme {
        PlugComponent(
            groupList = listOf<ActiveGroups>(),
            sensors = listOf<Sensor>(),
            plugSensors = listOf<PlugSensor>(),
            smartHouseViewModels = smartHouseViewModels,
        )
    }
}