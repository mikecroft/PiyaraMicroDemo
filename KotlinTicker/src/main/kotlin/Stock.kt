import java.io.Serializable

data class Stock(val symbol: String, val description: String, val price: Double) : Serializable {

    override fun toString(): String {
        return "{" + "\"price\":" + price + ",\"symbol\":\"" + symbol + "\"}"

    }
}
