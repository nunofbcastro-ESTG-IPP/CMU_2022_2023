package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database

import androidx.room.*

@Entity(
    tableName = "Sensors",
    primaryKeys = ["id"],
    foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = arrayOf("idHouse", "idDivision", "idTypeSensor", "id"),
            childColumns = arrayOf("idHouse", "idDivision", "idTypeSensor","idGroup"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("idHouse", "idDivision", "idTypeSensor","idGroup"),
        Index(value = ["ip", "idHouse"], unique = true)
    ],
)
data class Sensor(
    @ColumnInfo(name="id")
    var id: String,

    @ColumnInfo(name="ip")
    val ip: String,

    @ColumnInfo(name="idHouse")
    val idHouse: String,

    @ColumnInfo(name="idDivision")
    val idDivision: String,

    @ColumnInfo(name="idTypeSensor")
    val idTypeSensor: Int,

    @ColumnInfo(name="consumption")
    var consumption: Double,

    @ColumnInfo(name="idGroup")
    var idGroup: String,
)