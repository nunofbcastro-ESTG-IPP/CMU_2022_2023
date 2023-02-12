package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.division_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.Group
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyButton
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme

data class Option(
    var checked: Boolean,
    var onCheckedChange: (Boolean) -> Unit = {},
    val label: String,
    var enabled: Boolean = true
)

@Composable
private fun LabelledCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors()
) {
    Row(
        modifier = modifier.height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = colors
        )
        Spacer(Modifier.width(32.dp))
        Text(label)
    }
}

@Composable
fun GroupDialog(
    groupList: List<ActiveGroups>,
    setDialog: () -> Unit
) {

    val options = groupList.map { groupState ->
        Option(
            checked = groupState.state,
            onCheckedChange = groupState.onStateChange,
            label = groupState.group.name,
        )
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
                .height(350.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp)
                    .clip(RoundedCornerShape(size = 15.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.change_group_settings), fontSize = 20.sp)

                Column(modifier= Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)) {
                    options.forEach { option ->
                        LabelledCheckbox(
                            checked = option.checked,
                            onCheckedChange = option.onCheckedChange,
                            label = option.label,
                            enabled = option.enabled
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(0.dp, 5.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    MyButton(
                        text = stringResource(id = R.string.save),
                        onClick = {
                            setDialog()
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
    }
}

@Preview
@Composable
fun PreviewLabelledCheckbox() {
    CMU_07_8200398_8200591_8200592Theme {
        LabelledCheckbox(
            checked = true,
            onCheckedChange = {},
            label = "Grups 1",
        )
    }
}

@Preview
@Composable
fun PreviewGroupDialog() {
    CMU_07_8200398_8200591_8200592Theme {
        GroupDialog(
            groupList = listOf<ActiveGroups>(
                ActiveGroups(
                    Group(
                        id = "vCw9guYbGIOIjlUZQ2LR",
                        idHouse = "SMZapyiMvR1xRnl6Q8Ff",
                        idDivision = "61NVojOOCmlJj9hwk1zo",
                        idTypeSensor = 0,
                        name = "Grupo 1",
                    ),
                    true,
                    onStateChange = {},
                ),
                ActiveGroups(
                    Group(
                        id = "vCw9guYbGIOIjlUZQ2LR",
                        idHouse = "SMZapyiMvR1xRnl6Q8Ff",
                        idDivision = "61NVojOOCmlJj9hwk1zo",
                        idTypeSensor = 0,
                        name = "Grupo 2",
                    ),
                    false,
                    onStateChange = {},
                )
            ),
            setDialog= {}
        )
    }
}