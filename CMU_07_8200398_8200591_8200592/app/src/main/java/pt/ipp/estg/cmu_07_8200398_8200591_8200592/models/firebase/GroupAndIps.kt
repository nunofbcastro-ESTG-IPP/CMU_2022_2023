package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.firebase

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.Group


data class GroupAndIps(
    val group: Group,
    val ips: List<String>,
)