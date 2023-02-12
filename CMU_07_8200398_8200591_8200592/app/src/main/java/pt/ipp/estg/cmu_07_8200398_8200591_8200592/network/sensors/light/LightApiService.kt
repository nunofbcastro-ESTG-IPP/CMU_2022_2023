package pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.sensors.light

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.basesensor.Meter
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.lightsensor.Light
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.lightsensor.StatusLightSensor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LightApiService {
    @GET("/status")
    suspend fun status(): Response<StatusLightSensor>

    @GET("/light/0?turn=off")
    suspend fun setTurnOff(): Response<Light>

    @GET("/light/0?turn=on")
    suspend fun setTurnOn(
        @Query("red") red: Int, // 0-255
        @Query("green") green: Int, // 0-255
        @Query("blue") blue: Int, // 0-255
        @Query("gain") brightness: Int, // 0-100
    ): Response<Light>

    @GET("/light/0?turn=on")
    suspend fun setTurnOn(
        @Query("white") white: Int, // 0-255
        @Query("brightness") brightness: Int, // 0-100
    ): Response<Light>

    @GET("/meters/{index}")
    suspend fun meters(
        @Path("index") index: Int,
    ): Response<Meter>
}