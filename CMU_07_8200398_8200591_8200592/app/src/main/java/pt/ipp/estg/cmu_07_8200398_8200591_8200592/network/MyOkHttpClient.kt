package pt.ipp.estg.cmu_07_8200398_8200591_8200592.network

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

var MyOkHttpClient: OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(5, TimeUnit.SECONDS)
    .build()

