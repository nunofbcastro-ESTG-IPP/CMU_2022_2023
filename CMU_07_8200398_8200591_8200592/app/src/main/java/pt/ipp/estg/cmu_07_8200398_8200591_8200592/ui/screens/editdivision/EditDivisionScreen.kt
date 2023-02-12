package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.editdivision

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SmartHouseViewModels
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.TypeDivision
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.AppBarState
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.DialogBoxLoading
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyButton
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyErrorMessage
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyTextField
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.myCustomColors
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.getId
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.houseAndDivisionNameVaidation

@Composable
fun EditDivisionScreen(
    onComposing: (AppBarState) -> Unit,
    onNavigateToBack: () -> Unit,
    idDivision: String,
    idHouse: String
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
    val divisionTypes = smartHouseViewModels.getTypesDivisions().observeAsState()
    var divisionNameInput by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf<TypeDivision?>(null) }
    var isError by remember { mutableStateOf(false) }

    division.value?.let {
        val id = smartHouseViewModels.getTypeDivision(it.idTypeDivision).observeAsState()
        id.value?.let { typeDivision ->
            LaunchedEffect(Unit) {
                divisionNameInput = it.name
                selectedOption = typeDivision
            }

        }
    }

    var isClickedSave by remember { mutableStateOf(false) }
    val isLoading = smartHouseViewModels.isLoading.observeAsState()

    LaunchedEffect(key1 = isLoading.value) {
        if (isClickedSave && isLoading.value == false) {
            isClickedSave = false
            onNavigateToBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.edit_division), fontSize = 25.sp,
            color = MaterialTheme.myCustomColors.subtitle
        )
        Spacer(modifier = Modifier.height(20.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            content = {
                divisionTypes.value?.let {
                    items(it) { typeDivision ->
                        Card(
                            modifier = Modifier
                                .padding(4.dp)
                                .padding(end = 15.dp, bottom = 15.dp)
                                .fillMaxWidth()
                                .height(70.dp)
                                .border(
                                    2.dp, color = if (selectedOption == typeDivision) {
                                        MaterialTheme.colors.secondary
                                    } else {
                                        Color.Transparent
                                    }, shape = RoundedCornerShape(20.dp)
                                ),
                            elevation = 8.dp,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .clickable { selectedOption = typeDivision }
                                    .padding(10.dp),
                                painter = painterResource(
                                    id = R.drawable::class.java.getId(
                                        typeDivision.icon
                                    )
                                ),
                                contentDescription = "Division Icon",
                                tint = if (selectedOption == typeDivision) {
                                    MaterialTheme.colors.secondary
                                } else {
                                    MaterialTheme.colors.onBackground
                                },
                            )
                        }

                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        selectedOption?.let {
            MyTextField(
                value = divisionNameInput,
                onValueChange = { it1 ->
                    divisionNameInput = it1
                },
                placeholder = stringResource(
                    id = R.string::class.java.getId(
                        it.name
                    )
                ),
                trailingIcon = {
                    Icon(
                        painter = painterResource(
                            id = R.drawable::class.java.getId(
                                it.icon
                            )
                        ),
                        modifier = Modifier.size(20.dp),
                        contentDescription = "Division Icon",
                        tint = MaterialTheme.myCustomColors.textFieldIcon
                    )
                },
                keyboardType = KeyboardType.Text,
            )
            if (isError) {
                MyErrorMessage(text = stringResource(id = R.string.invalid_name_division))

            }
        }
        division.value?.let { division ->
            selectedOption?.let { typeDivision ->
                MyButton(
                    modifierButton = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 5.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    text = stringResource(id = R.string.save),
                    onClick = {
                        if (!houseAndDivisionNameVaidation(divisionNameInput)) {
                            isError = true
                            return@MyButton
                        }

                        isClickedSave = true

                        division.idTypeDivision = typeDivision.id
                        division.name = divisionNameInput
                        smartHouseViewModels.updateDivision(division)
                    }
                )
            }
        }

        if (isLoading.value == true && isClickedSave) {
            DialogBoxLoading()
        }
    }


}

@Preview
@Composable
fun PreviewDivisionScreen() {
    CMU_07_8200398_8200591_8200592Theme {
        EditDivisionScreen(
            onComposing = {},
            onNavigateToBack = {},
            idDivision = "61NVojOOCmlJj9hwk1zo",
            idHouse = "SMZapyiMvR1xRnl6Q8Ff"
        )
    }
}
