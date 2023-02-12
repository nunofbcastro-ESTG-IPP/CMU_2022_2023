package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database

import androidx.room.*

@Entity(
    tableName = "TypesDivisions",
    primaryKeys = ["id"],
)
data class TypeDivision(
    @ColumnInfo(name="id")
    val id: Int,

    @ColumnInfo(name="name")
    val name: String,

    @ColumnInfo(name="icon")
    val icon: String,
)