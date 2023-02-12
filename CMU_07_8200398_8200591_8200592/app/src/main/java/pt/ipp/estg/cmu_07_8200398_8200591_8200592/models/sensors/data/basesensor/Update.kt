package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.basesensor

data class Update (
    val status: String,
    val has_update: Boolean,
    val new_version: String,
    val old_version: String
)
