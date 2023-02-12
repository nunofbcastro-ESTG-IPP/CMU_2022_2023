package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.lightsensor

data class Light (
    val ison: Boolean,
    val source: String,
    val has_timer: Boolean,
    val timer_started: Int,
    val timer_duration: Int,
    val timer_remaining: Int,
    val mode: String,
    val red: Int,
    val green: Int,
    val blue: Int,
    val white: Int,
    val gain: Int,
    val temp: Int,
    val brightness: Int,
    val effect: Int,
    val transition: Int
)