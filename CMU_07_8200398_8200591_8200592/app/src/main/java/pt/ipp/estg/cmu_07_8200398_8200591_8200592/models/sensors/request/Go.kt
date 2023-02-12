package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.request

enum class Go(val value: String) {
    OPEN("open"),
    CLOSE("close");

    companion object {
        fun booleanToGo(value: Boolean): Go {
            return when (value) {
                true -> OPEN
                false -> CLOSE
            }
        }
    }
}