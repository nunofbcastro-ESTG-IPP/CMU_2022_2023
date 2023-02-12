package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.LightSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database.Sensor

data class SensorAndLightSensor(
    @Embedded val sensor: Sensor?,
    @Relation(
        parentColumn = "id",
        entityColumn = "idSensor"
    )
    val lightSensor: LightSensor?
)