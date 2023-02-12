package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.divisions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.R
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.viewmodels.SmartHouseViewModels
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.Division
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.TitleAndSubtitle
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.utils.getId

@Composable
fun MyDivisionCard(
    onNagivateToDivision: (idDivision: String) -> Unit,
    division: Division
)
{
    val smartHouseViewModels: SmartHouseViewModels = viewModel()
    val typeDivision =
        smartHouseViewModels.getTypeDivision(division.idTypeDivision).observeAsState()
    typeDivision.value?.let {
        val icon = R.drawable::class.java.getId(
            it.icon
        )
        Card(
            modifier = Modifier
                .padding(4.dp)
                .padding(end = 15.dp, bottom = 15.dp)
                .fillMaxWidth(),
            elevation = 8.dp,
            shape = RoundedCornerShape(20.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onNagivateToDivision(division.id) }
                    .padding(start = 10.dp)
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = division.name,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colors.secondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                TitleAndSubtitle(
                    title = division.name,
                    //alterar numero de devices
                    subtitle = "${division.nDevices} ${stringResource(id = R.string.devices)}"
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewMyDivisionCard() {
    CMU_07_8200398_8200591_8200592Theme {
        MyDivisionCard(
            onNagivateToDivision = {},
            division = Division(
                id = "61NVojOOCmlJj9hwk1zo",
                idHouse = "SMZapyiMvR1xRnl6Q8Ff",
                name = "Sala",
                nDevices = 0,
                idTypeDivision = 0
            )
        )
    }
}