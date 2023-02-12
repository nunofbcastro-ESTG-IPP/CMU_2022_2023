package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.BlindSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.Sensor

data class SensorAndBlindSensor(
    @Embedded val sensor: Sensor?,
    @Relation(
        parentColumn = "id",
        entityColumn = "idSensor"
    )
    val blindSensor: BlindSensor?
)