package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.basesensor

interface StatusSensor {
    val wifi_sta: WifiSta
    val cloud: Cloud
    val mqtt: Mqtt
    val time: String
    val unixtime: Long
    val serial: Int
    val has_update: Boolean
    val mac: String
    val cfg_changed_cnt: Int
    val actions_stats: ActionsStats
    val meters: List<Meter>
    val temperature: Double
    val overtemperature: Boolean
    val tmp: Tmp
    val update: Update
    val ram_total: Int
    val ram_free: Int
    val fs_size: Int
    val fs_free: Int
    val uptime: Int
}