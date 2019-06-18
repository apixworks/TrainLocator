package com.example.trainlocator.models

import com.google.gson.annotations.SerializedName

class Route {

    @SerializedName("id")
    var id: Int = 0
    @SerializedName("from_")
    var from: String? = null
    @SerializedName("to_")
    var to: String? = null
}

class RouteResponse {
    @SerializedName("status")
    var status: String?= null
    @SerializedName("routes")
    var routes: List<Route>? = null
}