package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.houses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SmartHouseViewModels
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.AppBarState
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme

data class NavigationsHouses(
    val onNagivateToHouse: (String) -> Unit,
    val onNavigateToAddHouses: () -> Unit
)

@Composable
fun HousesScreen(
    onComposing: (AppBarState) -> Unit,
    navigations: NavigationsHouses
) {
    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                topBar = true,
                transparentBackground = true,
                drawerMenu = true,
                navigateToBackScreen = false
            )
        )
    }

    val smartHouseViewModels: SmartHouseViewModels = viewModel()
    val houses = smartHouseViewModels.getAllHouses().observeAsState()

    BoxWithConstraints {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentPadding = PaddingValues(
                start = 30.dp,
                top = 30.dp,
                end = 30.dp,
                bottom = 30.dp
            ),
            content = {
                houses.value?.let {
                    items(it) { house ->
                        val consume = smartHouseViewModels.getHouseConsume(house.id).observeAsState()
                        var consumeValue = 0.00
                        consume.value?.let { consume ->
                            consumeValue = consume
                        }
                        MyHouseCard(
                            onNagivateToHouse = {
                                navigations.onNagivateToHouse(house.id)
                            },
                            house = house, consume = consumeValue)
                    }
                }
            }
        )
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    horizontal = 20.dp,
                    vertical = 30.dp
                )
                .size(60.dp),
            onClick = {
                navigations.onNavigateToAddHouses()
            }
        ){
            Icon(Icons.Filled.AddHome, "Add Home")
        }
    }
}

@Preview
@Composable
fun PreviewHousesScreen() {
    CMU_07_8200398_8200591_8200592Theme {
        HousesScreen(
            onComposing = {},
            navigations = NavigationsHouses(
                onNagivateToHouse = {},
                onNavigateToAddHouses = {}
            )
        )
    }
}