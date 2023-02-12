package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.plugsensor

data class Relay (
    val ison: Boolean,
    val has_timer: Boolean,
    val timer_started: Int,
    val timer_duration: Int,
    val timer_remaining: Int,
    val overpower: Boolean,
    val source: String
)