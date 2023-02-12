package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database

import androidx.room.*

@Entity(
    tableName = "PlugSensors",
    primaryKeys = ["idSensor"],
    foreignKeys = [
        ForeignKey(
            entity = Sensor::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idSensor"),
            onDelete = ForeignKey.CASCADE
        ),
    ],
)
data class PlugSensor(
    @ColumnInfo(name="idSensor")
    var idSensor: String,

    @ColumnInfo(name="status")
    var status: Boolean,
)