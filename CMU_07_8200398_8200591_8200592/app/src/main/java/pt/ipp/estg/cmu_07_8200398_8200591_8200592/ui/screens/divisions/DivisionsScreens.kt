package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.divisions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SmartHouseViewModels
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.AppBarState
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyButtonAndText
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyEnergyConsumption
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme

data class NavigationsDivision(
    val onNagivateToDivision: (String, String) -> Unit,
    val onNagivateToAddDivision: (String) -> Unit,
    val onNagivateToEditHouse: (String) -> Unit,
    val onNavigateToRemoveHouseBack: () -> Unit,
    val onNavigateToMap: (String) -> Unit
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DivisionsScreens(
    onComposing: (AppBarState) -> Unit,
    idHouse: String,
    navigations: NavigationsDivision
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
    val consume = smartHouseViewModels.getHouseConsume(idHouse).observeAsState()
    var consumeValue = 0.00
    consume.value?.let { it ->
        consumeValue = it
    }
    val divisions = smartHouseViewModels.getHouseDivisions(idHouse).observeAsState()
    var dropDownOpen by remember { mutableStateOf(false) }
    var dialogRemoveOpen by remember { mutableStateOf(false) }

    BoxWithConstraints {
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                MyEnergyConsumption(consume = consumeValue, 75.dp)
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
                                navigations.onNavigateToMap(idHouse)
                            }
                        ) {
                            Text(text = stringResource(id = R.string.see_on_map))
                        }
                        Divider(
                            modifier = Modifier.padding(
                                horizontal = 10.dp
                            ), color = Color.LightGray, thickness = 1.dp
                        )
                        DropdownMenuItem(
                            onClick = {
                                dropDownOpen = false
                                navigations.onNagivateToEditHouse(idHouse)
                            }
                        ) {
                            Text(text = stringResource(id = R.string.title_edit_house))
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
                            Text(text = stringResource(id = R.string.title_remove_house))
                        }
                    }
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentPadding = PaddingValues(
                    start = 35.dp,
                    top = 20.dp,
                    end = 20.dp,
                    bottom = 25.dp
                ),
                content = {
                    divisions.value?.let {
                        items(it) { division ->
                            MyDivisionCard(
                                onNagivateToDivision = {
                                    navigations.onNagivateToDivision(idHouse, division.id)
                                },
                                division = division,
                            )
                        }
                    }
                }
            )

            if (dialogRemoveOpen) {
                RemoveHouseDialog(
                    title = stringResource(id = R.string.title_remove_house),
                    idHouse = idHouse,
                    setDialog = { dialogRemoveOpen = false }
                ) {
                    navigations.onNavigateToRemoveHouseBack()
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .size(60.dp),
            onClick = {
                navigations.onNagivateToAddDivision(idHouse)
            }
        )
        {
            Icon(Icons.Filled.Add, "")
        }
    }
}

@Preview
@Composable
fun PreviewDivisionsScreens() {
    CMU_07_8200398_8200591_8200592Theme {
        DivisionsScreens(
            onComposing = {},
            navigations = NavigationsDivision(
                onNagivateToDivision = {
                        idhouse, idDivision ->
                },
                onNagivateToAddDivision = {},
                onNagivateToEditHouse = {},
                onNavigateToRemoveHouseBack = {},
                onNavigateToMap = {}
            ),
            idHouse = "SMZapyiMvR1xRnl6Q8Ff"
        )
    }
}