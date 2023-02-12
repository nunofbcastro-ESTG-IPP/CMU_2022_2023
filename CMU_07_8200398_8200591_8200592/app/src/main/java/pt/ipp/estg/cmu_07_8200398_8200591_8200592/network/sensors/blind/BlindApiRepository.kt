package pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.sensors.blind

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.basesensor.Meter
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.blindsensor.Roller
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.blindsensor.StatusBlindSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.request.Go
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.NetworkResult
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.executeRequest
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.verifyURL

class BlindApiRepository {
    suspend fun status(
        baseUrlSensor: String,
    ): NetworkResult<StatusBlindSensor> {
        return executeRequest<StatusBlindSensor>(
            request = {
                BlindApiInitializer(
                    baseUrl = verifyURL(baseUrlSensor),
                ).api.status()
            }
        )
    }

    suspend fun setGo(
        baseUrlSensor: String,
        go: Go,
    ): NetworkResult<Roller> {
        return executeRequest<Roller>(
            request = {
                BlindApiInitializer(
                    baseUrl = verifyURL(baseUrlSensor),
                ).api.setGo(
                    go = go.value,
                )
            }
        )
    }

    suspend fun setGo(
        baseUrlSensor: String,
        position: Int,
    ): NetworkResult<Roller> {
        return executeRequest<Roller>(
            request = {
                BlindApiInitializer(
                    baseUrl = verifyURL(baseUrlSensor),
                ).api.setGo(
                    position = position,
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
                BlindApiInitializer(
                    baseUrl = verifyURL(baseUrlSensor),
                ).api.meters(
                    index = index
                )
            }
        )
    }
}