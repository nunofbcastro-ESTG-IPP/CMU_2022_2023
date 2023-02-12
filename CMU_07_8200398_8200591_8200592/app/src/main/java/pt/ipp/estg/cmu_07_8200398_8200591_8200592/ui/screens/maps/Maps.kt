package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.maps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.AppBarState

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.ProgressIndicatorLoading
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SmartHouseViewModels

@Composable
fun Maps(
    onComposing: (AppBarState) -> Unit,
    idHouse: String,
) {
    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                topBar = true,
                transparentBackground = true,
                drawerMenu = true,
                navigateToBackScreen = true,
                content = {
                    Text(text = "")
                }
            )
        )
    }

    val smartHouseViewModels: SmartHouseViewModels = viewModel()
    val house = smartHouseViewModels.getHouse(idHouse).observeAsState()

    if(house.value != null){
        Maps(
            latitude = house.value!!.latitude,
            longitude = house.value!!.longitude,
        )
    }else{
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressIndicatorLoading()
        }
    }

}

@Composable
private fun Maps(
    latitude: Double,
    longitude: Double,
){
    val houseLocation = LatLng(
        latitude,
        longitude
    )
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(houseLocation, 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                LocalContext.current,
                if (MaterialTheme.colors.isLight) R.raw.map_style_white_mode
                else R.raw.map_style_dark_mode
            )
        )
    ) {
        Marker(
            state = MarkerState(position = houseLocation),
            title = stringResource(id = R.string.my_house)
        )
    }
}

@Preview
@Composable
fun PreviewScreenMaps() {
    CMU_07_8200398_8200591_8200592Theme {
        Maps(
            onComposing = {  },
            idHouse = "S1A9eHAvLBaDjSMSfn72",
        )
    }
}

@Preview
@Composable
fun PreviewMaps() {
    CMU_07_8200398_8200591_8200592Theme {
        Maps(
            latitude = 41.3667788,
            longitude = -8.1928864,
        )
    }
}