package ro.scoalainformala.trips

import com.google.gson.annotations.SerializedName

class WeatherResponse {
    @SerializedName("coord")
    var coord: Coord? = null

    @SerializedName("sys")
    var sys: Sys? = null

    @SerializedName("weather")
    var weather: ArrayList<Weather> = ArrayList()

    @SerializedName("main")
    var main: Main? = null

    @SerializedName("wind")
    var wind: Wind? = null

    @SerializedName("rain")
    var rain: Rain? = null

    @SerializedName("clouds")
    var clouds: Clouds? = null

    @SerializedName("dt")
    var dt: Float = 0f

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("cod")
    var cod: Float = 0f
}

class Weather {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("main")
    var main: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("icon")
    var icon: String? = null
}

class Clouds {
    @SerializedName("all")
    var all: Float = 0f
}

class Rain {
    @SerializedName("3h")
    var h3: Float = 0f
}

class Wind {
    @SerializedName("speed")
    var speed: Float = 0f

    @SerializedName("deg")
    var deg: Float = 0f
}

class Main {
    @SerializedName("temp")
    var temp: Float = 0f

    @SerializedName("humidity")
    var humidity: Float = 0f

    @SerializedName("pressure")
    var pressure: Float = 0f

    @SerializedName("temp_min")
    var temp_min: Float = 0f

    @SerializedName("temp_max")
    var temp_max: Float = 0f
}

class Sys {
    @SerializedName("country")
    var country: String? = null

    @SerializedName("sunrise")
    var sunrise: Long = 0

    @SerializedName("sunset")
    var sunset: Long = 0
}

class Coord {
    @SerializedName("lon")
    var lon: Float = 0f

    @SerializedName("lat")
    var lat: Float = 0f
}