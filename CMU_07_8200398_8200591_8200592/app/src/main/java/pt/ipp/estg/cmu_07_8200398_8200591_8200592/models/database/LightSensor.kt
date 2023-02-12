package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database

import androidx.room.*

@Entity(
    tableName = "LightSensors",
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
data class LightSensor(
    @ColumnInfo(name="idSensor")
    var idSensor: String,

    @ColumnInfo(name="red")
    var red: Int,

    @ColumnInfo(name="green")
    var green: Int,

    @ColumnInfo(name="blue")
    var blue: Int,

    @ColumnInfo(name="brightness")
    var brightness: Int,
)