package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.basesensor

data class WifiSta (
    val connected: Boolean,
    val ssid: String,
    val ip: String,
    val rssi: Int
)