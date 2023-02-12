package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database

import androidx.room.*

@Entity(
    tableName = "HousesUsers",
    primaryKeys = ["emailUser", "idHouse"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("email"),
            childColumns = arrayOf("emailUser"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = House::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idHouse"),
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index("emailUser"),
        Index("idHouse"),
    ],
)
data class HouseUser(
    @ColumnInfo(name="emailUser")
    val emailUser: String,

    @ColumnInfo(name="idHouse")
    val idHouse: String,
)