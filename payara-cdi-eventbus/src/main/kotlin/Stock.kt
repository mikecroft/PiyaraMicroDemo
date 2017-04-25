import java.io.Serializable

data class Stock(val symbol: String, val description: String, val price: Double) : Serializable {

//    var symbol: String = "NOTHING"
//    var description: String = "Uninitialised Stock object"
//    var price: Double = 0.0

    override fun toString(): String {
        return "{" + "\"price\":" + price + ",\"symbol\":\"" + symbol + "\"}"


    }

}