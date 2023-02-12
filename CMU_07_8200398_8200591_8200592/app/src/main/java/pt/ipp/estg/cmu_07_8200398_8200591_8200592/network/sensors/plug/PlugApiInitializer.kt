package pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.sensors.plug

import pt.ipp.estg.cmu_07_8200398_8200591_8200592.network.MyOkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlugApiInitializer(
    baseUrl: String,
) {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(MyOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: PlugApiService by lazy {
        retrofit.create(PlugApiService::class.java)
    }

}