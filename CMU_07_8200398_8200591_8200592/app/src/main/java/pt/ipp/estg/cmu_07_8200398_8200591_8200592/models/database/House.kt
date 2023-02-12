package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database

import androidx.room.*

@Entity(
    tableName = "Houses",
    primaryKeys = ["id"],
)
data class House(
    @ColumnInfo(name="id")
    var id: String,

    @ColumnInfo(name="name")
    var name: String,

    @ColumnInfo(name="photo")
    var photo: String,

    @ColumnInfo(name="latitude")
    var latitude: Double,

    @ColumnInfo(name="longitude")
    var longitude: Double,
)