package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.request

enum class Turn(val value: String) {
    ON("on"),
    OFF("off"),
    TOGGLE("toggle");

    companion object {
        fun booleanToTurn(value: Boolean?): Turn {
            return when (value) {
                true -> Turn.ON
                false -> Turn.OFF
                else -> TOGGLE
            }
        }
    }
}