package com.example.trainlocator.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BookingResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("response")
    @Expose
    var response: Response? = null
}

class Response {
    @SerializedName("route_id")
    @Expose
    var routeId: Int? = null
    @SerializedName("stations")
    @Expose
    var stations: List<String>? = null
}