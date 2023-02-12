package pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.screens.houses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.House
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.LoadableAsyncImage
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.components.MyEnergyConsumption
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.ui.theme.CMU_07_8200398_8200591_8200592Theme

@Composable
fun MyHouseCard(
    onNagivateToHouse: (idHouse: String) -> Unit,
    house: House,
    consume: Double
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        elevation = 8.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .height(150.dp)
                .clickable { onNagivateToHouse(house.id) }
        ) {
            LoadableAsyncImage(
                imageRequest = house.photo,
                contentDescription = house.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(150.dp)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = house.name,
                    modifier = Modifier.padding(start = 20.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                MyEnergyConsumption(consume = consume, 55.dp)
            }

        }
    }
}

@Preview
@Composable
fun PreviewMyHouseCard() {
    CMU_07_8200398_8200591_8200592Theme {
        MyHouseCard(
            onNagivateToHouse = {},
            house = House(
                id = "SMZapyiMvR1xRnl6Q8Ff",
                name = "Casa Teste",
                photo = "https://thumbor.forbes.com/thumbor/fit-in/900x510/https://www.forbes.com/home-improvement/wp-content/uploads/2022/07/download-23.jpg",
                latitude = 41.3667788,
                longitude = -8.1928864,
            ),
            consume = 200.00,
        )
    }
}