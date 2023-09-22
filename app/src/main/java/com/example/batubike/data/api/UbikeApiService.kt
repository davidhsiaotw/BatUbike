package com.example.batubike.data.api

import com.example.batubike.data.model.Station
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://tcgbusfs.blob.core.windows.net/dotapp/youbike/v2/"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder().addConverterFactory(
    MoshiConverterFactory.create(moshi)
).baseUrl(BASE_URL).build()

interface UbikeApiService {

    /**
     * [resource](https://tcgbusfs.blob.core.windows.net/dotapp/youbike/v2/youbike_immediate.json)
     */
    @GET("youbike_immediate.json")
    suspend fun getPhotos(): List<Station>
}

object UbikeApi {
    val ubikeApiService: UbikeApiService by lazy { retrofit.create(UbikeApiService::class.java) }
}