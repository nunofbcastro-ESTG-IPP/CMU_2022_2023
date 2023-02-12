package pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.sensors.blind

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.basesensor.Meter
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.blindsensor.Roller
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.blindsensor.StatusBlindSensor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BlindApiService {
    @GET("/status")
    suspend fun status(): Response<StatusBlindSensor>

    @GET("/roller/0")
    suspend fun setGo(
        @Query("turn") go: String,
    ): Response<Roller>

    @GET("/roller/0?go=to_pos")
    suspend fun setGo(
        @Query("roller_pos") position: Int,
    ): Response<Roller>

    @GET("/meters/{index}")
    suspend fun meters(
        @Path("index") index: Int,
    ): Response<Meter>
}