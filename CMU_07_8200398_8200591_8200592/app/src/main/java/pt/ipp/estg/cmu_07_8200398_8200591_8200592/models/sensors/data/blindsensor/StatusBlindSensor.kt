package pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.blindsensor

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.basesensor.*

data class StatusBlindSensor (
    override val wifi_sta: WifiSta,
    override val cloud: Cloud,
    override val mqtt: Mqtt,
    override val time: String,
    override val unixtime: Long,
    override val serial: Int,
    override val has_update: Boolean,
    override val mac: String,
    override val cfg_changed_cnt: Int,
    override val actions_stats: ActionsStats,
    val rollers: List<Roller>,
    override val meters: List<Meter>,
    val inputs: List<Input>,
    override val temperature: Double,
    override val overtemperature: Boolean,
    override val tmp: Tmp,
    override val update: Update,
    val temperature_status: String,
    override val ram_total: Int,
    override val ram_free: Int,
    override val fs_size: Int,
    override val fs_free: Int,
    val voltage: Double,
    override val uptime: Int,
) : StatusSensor