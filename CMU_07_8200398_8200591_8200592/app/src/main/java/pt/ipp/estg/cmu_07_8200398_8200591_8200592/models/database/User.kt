package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.database

import androidx.room.*
import com.google.firebase.firestore.PropertyName


@Entity(
    tableName = "Users",
    primaryKeys = ["email"],
)
data class User(
    @get: PropertyName("email") @set: PropertyName("email")
    @ColumnInfo(name="email")
    var email: String = "",

    @get: PropertyName("name") @set: PropertyName("name")
    @ColumnInfo(name="name")
    var name: String = "",

    @get: PropertyName("avatar") @set: PropertyName("avatar")
    @ColumnInfo(name="avatar")
    var avatar: String? = null,
)