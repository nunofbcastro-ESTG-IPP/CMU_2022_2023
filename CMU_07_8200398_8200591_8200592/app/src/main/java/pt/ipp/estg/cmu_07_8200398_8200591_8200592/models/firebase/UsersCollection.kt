package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.firebase

data class UsersCollection(
    val collectionName: String = "Users",
    val fieldName: String = "name",
    val fieldEmail: String = "email",
    val fieldAvatar: String = "avatar",
)