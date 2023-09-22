package com.example.batubike.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Station(
    // https://stackoverflow.com/a/72985109/22598753
    @Json(name = "sno")
    val id: String,
    @Json(name = "sarea")
    val area: String,
    @Json(name = "sna")
    val name: String
)
