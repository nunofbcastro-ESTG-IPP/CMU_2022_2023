package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.division_details

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker
import es.dmoral.toasty.Toasty
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SmartHouseViewModels
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.Group
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.LightSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.Sensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyEnergyConsumption
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.debounce
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.RGBB
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.DialogBoxLoading
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyButtonAndText
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.getHsvColorToRGBB
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.getRGBBToColor

data class ActiveGroups(
    val group: Group,
    var state: Boolean,
    var onStateChange: (Boolean) -> Unit = {}
)

@Composable
fun LightComponent(
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
        val sensorsAndLightSensors = smartHouseViewModels.getSensorsAndLightSensorsByGroup(
            idHouse = idHouse,
            idDivision = idDivision,
            groupList = groupIdList
        ).observeAsState()

        if (!isUpdatedOnline
            && sensorsAndLightSensors.value != null
        ) {
            isUpdatedOnline = true
            sensorsAndLightSensors.value?.let { sensorList ->
                sensorList.map { sensor ->
                    smartHouseViewModels.updateLightSensorOnline(
                        sensor = sensor.sensor!!,
                        lightSensor = sensor.lightSensor!!
                    )
                }
            }
        }

        val sensors = mutableListOf<Sensor>()
        val lightSensors = mutableListOf<LightSensor>()
        sensorsAndLightSensors.value?.let { sensorList ->
            sensorList.map { sensor ->
                sensors.add(sensor.sensor!!)
                lightSensors.add(sensor.lightSensor!!)
            }

            val groupStates = groupList.map { group ->
                val isActive = remember { mutableStateOf(true) }
                ActiveGroups(group, isActive.value) { isActive.value = it }
            }
            LightComponent(
                groupList = groupStates,
                sensors = sensors,
                lightSensors = lightSensors,
                smartHouseViewModels = smartHouseViewModels
            )
        }
    }
}

@Composable
private fun LightComponent(
    groupList: List<ActiveGroups>,
    sensors: List<Sensor>,
    lightSensors: List<LightSensor>,
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

    var currentColor by remember {
        mutableStateOf(
            getRGBBToColor(
                if(lightSensors.isNotEmpty()){
                    RGBB(
                        Red = lightSensors.first().red,
                        Green = lightSensors.first().green,
                        Blue = lightSensors.first().blue,
                        Brightness = lightSensors.first().brightness
                    )
                }else{
                    RGBB(
                        Red = 0,
                        Green = 0,
                        Blue = 0,
                        Brightness = 0
                    )
                }
            )
        )
    }
    val harmonyMode by remember {
        mutableStateOf(ColorHarmonyMode.NONE)
    }
    val colorChange: (HsvColor) -> Unit = debounce(
        destinationFunction = { color ->
            val rgbb = getHsvColorToRGBB(
                color
            )

            for (i in sensors.indices) {
                groupList.forEach { group ->
                    if (group.state && group.group.id == sensors[i].idGroup) {
                        smartHouseViewModels.changeColorLightSensor(
                            sensor = sensors[i],
                            lightSensor = lightSensors[i],
                            rgbb = rgbb,
                        )
                    }
                }
            }

            currentColor = color.toColor()
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HarmonyColorPicker(
            harmonyMode = harmonyMode,
            modifier = Modifier.height(getSizeColorPicker()),
            onColorChanged = { color: HsvColor ->
                colorChange(color)
            },
            color = currentColor,
        )

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

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun getSizeColorPicker(): Dp {
    val sizes: Dp

    val activity = LocalContext.current as Activity
    val windowSize = calculateWindowSizeClass(activity)

    sizes = if (windowSize.widthSizeClass < WindowWidthSizeClass.Medium) {
        300.dp
    } else {
        450.dp
    }
    return sizes
}

@Preview
@Composable
fun PreviewLightComponent() {
    CMU_07_8200398_8200591_8200592Theme {
        LightComponent(
            idHouse = "SMZapyiMvR1xRnl6Q8Ff",
            idDivision = "61NVojOOCmlJj9hwk1zo",
            idTypeSensor = 0,
        )
    }
}

@Preview
@Composable
fun PreviewPrivateLightComponent() {
    val smartHouseViewModels: SmartHouseViewModels = viewModel()
    CMU_07_8200398_8200591_8200592Theme {
        LightComponent(
            groupList = listOf<ActiveGroups>(),
            sensors = listOf<Sensor>(),
            lightSensors = listOf<LightSensor>(),
            smartHouseViewModels = smartHouseViewModels,
        )
    }
}
