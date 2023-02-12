package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.division_details

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.dmoral.toasty.Toasty
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SmartHouseViewModels
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.BlindSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.Sensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.DialogBoxLoading
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyButtonAndText
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyEnergyConsumption
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.debounce

@Composable
fun BlindComponent(
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
        val sensorsAndBlindSensors = smartHouseViewModels.getSensorsAndBlindSensorsByGroup(
            idHouse = idHouse,
            idDivision = idDivision,
            groupList = groupIdList
        ).observeAsState()

        if (!isUpdatedOnline
            && sensorsAndBlindSensors.value != null
        ) {
            isUpdatedOnline = true
            sensorsAndBlindSensors.value?.let { sensorList ->
                sensorList.map { sensor ->
                    smartHouseViewModels.updateBlindSensorOnline(
                        sensor = sensor.sensor!!,
                        blindSensor = sensor.blindSensor!!
                    )
                }
            }
        }

        val sensors = mutableListOf<Sensor>()
        val blindSensors = mutableListOf<BlindSensor>()
        sensorsAndBlindSensors.value?.let { sensorList ->
            sensorList.map { sensor ->
                sensors.add(sensor.sensor!!)
                blindSensors.add(sensor.blindSensor!!)
            }

            val groupStates = groupList.map { group ->
                val isActive = remember { mutableStateOf(true) }
                ActiveGroups(group, isActive.value) { isActive.value = it }
            }
            BlindComponent(
                groupList = groupStates,
                sensors = sensors,
                blindSensors = blindSensors,
                smartHouseViewModels = smartHouseViewModels
            )
        }
    }
}

@Composable
private fun BlindComponent(
    groupList: List<ActiveGroups>,
    sensors: List<Sensor>,
    blindSensors: List<BlindSensor>,
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

    var sliderPosition by remember { mutableStateOf(1f) }

    LaunchedEffect(key1 = true) {
        sliderPosition = if(blindSensors.isNotEmpty()){
            (blindSensors.first().position / 100.0).toFloat()
        }else{
            1f
        }
    }

    val positionChange: (Float) -> Unit = debounce(
        destinationFunction = { position ->
            for (i in sensors.indices) {
                groupList.forEach { group ->
                    if (group.state && group.group.id == sensors[i].idGroup) {
                        smartHouseViewModels.changePositionBlindSensor(
                            sensor = sensors[i],
                            blindSensor = blindSensors[i],
                            position = (position * 100).toInt(),
                        )
                    }
                }
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight(0.7f)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Image(
                    painter = painterResource(R.drawable.background_blind),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(sliderPosition)
                        .background(Color.Gray)
                ) {}

                Image(
                    painter = painterResource(R.drawable.window),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Slider(
                modifier = Modifier
                    .semantics { contentDescription = "Localized Description" }
                    .graphicsLayer {
                        rotationZ = 270f
                        transformOrigin = TransformOrigin(0f, 0f)
                    }
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(
                            Constraints(
                                minWidth = constraints.minHeight,
                                maxWidth = constraints.maxHeight,
                                minHeight = constraints.minWidth,
                                maxHeight = constraints.maxHeight,
                            )
                        )
                        layout(placeable.height, placeable.width) {
                            placeable.place(-placeable.width, 0)
                        }
                    }
                    .rotate(180f),
                value = sliderPosition,
                onValueChange = { position ->
                    sliderPosition = position
                    positionChange(position)
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colors.primaryVariant,
                    activeTrackColor = MaterialTheme.colors.primary,
                    inactiveTrackColor = MaterialTheme.myCustomColors.textFieldPlaceholder
                )
            )
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
fun PreviewBlindComponent() {
    CMU_07_8200398_8200591_8200592Theme {
        BlindComponent(
            idHouse = "SMZapyiMvR1xRnl6Q8Ff",
            idDivision = "61NVojOOCmlJj9hwk1zo",
            idTypeSensor = 2,
        )
    }
}

@Preview
@Composable
fun PreviewPrivateBlindComponent() {
    CMU_07_8200398_8200591_8200592Theme {
        val smartHouseViewModels: SmartHouseViewModels = viewModel()
        BlindComponent(
            groupList = listOf<ActiveGroups>(),
            sensors = listOf<Sensor>(),
            blindSensors = listOf<BlindSensor>(),
            smartHouseViewModels = smartHouseViewModels,
        )
    }
}