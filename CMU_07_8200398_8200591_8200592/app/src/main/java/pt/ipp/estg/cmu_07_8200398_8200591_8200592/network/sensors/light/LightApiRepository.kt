package pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.sensors.light

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.basesensor.Meter
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.lightsensor.Light
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.lightsensor.StatusLightSensor
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.NetworkResult
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.executeRequest
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.verifyURL

class LightApiRepository {
    suspend fun status(
        baseUrlSensor: String
    ): NetworkResult<StatusLightSensor> {
        return executeRequest<StatusLightSensor>(
            request = {
                LightApiInitializer(
                    baseUrl = verifyURL(baseUrlSensor),
                ).api.status()
            }
        )
    }

    suspend fun setTurnOff(
        baseUrlSensor: String
    ): NetworkResult<Light> {
        return executeRequest<Light>(
            request = {
                LightApiInitializer(
                    baseUrl = verifyURL(baseUrlSensor),
                ).api.setTurnOff()
            }
        )
    }

    suspend fun setTurnOn(
        baseUrlSensor: String,
        red: Int,
        green: Int,
        blue: Int,
        brightness: Int,
    ): NetworkResult<Light> {
        return executeRequest<Light>(
            request = {
                LightApiInitializer(
                    baseUrl = verifyURL(baseUrlSensor),
                ).api.setTurnOn(
                    red = red,
                    green = green,
                    blue = blue,
                    brightness = brightness,
                )
            }
        )
    }

    suspend fun setTurnOn(
        baseUrlSensor: String,
        white: Int,
        brightness: Int,
    ): NetworkResult<Light> {
        return executeRequest<Light>(
            request = {
                LightApiInitializer(
                    baseUrl = verifyURL(baseUrlSensor),
                ).api.setTurnOn(
                    white = white,
                    brightness = brightness,
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
                LightApiInitializer(
                    baseUrl = verifyURL(baseUrlSensor),
                ).api.meters(
                    index = index
                )
            }
        )
    }
}