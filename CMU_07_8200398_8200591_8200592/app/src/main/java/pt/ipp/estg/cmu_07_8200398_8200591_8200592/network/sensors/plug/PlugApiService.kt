package pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.sensors.plug

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.basesensor.Meter
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.plugsensor.Relay
import pt.ipp.estg.cmu_07_8200398_8200591_8200592.models.sensors.data.plugsensor.StatusPlugSensor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlugApiService {
    @GET("/status")
    suspend fun status(): Response<StatusPlugSensor>

    @GET("/relay/0")
    suspend fun setTurn(
        @Query("turn") turn: String,
    ): Response<Relay>

    @GET("/relay/0?turn={turn.value}&duration={duration}")
    suspend fun setTurnDelay(
        @Query("turn") turn: String,
        @Query("duration") duration: Int
    ): Response<StatusPlugSensor>

    @GET("/meters/{index}")
    suspend fun meters(
        @Path("index") index: Int,
    ): Response<Meter>
}