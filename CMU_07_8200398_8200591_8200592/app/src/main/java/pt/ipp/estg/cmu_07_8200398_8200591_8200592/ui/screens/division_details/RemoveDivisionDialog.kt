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
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.DialogBoxLoading
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SmartHouseViewModels
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyButton
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme

@Composable
fun RemoveDivisionDialog(
    title: String = stringResource(id = R.string.title_remove_division),
    idHouse: String,
    idDivision: String,
    setDialog: () -> Unit,
    onNavigateToBack: () -> Unit
) {
    val smartHouseViewModels: SmartHouseViewModels = viewModel()
    val division = smartHouseViewModels.getDivisionById(idHouse = idHouse, idDivision = idDivision)
        .observeAsState()

    var isClickedSave by remember { mutableStateOf(false) }
    val isLoading = smartHouseViewModels.isLoading.observeAsState()


    LaunchedEffect(key1 = isLoading.value) {
        if (isClickedSave && isLoading.value == false) {
            onNavigateToBack()
            setDialog()
        }
    }

    division.value?.let {
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
                    Spacer(modifier = Modifier.padding(0.dp, 5.dp))
                    Text(text = "${stringResource(id = R.string.message_remove_division)} \"${it.name}\"")
                    Row(modifier = Modifier.fillMaxWidth()) {
                        MyButton(
                            text = stringResource(id = R.string.yes),
                            onClick = {
                                isClickedSave = true
                                smartHouseViewModels.removeDivision(it)
                            },
                            modifierButton = Modifier
                                .weight(1f)
                                .padding(5.dp)
                                .clip(RoundedCornerShape(15.dp))
                        )
                        Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                        MyButton(
                            text = stringResource(id = R.string.no),
                            onClick = { setDialog() },
                            modifierButton = Modifier
                                .weight(1f)
                                .padding(5.dp)
                                .clip(RoundedCornerShape(15.dp))
                        )
                    }
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
fun PreviewRemoveDivisionDialog() {
    CMU_07_8200398_8200591_8200592Theme {
        RemoveDivisionDialog(
            idHouse = "SMZapyiMvR1xRnl6Q8Ff",
            idDivision = "61NVojOOCmlJj9hwk1zo",
            setDialog = {},
            onNavigateToBack = {}
        )
    }
}