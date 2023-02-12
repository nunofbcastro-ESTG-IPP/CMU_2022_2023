package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.firebase

data class HousesCollection(
    val collectionName: String = "Houses",
    val fieldName: String = "name",
    val fieldPhoto: String = "photo",
    val fieldLatitude: String = "latitude",
    val fieldLongitude: String = "longitude",
    val fieldUsers: String = "users",
)