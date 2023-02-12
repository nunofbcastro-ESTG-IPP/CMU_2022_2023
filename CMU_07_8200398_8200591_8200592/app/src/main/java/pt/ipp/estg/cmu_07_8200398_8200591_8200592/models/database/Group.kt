package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "Groups",
    primaryKeys = ["idHouse", "idDivision", "idTypeSensor", "id"],
    foreignKeys = [
        ForeignKey(
            entity = Division::class,
            parentColumns = arrayOf("id", "idHouse"),
            childColumns = arrayOf("idDivision", "idHouse"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TypeSensor::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idTypeSensor"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("idDivision", "idHouse"),
        Index("idTypeSensor"),
        Index(value = ["idHouse", "idDivision", "idTypeSensor", "name"], unique = true),
    ],
)
data class Group(
    @ColumnInfo(name="id")
    var id: String,

    @ColumnInfo(name="idHouse")
    val idHouse: String,

    @ColumnInfo(name="idDivision")
    val idDivision: String,

    @ColumnInfo(name="idTypeSensor")
    val idTypeSensor: Int,

    @ColumnInfo(name="name")
    var name: String,
)