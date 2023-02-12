package pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.sensors.plug

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.basesensor.Meter
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.plugsensor.Relay
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.plugsensor.StatusPlugSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.request.Turn
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.NetworkResult
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.executeRequest
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.verifyURL

class PlugApiRepository {
    suspend fun status(
        baseUrlSensor: String,
    ): NetworkResult<StatusPlugSensor> {
        return executeRequest<StatusPlugSensor>(
            request = {
                PlugApiInitializer(
                    baseUrl = verifyURL(baseUrlSensor),
                ).api.status()
            }
        )
    }

    suspend fun setTurn(
        baseUrlSensor: String,
        turn: Turn,
    ): NetworkResult<Relay> {
        return executeRequest<Relay>(
            request = {
                PlugApiInitializer(
                    baseUrl = verifyURL(baseUrlSensor),
                ).api.setTurn(
                    turn = turn.value,
                )
            }
        )
    }

    suspend fun setTurnDelay(
        baseUrlSensor: String,
        turn: Turn,
        duration: Int,
    ): NetworkResult<StatusPlugSensor> {
        return executeRequest<StatusPlugSensor>(
            request = {
                PlugApiInitializer(
                    baseUrl = verifyURL(baseUrlSensor),
                ).api.setTurnDelay(
                    turn = turn.value,
                    duration = duration,
                )
            }
        )
    }

    suspend fun meters(
        baseUrlSensor: String,
        index: Int
    ): NetworkResult<Meter> {
        return executeRequest<Meter>(
            request = {
                PlugApiInitializer(
                    baseUrl = verifyURL(baseUrlSensor),
                ).api.meters(
                    index = index
                )
            }
        )
    }
}