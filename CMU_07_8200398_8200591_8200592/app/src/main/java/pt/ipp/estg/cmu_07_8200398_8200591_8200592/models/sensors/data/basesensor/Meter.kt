package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.basesensor

data class Meter (
    val power: Double,
    val overpower: Int?,
    val is_valid: Boolean,
    val timestamp: Int,
    val counters: List<Double>,
    val total: Double
)