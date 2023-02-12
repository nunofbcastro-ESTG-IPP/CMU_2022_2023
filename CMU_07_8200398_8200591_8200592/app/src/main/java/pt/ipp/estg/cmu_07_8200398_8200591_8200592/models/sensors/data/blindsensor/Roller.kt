package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.blindsensor

data class Roller (
    val state: String,
    val source: String,
    val power: Int,
    val is_valid: Boolean,
    val safety_switch: Boolean,
    val overtemperature: Boolean,
    val stop_reason: String,
    val last_direction: String,
    val current_pos: Int,
    val calibrating: Boolean,
    val positioning: Boolean
)