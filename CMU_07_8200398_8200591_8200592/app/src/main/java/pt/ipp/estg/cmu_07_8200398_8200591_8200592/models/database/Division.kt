package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database

import androidx.room.*

@Entity(
    tableName = "Divisions",
    primaryKeys = ["id", "idHouse"],
    foreignKeys = [
        ForeignKey(
            entity = House::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idHouse"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TypeDivision::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idTypeDivision"),
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index("idHouse"),
        Index("idTypeDivision"),
    ],
)
data class Division(
    @ColumnInfo(name="id")
    var id: String,

    @ColumnInfo(name="idHouse")
    val idHouse: String,

    @ColumnInfo(name="name")
    var name: String,

    @ColumnInfo(name="nDevices")
    val nDevices: Int = 0,

    @ColumnInfo(name="idTypeDivision")
    var idTypeDivision: Int,
)