package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.firebase

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.House
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.User

data class HouseAndUsers(
    val house: House,
    val users: List<User>,
)